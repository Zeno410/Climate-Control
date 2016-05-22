
package climateControl.customGenLayer;
import climateControl.utils.Zeno410Logger;
import climateControl.genLayerPack.GenLayerPack;
import climateControl.utils.Acceptor;
import climateControl.utils.IntPad;
import climateControl.utils.PlaneLocated;
import climateControl.utils.PlaneLocation;
import climateControl.utils.StringWriter;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import java.util.logging.Logger;

/**
 *
 * This class tests a GenLayer to ensure the same location always has the same value
 * @author Zeno410
 */
public class GenLayerConfirm extends GenLayerPack{

    private int exclusion;

    private PlaneLocation lastHit= new PlaneLocation(Integer.MAX_VALUE,Integer.MAX_VALUE);
    private PlaneLocation lastSize= new PlaneLocation(Integer.MAX_VALUE,Integer.MAX_VALUE);
    private IntPad lastInput = new IntPad();

    public static Logger logger = new Zeno410Logger("Cache").logger();

    private PlaneLocated<Integer> storedVals  = new PlaneLocated<Integer>();
    public boolean testing;

    public GenLayerConfirm(GenLayer parent, int exclusion) {
        super(0L);
        this.parent = parent;
        this.exclusion = exclusion;
    }

    public GenLayerConfirm(GenLayer parent) {
        this(parent,0);
    }

    public GenLayer parent() {
        return parent;
    }
    
    @Override
    public void initWorldGenSeed(long par1) {
        super.initWorldGenSeed(par1);
        if (testing)
        throw new RuntimeException(""+par1);
    }
    public int [] getInts(int x0, int z0, int xSize, int zSize) {
        PlaneLocation calllocation = new PlaneLocation(x0,z0);
        PlaneLocation size = new PlaneLocation(xSize,zSize);
        if (calllocation.equals(this.lastHit)&&size.equals(this.lastSize)) {
            //return output.pad(par3 * par4);
        }
        //logger.info("location " + x0 + " " + z0 + " " + xSize + " " + zSize);
        PlaneLocation.Probe probe = new PlaneLocation.Probe(x0,z0);
        String problems = probe.toString()+":";
        int originalLength = problems.length();
        int[] result = parent.getInts(x0, z0, xSize, zSize);
        try {
            taste(result, xSize * zSize);
        } catch (Exception e) {
            throw new RuntimeException(parent.toString());
        }
        for (int x=exclusion; x<xSize-exclusion;x++) {
            probe.setX(x+x0);
            for (int z = exclusion; z<zSize-exclusion;z++) {
                probe.setZ(z+z0);
                Integer locked = storedVals.get(probe);
                if (locked == null) {
                    // not stored, get from parent
                    locked = result[(z)*(xSize)+x];
                    // and store for future reference
                    PlaneLocation location = new PlaneLocation(probe.x(),probe.z());
                    storedVals.put(location, locked);
                } else {
                    //logger.info("hit "+probe.toString()+locked.intValue());
                    // already stored
                    if (result[(z)*xSize+x] !=locked.intValue()) {
                        if (new PlaneLocation(x+x0,z+z0).hashCode() != probe.hashCode()) throw new RuntimeException();
                        if (!(new PlaneLocation(x+x0,z+z0).equals(probe))) throw new RuntimeException();
                        problems += new PlaneLocation(x,z).toString()+
                                "["+result[(z)*(xSize)+x]+"->"+locked.intValue()+"]";
                    }
                }
            }
        }
        if (problems.length()>originalLength){
            StringWriter output = null;
            try {
                output = new StringWriter(new File("confirmationProblems.txt"));
            } catch (IOException ex) {
                throw new RuntimeException();
            }
            for (int x=exclusion; x<xSize-exclusion;x++) {
                probe.setX(x+x0);
                String printed = "";
                for (int z = exclusion; z<zSize-exclusion;z++) {
                    probe.setZ(z+z0);
                    Integer locked = storedVals.get(probe);
                    printed  += "" + locked + '\t';
                }
                output.accept(printed);
            }
            output.accept("");
            output.accept(calllocation.toString()+ size.toString());
            for (int x=exclusion; x<xSize-exclusion;x++) {
                String printed = "";
                for (int z = exclusion; z<zSize-exclusion;z++) {
                    printed  += "" + result[(z)*(xSize)+x] + '\t';
                }
                output.accept(printed);
            }
            output.accept("");
            int [] oldData = this.lastInput.pad(result.length);
            if (oldData.length>0) {
                output.accept(lastHit.toString()+ lastSize.toString());
                for (int x=exclusion; x<xSize-exclusion;x++) {
                    String printed = "";
                    for (int z = exclusion; z<zSize-exclusion;z++) {
                        printed  += "" + oldData[(z)*(xSize)+x] + '\t';
                    }
                    output.accept(printed);
                }
            }
            output.done();
            throw new RuntimeException(parent.toString() + problems);
        }
        int [] oldData = this.lastInput.pad(result.length);
        for (int i= 0; i < oldData.length; i ++) {
            oldData[i] = result[i];
        }


        lastHit = calllocation;
        lastSize = size;
        taste(result,xSize*zSize);
        return result;
    }

}