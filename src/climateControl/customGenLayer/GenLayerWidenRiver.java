
package climateControl.customGenLayer;

/**
 *
 * This widens rivers
 * @author Zeno410
 */

import climateControl.genLayerPack.GenLayerPack;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerRiver;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerWidenRiver extends GenLayerPack
{
    private static final String __OBFID = "CL_00000566";

    public GenLayerWidenRiver(long par1, GenLayer par3GenLayer) {
        super(par1);
        super.parent = par3GenLayer;
    }

    /**
     * Returns a list of integer values generated by this layer. These may be interpreted as temperatures, rainfall
     * amounts, or biomeList[] indices based on the particular GenLayer subclass.
     */
    public int[] getInts(int par1, int par2, int par3, int par4)
    {
        int i1 = par1 - 1;
        int j1 = par2 - 1;
        int k1 = par3 + 2;
        int l1 = par4 + 2;
        int[] aint = this.parent.getInts(i1, j1, k1, l1);
        int[] aint1 = IntCache.getIntCache(par3 * par4);

        for (int i2 = 0; i2 < par4; ++i2) {
            for (int j2 = 0; j2 < par3; ++j2){
                aint1[j2 + i2 * par3] = 0;
            }
        }

        for (int i2 = 0; i2 < par4; ++i2)
        {
            for (int j2 = 0; j2 < par3; ++j2)
            {
                // set to parent
                aint1[j2 + i2 * par3] = aint[j2 + 1 + (i2 + 1) * k1];
                // if anything horizontal is river, make it river
                if  (aint[j2 + 0 + (i2 + 1) * k1]==BiomeGenBase.river.biomeID) {
                    aint1[j2 + i2 * par3] = BiomeGenBase.river.biomeID;
                }
                if  (aint[j2 + 2 + (i2 + 1) * k1]==BiomeGenBase.river.biomeID) {
                    aint1[j2 + i2 * par3] = BiomeGenBase.river.biomeID;
                }
                if  (aint[j2 + 1 + (i2 + 0) * k1]==BiomeGenBase.river.biomeID) {
                    aint1[j2 + i2 * par3] = BiomeGenBase.river.biomeID;
                }
                if  (aint[j2 + 1 + (i2 + 2) * k1]==BiomeGenBase.river.biomeID) {
                    aint1[j2 + i2 * par3] = BiomeGenBase.river.biomeID;
                }

                if  (aint[j2 + 0 + (i2 + 1) * k1]==BiomeGenBase.frozenRiver.biomeID) {
                    aint1[j2 + i2 * par3] = BiomeGenBase.frozenRiver.biomeID;
                }
                if  (aint[j2 + 2 + (i2 + 1) * k1]==BiomeGenBase.frozenRiver.biomeID) {
                    aint1[j2 + i2 * par3] = BiomeGenBase.frozenRiver.biomeID;
                }
                if  (aint[j2 + 1 + (i2 + 0) * k1]==BiomeGenBase.frozenRiver.biomeID) {
                    aint1[j2 + i2 * par3] = BiomeGenBase.frozenRiver.biomeID;
                }
                if  (aint[j2 + 1 + (i2 + 2) * k1]==BiomeGenBase.frozenRiver.biomeID) {
                    aint1[j2 + i2 * par3] = BiomeGenBase.frozenRiver.biomeID;
                }
            }
        }

        return aint1;
    }

    private int func_151630_c(int p_151630_1_)
    {
        return p_151630_1_ >= 2 ? 2 + (p_151630_1_ & 1) : p_151630_1_;
    }
}