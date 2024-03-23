
package climateControl.customGenLayer;
import climateControl.genLayerPack.GenLayerPack;
import com.Zeno410Utils.Acceptor;
import com.Zeno410Utils.PlaneLocated;
import com.Zeno410Utils.PlaneLocation;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.logging.Level;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import java.util.logging.Logger;


/**
 *
 * @author Zeno410
 */
public class GenLayerCache extends GenLayerPack{

    //public static Logger logger = new Zeno410Logger("Cache").logger();

    private PlaneLocated<Integer> storedVals  = new PlaneLocated<Integer>();
    private PlaneLocatedRecorder target;

    public GenLayerCache(GenLayer parent) {
        super(0L);
        this.parent = parent;
    }
    
    public GenLayerCache(GenLayer parent,DataOutputStream target) {
        super(0L);
        this.parent = parent;
        this.target = new PlaneLocatedRecorder(target);
    }

    @Override
    public void initWorldGenSeed(long par1) {
        if (target != null) target.writeSeed(par1);
        super.initWorldGenSeed(par1);
    }
    
    public int [] getInts(int x0, int z0, int xSize, int zSize) {
        //logger.info("location " + x0 + " " + z0 + " " + xSize + " " + zSize);
        PlaneLocation.Probe probe = new PlaneLocation.Probe(x0,z0);
        int [] parentInts = null;
        int[] result = IntCache.getIntCache(xSize * zSize);
        for (int x=0; x<xSize;x++) {
            probe.setX(x+x0);
            for (int z = 0; z<zSize;z++) {
                probe.setZ(z+z0);
                Integer locked = storedVals.get(probe);
                if (locked == null) {
                    // not stored, get from parent
                    if (parentInts == null) {
                        parentInts = parent.getInts(x0, z0, xSize, zSize);
                    }
                    locked = parentInts[(z)*(xSize)+x];
                    result[(z)*xSize+x] = locked;
                    // and store for future reference
                    PlaneLocation location = new PlaneLocation(probe.x(),probe.z());
                    storedVals.put(location, locked);
                } else {
                    //logger.info("hit "+probe.toString()+locked.intValue());
                    // already stored
                    result[(z)*xSize+x] = locked;
                }
            }
        }
        if (target != null) target.accept(storedVals);
        return result;
    }

}
class PlaneLocatedRecorder extends Acceptor<PlaneLocated<Integer>> {

    public PlaneLocatedRecorder(DataOutputStream target) {
        recording = target;
    }

    public void writeSeed(long seed) {
        try {
            recording.writeUTF("seed " + seed+ '\r');
        } catch (IOException ex) {
                throw new RuntimeException(ex);
        }
    }
    final DataOutputStream recording;

    final HashSet<PlaneLocation> alreadyRecorded = new HashSet<PlaneLocation>();
    @Override
    public void accept(PlaneLocated<Integer> accepted) {
        for (PlaneLocation location: accepted.locations()) {
            if (alreadyRecorded.contains(location)) continue;
            alreadyRecorded.add(location);
            String toWrite = ""+location.x() + '\t' + location.z() + '\t' +accepted.get(location)+ '\r';
            try {
                recording.writeUTF(toWrite);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

}
