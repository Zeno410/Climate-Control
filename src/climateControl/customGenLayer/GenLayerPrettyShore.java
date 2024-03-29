
package climateControl.customGenLayer;

import climateControl.api.ClimateControlRules;
import climateControl.genLayerPack.GenLayerPack;
import com.Zeno410Utils.AccessFloat;
import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.TempCategory;
import net.minecraft.world.biome.BiomeJungle;
import net.minecraft.world.biome.BiomeMesa;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
/*
 * This routine puts borders between biomes. Primarily that's appropriate beaches at the water's edge
 * but it also includes desert borders on mesa and Jungle Edge for jungles
 * */
public class GenLayerPrettyShore extends GenLayerPack
{
    private static final String __OBFID = "CL_00000568";
    private float maxChasm;
    private ClimateControlRules rules;
    private boolean mesaMesaBorders;
    private AccessFloat<Biome> baseHeight = new AccessFloat<Biome>("field_76748_D");
    private AccessFloat<Biome> heightVariation = new AccessFloat<Biome>("field_76749_E");

    public GenLayerPrettyShore(long par1, GenLayer par3GenLayer, float maxChasm, ClimateControlRules rules,boolean mesaMesaBorders)
    {
        super(par1);
        this.parent = par3GenLayer;
        this.maxChasm = maxChasm;
        this.rules = rules;
        this.mesaMesaBorders = mesaMesaBorders;
    }

    private boolean waterBiome(int biomeID) {
        if (isOceanic(biomeID)) return true;
        return rules.noBeachesAllowed(biomeID);
    }

    public int[] getInts(int par1, int par2, int par3, int par4)
    {
        int[] aint = this.parent.getInts(par1 - 1, par2 - 1, par3 + 2, par4 + 2);
        int[] aint1 = IntCache.getIntCache(par3 * par4);

        for (int i1 = 0; i1 < par4; ++i1)
        {
            for (int j1 = 0; j1 < par3; ++j1)
            {
                this.initChunkSeed((long)(j1 + par1), (long)(i1 + par2));
                int k1 = aint[j1 + 1 + (i1 + 1) * (par3 + 2)];
                Biome biome = Biome.getBiome(k1);
                int l1;
                int i2;
                int j2;
                int k2;

                if (k1 == Biome.getIdForBiome(Biomes.MUSHROOM_ISLAND))
                {
                    l1 = aint[j1 + 1 + (i1 + 1 - 1) * (par3 + 2)];
                    i2 = aint[j1 + 1 + 1 + (i1 + 1) * (par3 + 2)];
                    j2 = aint[j1 + 1 - 1 + (i1 + 1) * (par3 + 2)];
                    k2 = aint[j1 + 1 + (i1 + 1 + 1) * (par3 + 2)];

                    if (l1 != Biome.getIdForBiome(Biomes.OCEAN) && i2 != Biome.getIdForBiome(Biomes.OCEAN) && j2 != Biome.getIdForBiome(Biomes.OCEAN) && k2 != Biome.getIdForBiome(Biomes.OCEAN))
                    {
                        aint1[j1 + i1 * par3] = k1;
                    }
                    else
                    {
                        aint1[j1 + i1 * par3] = Biome.getIdForBiome(Biomes.MUSHROOM_ISLAND_SHORE);
                    }
                }
                else if (biome != null && biome.getBiomeClass() == BiomeJungle.class)
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
                            aint1[j1 + i1 * par3] = Biome.getIdForBiome(Biomes.BEACH);
                        }
                    }
                    else
                    {
                        aint1[j1 + i1 * par3] = Biome.getIdForBiome(Biomes.JUNGLE_EDGE);
                    }
                }
                else if (k1 != Biome.getIdForBiome(Biomes.EXTREME_HILLS) && k1 != Biome.getIdForBiome(Biomes.EXTREME_HILLS_WITH_TREES) && k1 != Biome.getIdForBiome(Biomes.EXTREME_HILLS_EDGE))
                {
                    if (biome != null && biome.getEnableSnow())
                    {
                        this.func_151632_a(aint, aint1, j1, i1, par3, k1, biome.getIdForBiome(Biomes.COLD_BEACH));
                    }
                    else if (k1 != Biome.getIdForBiome(Biomes.MESA_CLEAR_ROCK) && k1 != Biome.getIdForBiome(Biomes.MESA_ROCK))
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
                            {//
                                if (biome == null) throw new RuntimeException("no biome found for biome #"+k1);
                                float height = this.baseHeight.get(biome) + this.heightVariation.get(biome);
                                if ((height>maxChasm+0.5)&&(rules.stoneBeachAllowed(k1))) {
                                        aint1[j1 + i1 * par3] = Biome.getIdForBiome(Biomes.STONE_BEACH);

                                } else {
                                    if (biome.getTempCategory() == Biome.TempCategory.COLD){
                                        aint1[j1 + i1 * par3] = Biome.getIdForBiome(Biomes.COLD_BEACH);
                                    } else if (aint[j1 + i1 * par3] != Biome.getIdForBiome(Biomes.MUSHROOM_ISLAND)
                                            && aint[j1 + i1 * par3] != Biome.getIdForBiome(Biomes.MUSHROOM_ISLAND_SHORE)){
                                                    aint1[j1 + i1 * par3] = Biome.getIdForBiome(Biomes.BEACH);
                                    } else {
                                        aint1[j1 + i1 * par3] = Biome.getIdForBiome(Biomes.MUSHROOM_ISLAND_SHORE);
                                    }
                                }
                            }
                        }
                        else
                        {
                            aint1[j1 + i1 * par3] = k1;
                        }
                    }
                    else // Mesa biomes
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
                                    aint1[j1 + i1 * par3] = Biome.getIdForBiome(Biomes.MESA);
                                } else {
                                    aint1[j1 + i1 * par3] = Biome.getIdForBiome(Biomes.DESERT);
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
                    this.func_151632_a(aint, aint1, j1, i1, par3, k1, Biome.getIdForBiome(Biomes.STONE_BEACH));
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
        return Biome.getBiome(p_151631_1_) != null && Biome.getBiome(p_151631_1_).getBiomeClass() == BiomeJungle.class ? true : p_151631_1_ == Biome.getIdForBiome(Biomes.JUNGLE_EDGE)
                || p_151631_1_ == Biome.getIdForBiome(Biomes.JUNGLE) || p_151631_1_ == Biome.getIdForBiome(Biomes.JUNGLE_HILLS)
                || p_151631_1_ == Biome.getIdForBiome(Biomes.FOREST) || p_151631_1_ == Biome.getIdForBiome(Biomes.TAIGA) || isOceanic(p_151631_1_);
    }

    private boolean func_151633_d(int p_151633_1_)
    {
        return Biome.getBiome(p_151633_1_) != null && Biome.getBiome(p_151633_1_) instanceof BiomeMesa;
    }
}