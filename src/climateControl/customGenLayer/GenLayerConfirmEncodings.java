
package climateControl.customGenLayer;
import com.Zeno410Utils.Zeno410Logger;
import climateControl.genLayerPack.GenLayerPack;
import climateControl.generator.Decoder;
import com.Zeno410Utils.PlaneLocated;
import com.Zeno410Utils.PlaneLocation;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import java.util.logging.Logger;

/**
 *
 * This class tests a GenLayer to ensure the same location always has the same encoding
 * @author Zeno410
 */
public class GenLayerConfirmEncodings extends GenLayerPack{

    private int exclusion;

    public static Logger logger = new Zeno410Logger("Cache").logger();

    private PlaneLocated<Integer> storedVals  = new PlaneLocated<Integer>();
    private final Decoder biomeEncoder;

    public GenLayerConfirmEncodings(GenLayer parent, int exclusion, Decoder biomeEncoder) {
        super(0L);
        this.parent = parent;
        this.exclusion = exclusion;
        this.biomeEncoder = biomeEncoder;
    }

    public GenLayerConfirmEncodings(GenLayer parent, Decoder biomeEncoder) {
        this(parent,0, biomeEncoder);
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
                    locked = biomeEncoder.decode(result[(z)*(xSize)+x]);
                    // and store for future reference
                    PlaneLocation location = new PlaneLocation(probe.x(),probe.z());
                    storedVals.put(location, locked);
                } else {
                    //logger.info("hit "+probe.toString()+locked.intValue());
                    // already stored
                    if (biomeEncoder.decode(result[(z)*(xSize)+x]) !=locked) {
                        problems += new PlaneLocation(x,z).toString();
                    };
                }
            }
        }
        if (problems.length()>originalLength){
            throw new RuntimeException(problems);
        }
        return result;
    }

}