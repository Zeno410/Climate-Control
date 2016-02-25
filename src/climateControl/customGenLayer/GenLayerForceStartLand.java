
package climateControl.customGenLayer;

import climateControl.genLayerPack.GenLayerPack;
import climateControl.utils.IntRandomizer;
import climateControl.utils.RandomIntUser;
import net.minecraft.world.gen.layer.GenLayer;

/**
 *
 * @author Zeno410
 */
public class GenLayerForceStartLand extends GenLayerPack {

    public GenLayerForceStartLand(GenLayer parent) {
        super(0L);
        this.parent = parent;
    }



    @Override
    public int [] getInts(int x0, int z0, int xSize, int zSize) {
        //logger.info("location " + x0 + " " + z0 + " " + xSize + " " + zSize);
        int[] result = parent.getInts(x0, z0, xSize, zSize);
        for (int x=0; x<xSize-0;x++) {
            for (int z = 0; z<zSize;z++) {
                if (x+x0 ==0&& z+z0 == 0) {
                    //force land
                    result[(z)*(xSize)+x] = 1;
                }
            }
        }
        return result;
    }

}
