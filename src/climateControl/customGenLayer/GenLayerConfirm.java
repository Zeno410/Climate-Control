
package climateControl.customGenLayer;
import com.Zeno410Utils.Zeno410Logger;

import climateControl.api.BiomeSettings;
import climateControl.genLayerPack.GenLayerPack;
import com.Zeno410Utils.PlaneLocated;
import com.Zeno410Utils.PlaneLocation;
import net.minecraft.world.gen.layer.GenLayer;
import java.util.logging.Logger;

/**
 *
 * This class tests a GenLayer to ensure the same location always has the same value
 * @author Zeno410
 */
public class GenLayerConfirm extends GenLayerPack{

    private int exclusion;

    public static Logger logger = new Zeno410Logger("Cache").logger();

    public String name = "";

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

    public GenLayerConfirm(GenLayer parent, String name) {
        this(parent,0);
        this.name = name;
    }

    @Override
    public void initWorldGenSeed(long par1) {
        super.initWorldGenSeed(par1);
        if (testing)
        throw new RuntimeException(""+par1);
    }
    public int [] getInts(int x0, int z0, int xSize, int zSize) {
        //logger.info("location " + x0 + " " + z0 + " " + xSize + " " + zSize);
        PlaneLocation.Probe probe = new PlaneLocation.Probe(x0,z0);
        String problems = probe.toString()+":";
        int originalLength = problems.length();
        int[] result = parent.getInts(x0, z0, xSize, zSize);
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
                    if (result[(z)*xSize+x] !=locked) {
                        problems += new PlaneLocation(x,z).toString()+
                                "["+result[(z)*(xSize)+x]+"->"+locked+"]";
                    };
                }
            }
        }
        if (problems.length()>originalLength){
            String grandparent = "";
            try {
                grandparent = ((GenLayerPack)(parent)).getParent().toString();
            } catch (Exception e) {}
            throw new RuntimeException(problems + " " + parent.toString()+ " " + grandparent);
        }
        return result;
    }

}