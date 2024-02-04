
package climateControl.customGenLayer;

import climateControl.api.ClimateControlRules;
import climateControl.genLayerPack.GenLayerPack;
import climateControl.utils.IntPad;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenJungle;
import net.minecraft.world.biome.BiomeGenMesa;
import net.minecraft.world.gen.layer.GenLayer;

public class GenLayerPrettyShore extends GenLayerPack
{
    private static final String __OBFID = "CL_00000568";
    private float maxChasm;
    private ClimateControlRules rules;
    private boolean mesaMesaBorders;
    private IntPad output = new IntPad();

    public GenLayerPrettyShore(long par1, GenLayer par3GenLayer, float maxChasm, ClimateControlRules rules,boolean mesaMesaBorders)
    {
        super(par1);
        this.parent = par3GenLayer;
        this.maxChasm = maxChasm;
        this.rules = rules;
        this.mesaMesaBorders = mesaMesaBorders;
    }

    /**
     * Returns a list of integer values generated by this layer. These may be interpreted as temperatures, rainfall
     * amounts, or biomeList[] indices based on the particular GenLayer subclass.
     */

    private boolean waterBiome(int biomeID) {
        if (isOceanic(biomeID)) return true;
        return rules.noBeachesAllowed(biomeID);
    }
    public int[] getInts(int par1, int par2, int par3, int par4)
    {
        int[] aint = this.parent.getInts(par1 - 1, par2 - 1, par3 + 2, par4 + 2);
        int[] aint1 = output.pad(par3 * par4);

        for (int i1 = 0; i1 < par4; ++i1)
        {
            for (int j1 = 0; j1 < par3; ++j1)
            {
                this.initChunkSeed((long)(j1 + par1), (long)(i1 + par2));
                int k1 = aint[j1 + 1 + (i1 + 1) * (par3 + 2)];
                BiomeGenBase biomegenbase = BiomeGenBase.getBiome(k1);
                int l1;
                int i2;
                int j2;
                int k2;

                if (k1 == BiomeGenBase.mushroomIsland.biomeID)
                {
                    l1 = aint[j1 + 1 + (i1 + 1 - 1) * (par3 + 2)];
                    i2 = aint[j1 + 1 + 1 + (i1 + 1) * (par3 + 2)];
                    j2 = aint[j1 + 1 - 1 + (i1 + 1) * (par3 + 2)];
                    k2 = aint[j1 + 1 + (i1 + 1 + 1) * (par3 + 2)];

                    if (l1 != BiomeGenBase.ocean.biomeID && i2 != BiomeGenBase.ocean.biomeID && j2 != BiomeGenBase.ocean.biomeID && k2 != BiomeGenBase.ocean.biomeID)
                    {
                        aint1[j1 + i1 * par3] = k1;
                    }
                    else
                    {
                        aint1[j1 + i1 * par3] = BiomeGenBase.mushroomIslandShore.biomeID;
                    }
                }
                else if (biomegenbase != null && biomegenbase.getBiomeClass() == BiomeGenJungle.class)
                {
                    l1 = aint[j1 + 1 + (i1 + 1 - 1) * (par3 + 2)];
                    i2 = aint[j1 + 1 + 1 + (i1 + 1) * (par3 + 2)];
                    j2 = aint[j1 + 1 - 1 + (i1 + 1) * (par3 + 2)];
                    k2 = aint[j1 + 1 + (i1 + 1 + 1) * (par3 + 2)];

                    if (this.func_151631_c(l1) && this.func_151631_c(i2) && this.func_151631_c(j2) && this.func_151631_c(k2))
                    {
                        if (!waterBiome(l1) && !waterBiome(i2) && !waterBiome(j2) && !waterBiome(k2))
                        {
                            aint1[j1 + i1 * par3] = k1;
                        }
                        else
                        {
                            aint1[j1 + i1 * par3] = BiomeGenBase.beach.biomeID;
                        }
                    }
                    else
                    {
                        aint1[j1 + i1 * par3] = BiomeGenBase.jungleEdge.biomeID;
                    }
                }
                else if (k1 != BiomeGenBase.extremeHills.biomeID && k1 != BiomeGenBase.extremeHillsPlus.biomeID && k1 != BiomeGenBase.extremeHillsEdge.biomeID)
                {
                    if (biomegenbase != null && biomegenbase.func_150559_j()&&!biomegenbase.biomeName.equalsIgnoreCase("Dead Forest"))
                    {
                        this.func_151632_a(aint, aint1, j1, i1, par3, k1, BiomeGenBase.coldBeach.biomeID);
                    }
                    else if (k1 != BiomeGenBase.mesa.biomeID && k1 != BiomeGenBase.mesaPlateau_F.biomeID)
                    {
                        if (!waterBiome(k1))
                        {
                            l1 = aint[j1 + 1 + (i1 + 1 - 1) * (par3 + 2)];
                            i2 = aint[j1 + 1 + 1 + (i1 + 1) * (par3 + 2)];
                            j2 = aint[j1 + 1 - 1 + (i1 + 1) * (par3 + 2)];
                            k2 = aint[j1 + 1 + (i1 + 1 + 1) * (par3 + 2)];

                            if (!waterBiome(l1) && !waterBiome(i2) && !waterBiome(j2) && !waterBiome(k2))
                            {
                                aint1[j1 + i1 * par3] = k1;
                            }
                            else
                            {
                                float height = biomegenbase.rootHeight + biomegenbase.heightVariation*2;
                                if ((height>maxChasm+0.5)&&(rules.stoneBeachAllowed(biomegenbase.biomeID))) {
                                        aint1[j1 + i1 * par3] = BiomeGenBase.stoneBeach.biomeID;

                                } else {
                                    if (biomegenbase.getTempCategory() == BiomeGenBase.TempCategory.COLD){
                                        aint1[j1 + i1 * par3] = BiomeGenBase.coldBeach.biomeID;
                                    } else if (aint[j1 + i1 * par3] != BiomeGenBase.mushroomIsland.biomeID
                                            && aint[j1 + i1 * par3] != BiomeGenBase.mushroomIslandShore.biomeID){
                                                    aint1[j1 + i1 * par3] = BiomeGenBase.beach.biomeID;
                                    } else {
                                        aint1[j1 + i1 * par3] = BiomeGenBase.mushroomIslandShore.biomeID;
                                    }
                                }
                            }
                        }
                        else
                        {
                            aint1[j1 + i1 * par3] = k1;
                        }
                    }
                    else
                    {
                        l1 = aint[j1 + 1 + (i1 + 1 - 1) * (par3 + 2)];
                        i2 = aint[j1 + 1 + 1 + (i1 + 1) * (par3 + 2)];
                        j2 = aint[j1 + 1 - 1 + (i1 + 1) * (par3 + 2)];
                        k2 = aint[j1 + 1 + (i1 + 1 + 1) * (par3 + 2)];

                        if (!waterBiome(l1) && !waterBiome(i2) && !waterBiome(j2) && !waterBiome(k2))
                        {
                            if (this.func_151633_d(l1) && this.func_151633_d(i2) && this.func_151633_d(j2) && this.func_151633_d(k2))
                            {
                                aint1[j1 + i1 * par3] = k1;
                            }
                            else
                            {
                                if (this.mesaMesaBorders) {
                                    aint1[j1 + i1 * par3] = BiomeGenBase.mesa.biomeID;
                                } else {
                                    aint1[j1 + i1 * par3] = BiomeGenBase.desert.biomeID;
                                }
                            }
                        }
                        else
                        {
                            aint1[j1 + i1 * par3] = k1;
                        }
                    }
                }
                else
                {
                    this.func_151632_a(aint, aint1, j1, i1, par3, k1, BiomeGenBase.stoneBeach.biomeID);
                }
            }
        }

        return aint1;
    }

    private void func_151632_a(int[] p_151632_1_, int[] p_151632_2_, int p_151632_3_, int p_151632_4_, int p_151632_5_, int p_151632_6_, int p_151632_7_)
    {
        if (waterBiome(p_151632_6_))
        {
            p_151632_2_[p_151632_3_ + p_151632_4_ * p_151632_5_] = p_151632_6_;
        }
        else
        {
            int j1 = p_151632_1_[p_151632_3_ + 1 + (p_151632_4_ + 1 - 1) * (p_151632_5_ + 2)];
            int k1 = p_151632_1_[p_151632_3_ + 1 + 1 + (p_151632_4_ + 1) * (p_151632_5_ + 2)];
            int l1 = p_151632_1_[p_151632_3_ + 1 - 1 + (p_151632_4_ + 1) * (p_151632_5_ + 2)];
            int i2 = p_151632_1_[p_151632_3_ + 1 + (p_151632_4_ + 1 + 1) * (p_151632_5_ + 2)];

            if (!waterBiome(j1) && !waterBiome(k1) && !waterBiome(l1) && !waterBiome(i2))
            {
                p_151632_2_[p_151632_3_ + p_151632_4_ * p_151632_5_] = p_151632_6_;
            }
            else
            {
                p_151632_2_[p_151632_3_ + p_151632_4_ * p_151632_5_] = p_151632_7_;
            }
        }
    }

    private boolean func_151631_c(int p_151631_1_)
    {
        return BiomeGenBase.getBiome(p_151631_1_) != null && BiomeGenBase.getBiome(p_151631_1_).getBiomeClass() == BiomeGenJungle.class ? true : p_151631_1_ == BiomeGenBase.jungleEdge.biomeID || p_151631_1_ == BiomeGenBase.jungle.biomeID || p_151631_1_ == BiomeGenBase.jungleHills.biomeID || p_151631_1_ == BiomeGenBase.forest.biomeID || p_151631_1_ == BiomeGenBase.taiga.biomeID || isOceanic(p_151631_1_);
    }

    private boolean func_151633_d(int p_151633_1_)
    {
        return BiomeGenBase.getBiome(p_151633_1_) != null && BiomeGenBase.getBiome(p_151633_1_) instanceof BiomeGenMesa;
    }
}