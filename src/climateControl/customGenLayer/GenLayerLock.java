
package climateControl.customGenLayer;
import com.Zeno410Utils.Zeno410Logger;
import climateControl.genLayerPack.GenLayerPack;
import com.Zeno410Utils.PlaneLocated;
import com.Zeno410Utils.PlaneLocation;
import java.util.HashMap;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import java.util.logging.Logger;

/**
 * This
 * @author Zeno410
 */
public class GenLayerLock extends GenLayerPack{

    public static Logger logger = new Zeno410Logger("GenLayerLock").logger();

    private boolean watching = false;
    private PlaneLocated<Integer> storedVals ;
    private int excludeEdge = 3;

    public GenLayerLock(GenLayer parent, PlaneLocated<Integer> storedVals, int excludeEdge) {
        super(0L);
        this.parent = parent;
        this.storedVals = storedVals;
        this.excludeEdge = excludeEdge;
    }

    public void setWatch(boolean newWatching) {
        watching = newWatching;
    }
    
    public int [] getInts(int x0, int z0, int xSize, int zSize) {
        if (watching) logger.info("location " + x0 + " " + z0 + " " + xSize + " " + zSize);
        HashMap<PlaneLocation,Integer> addedVals = null;
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
                        addedVals = new HashMap<PlaneLocation,Integer>();
                    }
                    locked = parentInts[(z)*(xSize)+x];
                    //logger.info("miss "+probe.toString()+locked.intValue());
                    result[(z)*xSize+x] = locked;
                    // and store for future reference
                    PlaneLocation location = new PlaneLocation(probe.x(),probe.z());
                    if (shouldStore(location,x0,z0,xSize,zSize)){
                        if (watching) {
                            logger.info("locking "+location.toString()+" to " +locked + " "+parent.toString());
                        }
                       addedVals.put(location, locked);
                    }
                } else {
                    //logger.info("hit "+probe.toString()+locked.intValue());
                    // already stored
                    result[(z)*xSize+x] = locked;
                }
            }
        }
        // if we've had to look anything up, save it
        // clustered because this will set off a write operation normally
        if ((addedVals != null)) { 
            if (addedVals.size() >0) {
                storedVals.putAll(addedVals);
                if (watching) logger.info("lock size "+storedVals.size());
            }
        }
        return result;
    }

    private final boolean shouldStore(PlaneLocation location, int x0, int z0, int xSize, int zSize) {
        int thisExclusion = Math.min(excludeEdge,xSize/2-1);

        if ((location.x()-x0)<thisExclusion) return false;
        if ((location.z()-z0)<thisExclusion) return false;
        if (x0+xSize -(location.x())<thisExclusion) return false;
        if (z0+zSize -(location.z())<thisExclusion) return false;
        return true;
    }

}