
package climateControl.customGenLayer;
import climateControl.api.Climate;
import com.Zeno410Utils.Zeno410Logger;
import climateControl.genLayerPack.GenLayerPack;
import com.Zeno410Utils.PlaneLocated;
import com.Zeno410Utils.PlaneLocation;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import java.util.logging.Logger;

/**
 *
 * This class tests a GenLayer to ensure the same location always has the same value
 * @author Zeno410
 */
public class ConfirmClimate extends GenLayerPack{

    private int exclusion;

    public static Logger logger = new Zeno410Logger("Cache").logger();

    private PlaneLocated<Integer> storedVals  = new PlaneLocated<Integer>();

    public ConfirmClimate(GenLayer parent, int exclusion) {
        super(0L);
        this.parent = parent;
        this.exclusion = exclusion;
    }

    public ConfirmClimate(GenLayer parent) {
        this(parent,0);
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
                Integer locked = result[(z)*(xSize)+x];
                if (!Climate.legitimate(locked)) {
                    problems += new PlaneLocation(x,z).toString()+locked+" ";
                };
            }
        }
        if (problems.length()>originalLength){
            throw new RuntimeException(problems);
        }
        return result;
    }

}