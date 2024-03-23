
package climateControl.customGenLayer;

import climateControl.genLayerPack.GenLayerPack;
import com.Zeno410Utils.Compass;
import com.Zeno410Utils.Direction;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

/**
 * This class makes mountainChains from
 * @author Zeno410
 */
public class GenLayerMountainChains extends GenLayerPack {

    Compass compass = new Compass();

    public GenLayerMountainChains(long par1, GenLayer par3GenLayer){
        super(par1);
        super.parent = par3GenLayer;
        if (super.parent == null) throw new RuntimeException();
    }

    @Override
    public int[] getInts(int par1, int par2, int par3, int par4) {
        int i1 = par1 - 1;
        int j1 = par2 - 1;
        int k1 = par3 + 2;
        int l1 = par4 + 2;
        int[] parentVals = this.parent.getInts(i1, j1, k1, l1);
        int[] result = IntCache.getIntCache(par3 * par4);
        taste(parentVals,k1*l1);
        poison(result,par3*par4);
        taste(parentVals,k1*l1);

        for (int i2 = 0; i2 < par4; i2++)
        {
            for (int j2 = 0; j2 < par3; j2++)
            {
                int plate = parentVals[j2 + 1 + (i2 + 1) * k1];

                Direction direction = compass.direction(plate%8);

                int adjacent = parentVals[j2 + 1 + direction.zOffset + (i2 + 1+ direction.xOffset) * k1];

                if (plate == adjacent) {
                    // same plate; lowlands
                    result[j2 + i2 * par3] = 0;
                } else {
                    // next to different plate; mountains
                    result[j2 + i2 * par3] = 1;
                }

            }
        }
        taste(result,par3*par4);
        return result;
    }

}
