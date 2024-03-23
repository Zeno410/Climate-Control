
package climateControl.customGenLayer;

import climateControl.genLayerPack.*;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

/**
 *
 * @author Zeno410
 */
public class GenLayerBreakMergers extends GenLayerPack {
    
    public GenLayerBreakMergers(long par1, GenLayer parent) {
        super(par1);
        this.parent = parent;
    }

    public int[] getInts(int par1, int par2, int par3, int par4){
        int i1 = par1 - 1;
        int j1 = par2 - 1;
        int k1 = par3 + 2;
        int l1 = par4 + 2;
        int[] aint = this.parent.getInts(i1, j1, k1, l1);
        int[] aint1 = IntCache.getIntCache(par3 * par4);

        for (int i2 = 0; i2 < par4; i2++){
            for (int j2 = 0; j2 < par3; j2++){
                int k3 = aint[j2 + 1 + (i2 + 1) * k1];
                if (isOceanic(k3)) {
                    aint1[j2 + i2 * par3]=k3;
                    continue;
                }
                int up = aint[j2 + 0 + (i2 + 1) * k1];
                int left = aint[j2 + 1 + (i2 + 0) * k1];
                int right = aint[j2 + 1 + (i2 + 2) * k1];
                int down = aint[j2 + 2 + (i2 + 1) * k1];
                int upleft = aint[j2 + 0 + (i2 + 0) * k1];
                int downleft = aint[j2 + 2 + (i2 + 0) * k1];
                int upright = aint[j2 + 0 + (i2 + 2) * k1];
                int downright = aint[j2 + 2 + (i2 + 2) * k1];
                if (tooDifferent(up,k3)) k3=0;
                if (tooDifferent(left,k3)) k3=0;
                if (tooDifferent(right,k3)) k3=0;
                if (tooDifferent(down,k3)) k3=0;
                if (tooDifferent(upleft,k3)) k3=0;
                if (tooDifferent(downleft,k3)) k3=0;
                if (tooDifferent(upright,k3)) k3=0;
                if (tooDifferent(downright,k3)) k3=0;
                aint1[j2 + i2 * par3]=k3;
            }
        }
        return aint1;


    }

    boolean tooDifferent(int compare, int result){
        if (!isOceanic(compare)&&!isOceanic(result)&&((compare<result-3)||(compare>result+3))) return true;
        return false;
    }
}
