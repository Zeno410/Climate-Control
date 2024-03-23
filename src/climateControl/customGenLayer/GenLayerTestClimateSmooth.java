
package climateControl.customGenLayer;

import climateControl.genLayerPack.GenLayerPack;
import com.Zeno410Utils.Zeno410Logger;
import java.util.logging.Logger;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

/**
 *
 * @author Zeno410
 */
public class GenLayerTestClimateSmooth extends GenLayerPack{
   public static Logger logger = new Zeno410Logger("GenLayerClimateSmooth").logger();

    public GenLayerTestClimateSmooth(GenLayer parent) {
        super(0L);
        this.parent = parent;
    }

    public int [] getInts(int x0, int z0, int xSize, int zSize) {
        //logger.info("location " + x0 + " " + z0 + " " + xSize + " " + zSize);
        int [] parentInts = parent.getInts(x0, z0, xSize, zSize);
        for (int x=0; x<xSize;x++) {
            for (int z = 0; z<zSize-1;z++) {
                int first = parentInts[x+(xSize)*(z)];
                int second = parentInts[x+(xSize)*(z+1)];

                if (isOceanic(first)) continue;
                if (isOceanic(second)) continue;
                if (Math.abs(first-second)>2) {
                    logger.info("first "+first + " to " +second);
                    for (int i=0; i<xSize;i++) {
                        String line = "";
                        for (int j = 0; j<zSize-1;j++) {
                            line = line + " " + parentInts[i+(xSize)*(j)];
                        }
                        logger.info(line);
                    }
                    throw new RuntimeException();
                }
            }
        }
        for (int x=0; x<xSize-1;x++) {
            for (int z = 0; z<zSize;z++) {
                int first =  (parentInts[x+(xSize)*(z)]);
                int second =  (parentInts[x+1+(xSize)*(z)]) ;


                if (isOceanic(first)) continue;
                if (isOceanic(second)) continue;
                if (Math.abs(first-second)>2) {
                    logger.info("first "+first + " to " +second);
                    throw new RuntimeException();
                }
            }
        }
        return parentInts;
    }
}
