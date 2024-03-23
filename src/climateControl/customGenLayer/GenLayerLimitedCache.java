package climateControl.customGenLayer;
import climateControl.genLayerPack.GenLayerPack;
import com.Zeno410Utils.PlaneLocated;
import com.Zeno410Utils.PlaneLocation;
import java.io.DataOutputStream;
import java.util.ArrayList;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;


/**
 * This is a version of GenLayerCache that only caches a limited set of objects
 * @author Zeno410
 */
public class GenLayerLimitedCache extends GenLayerPack{

    //public static Logger logger = new Zeno410Logger("Cache").logger();

    private PlaneLocated<Integer> storedVals  = new PlaneLocated<Integer>();
    private PlaneLocatedRecorder target;
    private final int limit;
    private final ArrayList<PlaneLocation> currentStored;
    int nextSlot = 0;
    boolean full = false;

    public GenLayerLimitedCache(GenLayer parent, int limit) {
        super(0L);
        this.parent = parent;
        this.limit = limit;
        this.currentStored = new ArrayList<PlaneLocation>(limit);
        this.currentStored.ensureCapacity(limit);
    }

    public GenLayerLimitedCache(GenLayer parent,DataOutputStream target, int limit) {
        this(parent,limit);
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
                    // clear existing if full
                    if (full) {
                        PlaneLocation oldLocation = currentStored.get(nextSlot);
                        storedVals.remove(oldLocation);
                    }
                    storedVals.put(location, locked);
                    if (full) {
                        currentStored.set(nextSlot, location);
                    } else {
                        currentStored.add(location);
                    }
                    if (nextSlot++ >= limit) {
                        nextSlot = 0;
                        full = true;
                    }
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


