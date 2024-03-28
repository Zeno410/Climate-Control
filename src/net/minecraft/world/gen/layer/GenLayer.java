package net.minecraft.world.gen.layer;

import java.util.concurrent.Callable;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.init.Biomes;
import net.minecraft.util.ReportedException;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGeneratorSettings;

import net.minecraftforge.common.*;
import net.minecraftforge.event.terraingen.*;

public abstract class GenLayer
{
    /**
     * seed from World#getWorldSeed that is used in the LCG prng
     */
    private long worldGenSeed;
    /**
     * parent GenLayer that was provided via the constructor
     */
    protected GenLayer parent;
    /**
     * final part of the LCG prng that uses the chunk X, Z coords along with the other two seeds to generate
     * pseudorandom numbers
     */
    private long chunkSeed;
    /**
     * base seed to the LCG prng provided via the constructor
     */
    protected long baseSeed;
    private static final String __OBFID = "CL_00000559";

    /**
     * the first array item is a linked list of the bioms, the second is the zoom function, the third is the same as the
     * first.
     */
    public static GenLayer[] initializeAllBiomeGenerators(long par0, WorldType worldType)
    {
        boolean flag = false;

        GenLayerIsland genlayerisland = new GenLayerIsland(1L);
        GenLayerFuzzyZoom genlayerfuzzyzoom = new GenLayerFuzzyZoom(2000L, genlayerisland);
        GenLayerAddIsland genlayeraddisland = new GenLayerAddIsland(1L, genlayerfuzzyzoom);
        GenLayerZoom genlayerzoom = new GenLayerZoom(2001L, genlayeraddisland);
        genlayeraddisland = new GenLayerAddIsland(2L, genlayerzoom);
        genlayeraddisland = new GenLayerAddIsland(50L, genlayeraddisland);
        genlayeraddisland = new GenLayerAddIsland(70L, genlayeraddisland);
        GenLayerRemoveTooMuchOcean genlayerremovetoomuchocean = new GenLayerRemoveTooMuchOcean(2L, genlayeraddisland);
        genlayerzoom = new GenLayerZoom(2002L, genlayerremovetoomuchocean);
        genlayerzoom = new GenLayerZoom(2003L, genlayerzoom);
        GenLayerAddSnow genlayeraddsnow = new GenLayerAddSnow(2L, genlayerzoom);
        genlayeraddisland = new GenLayerAddIsland(3L, genlayeraddsnow);
        GenLayerEdge genlayeredge = new GenLayerEdge(2L, genlayeraddisland, GenLayerEdge.Mode.COOL_WARM);
        genlayeredge = new GenLayerEdge(2L, genlayeredge, GenLayerEdge.Mode.HEAT_ICE);
        genlayeredge = new GenLayerEdge(3L, genlayeredge, GenLayerEdge.Mode.SPECIAL);
        genlayeraddisland = new GenLayerAddIsland(4L, genlayeredge);
        GenLayerAddMushroomIsland genlayeraddmushroomisland = new GenLayerAddMushroomIsland(5L, genlayeraddisland);
        GenLayerDeepOcean genlayerdeepocean = new GenLayerDeepOcean(4L, genlayeraddmushroomisland);
        GenLayer genlayer3 = GenLayerZoom.magnify(1000L, genlayerdeepocean, 0);
             byte b0 = 4;

        if (worldType == WorldType.LARGE_BIOMES)
        {
            b0 = 6;
        }

        b0 = getModdedBiomeSize(worldType, b0);

        GenLayer genlayer = GenLayerZoom.magnify(1000L, genlayer3, 0);
        GenLayerRiverInit genlayerriverinit = new GenLayerRiverInit(100L, genlayer);
        Object object = worldType.getBiomeLayer(par0, genlayer3,ChunkGeneratorSettings.Factory.jsonToFactory("").build());

        GenLayer genlayer1 = GenLayerZoom.magnify(1000L, genlayerriverinit, 2);
        GenLayerHills genlayerhills = new GenLayerHills(1000L, (GenLayer)object, genlayer1);
        genlayer = GenLayerZoom.magnify(1000L, genlayerriverinit, 2);
        genlayer = GenLayerZoom.magnify(1000L, genlayer, b0);
        GenLayerRiver genlayerriver = new GenLayerRiver(1L, genlayer);
        GenLayerSmooth genlayersmooth = new GenLayerSmooth(1000L, genlayerriver);
        object = new GenLayerRareBiome(1001L, genlayerhills);

        for (int j = 0; j < b0; ++j)
        {
            object = new GenLayerZoom((long)(1000 + j), (GenLayer)object);

            if (j == 0)
            {
                object = new GenLayerAddIsland(3L, (GenLayer)object);
            }

            if (j == 1)
            {
                object = new GenLayerShore(1000L, (GenLayer)object);
            }
        }

        GenLayerSmooth genlayersmooth1 = new GenLayerSmooth(1000L, (GenLayer)object);
        GenLayerRiverMix genlayerrivermix = new GenLayerRiverMix(100L, genlayersmooth1, genlayersmooth);
        GenLayerVoronoiZoom genlayervoronoizoom = new GenLayerVoronoiZoom(10L, genlayerrivermix);
        return new GenLayer[] {genlayerrivermix, genlayervoronoizoom, genlayerrivermix};
    }

    public GenLayer(long par1)
    {
        this.baseSeed = par1;
        this.baseSeed *= this.baseSeed * 6364136223846793005L + 1442695040888963407L;
        this.baseSeed += par1;
        this.baseSeed *= this.baseSeed * 6364136223846793005L + 1442695040888963407L;
        this.baseSeed += par1;
        this.baseSeed *= this.baseSeed * 6364136223846793005L + 1442695040888963407L;
        this.baseSeed += par1;
    }

    /**
     * Initialize layer's local worldGenSeed based on its own baseSeed and the world's global seed (passed in as an
     * argument).
     */
    public void initWorldGenSeed(long par1)
    {
        this.worldGenSeed = par1;

        if (this.parent != null)
        {
            this.parent.initWorldGenSeed(par1);
        }

        this.worldGenSeed *= this.worldGenSeed * 6364136223846793005L + 1442695040888963407L;
        this.worldGenSeed += this.baseSeed;
        this.worldGenSeed *= this.worldGenSeed * 6364136223846793005L + 1442695040888963407L;
        this.worldGenSeed += this.baseSeed;
        this.worldGenSeed *= this.worldGenSeed * 6364136223846793005L + 1442695040888963407L;
        this.worldGenSeed += this.baseSeed;
    }

    /**
     * Initialize layer's current chunkSeed based on the local worldGenSeed and the (x,z) chunk coordinates.
     */
    public void initChunkSeed(long par1, long par3)
    {
        this.chunkSeed = this.worldGenSeed;
        this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
        this.chunkSeed += par1;
        this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
        this.chunkSeed += par3;
        this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
        this.chunkSeed += par1;
        this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
        this.chunkSeed += par3;
    }

    /**
     * returns a LCG pseudo random number from [0, x). Args: int x
     */
    protected int nextInt(int par1)
    {
        int j = (int)((this.chunkSeed >> 24) % (long)par1);

        if (j < 0)
        {
            j += par1;
        }

        this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
        this.chunkSeed += this.worldGenSeed;
        return j;
    }

    /**
     * Returns a list of integer values generated by this layer. These may be interpreted as temperatures, rainfall
     * amounts, or biomeList[] indices based on the particular GenLayer subclass.
     */
    public abstract int[] getInts(int var1, int var2, int var3, int var4);

    /**
     * returns true if the biomeIDs are equal, or returns the result of the comparison as per Biome.isEqualTo
     */
    protected static boolean compareBiomesById(final int p_151616_0_, final int p_151616_1_)
    {
        if (p_151616_0_ == p_151616_1_)
        {
            return true;
        }
        else if (p_151616_0_ !=Biome.getIdForBiome(Biomes.MESA_CLEAR_ROCK) && p_151616_0_ != Biome.getIdForBiome(Biomes.MESA_ROCK))
        {
            try
            {
                return Biome.getBiome(p_151616_0_) != null && Biome.getBiome(p_151616_1_) != null ? Biome.getBiome(p_151616_0_).equals(Biome.getBiome(p_151616_1_)) : false;
            }
            catch (Throwable throwable)
            {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Comparing biomes");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Biomes being compared");
                crashreportcategory.addCrashSection("Biome A ID", Integer.valueOf(p_151616_0_));
                crashreportcategory.addCrashSection("Biome B ID", Integer.valueOf(p_151616_1_));
                crashreportcategory.addCrashSection("Biome A", new Callable()
                {
                    private static final String __OBFID = "CL_00000560";
                    public String call()
                    {
                        return String.valueOf(Biome.getBiome(p_151616_0_));
                    }
                });
                crashreportcategory.addCrashSection("Biome B", new Callable()
                {
                    private static final String __OBFID = "CL_00000561";
                    public String call()
                    {
                        return String.valueOf(Biome.getBiome(p_151616_1_));
                    }
                });
                throw new ReportedException(crashreport);
            }
        }
        else
        {
            return p_151616_1_ == Biome.getIdForBiome(Biomes.MESA_ROCK) || p_151616_1_ == Biome.getIdForBiome(Biomes.MESA_CLEAR_ROCK);
        }
    }

    /**
     * returns true if the biomeId is one of the various ocean biomes.
     */
    protected static boolean isBiomeOceanic(int p_151618_0_)
    {
        return p_151618_0_ == Biome.getIdForBiome(Biomes.OCEAN) || p_151618_0_ == Biome.getIdForBiome(Biomes.DEEP_OCEAN) || p_151618_0_ == Biome.getIdForBiome(Biomes.FROZEN_OCEAN);
    }

    /**
     * selects a random integer from a set of provided integers
     */
    protected int selectRandom(int ... p_151619_1_)
    {
        return p_151619_1_[this.nextInt(p_151619_1_.length)];
    }

    /**
     * returns the most frequently occurring number of the set, or a random number from those provided
     */
    protected int selectModeOrRandom(int p_151617_1_, int p_151617_2_, int p_151617_3_, int p_151617_4_)
    {
        return p_151617_2_ == p_151617_3_ && p_151617_3_ == p_151617_4_ ? p_151617_2_ : (p_151617_1_ == p_151617_2_ && p_151617_1_ == p_151617_3_ ? p_151617_1_ : (p_151617_1_ == p_151617_2_ && p_151617_1_ == p_151617_4_ ? p_151617_1_ : (p_151617_1_ == p_151617_3_ && p_151617_1_ == p_151617_4_ ? p_151617_1_ : (p_151617_1_ == p_151617_2_ && p_151617_3_ != p_151617_4_ ? p_151617_1_ : (p_151617_1_ == p_151617_3_ && p_151617_2_ != p_151617_4_ ? p_151617_1_ : (p_151617_1_ == p_151617_4_ && p_151617_2_ != p_151617_3_ ? p_151617_1_ : (p_151617_2_ == p_151617_3_ && p_151617_1_ != p_151617_4_ ? p_151617_2_ : (p_151617_2_ == p_151617_4_ && p_151617_1_ != p_151617_3_ ? p_151617_2_ : (p_151617_3_ == p_151617_4_ && p_151617_1_ != p_151617_2_ ? p_151617_3_ : this.selectRandom(new int[] {p_151617_1_, p_151617_2_, p_151617_3_, p_151617_4_}))))))))));
    }

    public static byte getModdedBiomeSize(WorldType worldType, byte original)
    {
        WorldTypeEvent.BiomeSize event = new WorldTypeEvent.BiomeSize(worldType, original);
        MinecraftForge.TERRAIN_GEN_BUS.post(event);
        return (byte)event.getNewSize();
    }
}