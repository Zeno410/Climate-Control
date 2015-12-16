package climateControl.generator;
import climateControl.BiomeRandomizer;
import climateControl.ClimateChooser;
import climateControl.api.ClimateControlRules;
import climateControl.api.ClimateControlSettings;
import climateControl.api.BiomeSettings;
import climateControl.customGenLayer.DecodingSubBiome;
import climateControl.customGenLayer.HashEncodedAddLand;
import climateControl.customGenLayer.HashEncodedBiomeByClimate;
import climateControl.customGenLayer.HashEncodedSmoothWithClimates;
import climateControl.customGenLayer.EncodedZoom;
import climateControl.customGenLayer.GenLayerAddBiome;
import climateControl.customGenLayer.GenLayerDederpedHills;
import climateControl.customGenLayer.GenLayerOceanicIslands;
import climateControl.utils.RandomIntUser;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerAddIsland;
import net.minecraft.world.gen.layer.GenLayerFuzzyZoom;
import net.minecraft.world.gen.layer.GenLayerRareBiome;
import net.minecraft.world.gen.layer.GenLayerRiver;
import net.minecraft.world.gen.layer.GenLayerRiverInit;
import net.minecraft.world.gen.layer.GenLayerRiverMix;
import net.minecraft.world.gen.layer.GenLayerShore;
import climateControl.genLayerPack.GenLayerSmooth;
import net.minecraft.world.gen.layer.GenLayerVoronoiZoom;
import net.minecraft.world.gen.layer.GenLayerZoom;
import climateControl.customGenLayer.GenLayerAddLand;
import climateControl.customGenLayer.GenLayerAdjustIsland;
import climateControl.customGenLayer.GenLayerBiomeByClimate;
import climateControl.customGenLayer.GenLayerCache;
import climateControl.customGenLayer.GenLayerSmoothWithClimates;
import climateControl.customGenLayer.GenLayerConstant;
import climateControl.customGenLayer.GenLayerContinentalShelf;
import climateControl.customGenLayer.GenLayerDefineClimate;
import climateControl.customGenLayer.GenLayerLessRiver;
import climateControl.customGenLayer.GenLayerLowlandRiverMix;
import climateControl.customGenLayer.GenLayerNoPlains;
import climateControl.customGenLayer.GenLayerOceanicMushroomIsland;
import climateControl.customGenLayer.GenLayerRandomBiomes;

import climateControl.customGenLayer.GenLayerRiverMixWrapper;
import climateControl.customGenLayer.GenLayerSmoothWithBiomes;
import climateControl.customGenLayer.GenLayerSubBiome;

import climateControl.customGenLayer.GenLayerWidenRiver;
import climateControl.customGenLayer.GenLayerZoomBiome;
import climateControl.utils.Acceptor;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import net.minecraft.world.WorldType;
/**
 * This class creates a world generator from a ClimateControlSettings and a world seed
 * @author Zeno410
 */
public class StackedContinentsGenerator extends AbstractWorldGenerator {
    protected final SubBiomeChooser subBiomeChooser = new SubBiomeChooser();
    protected final BiomeSwapper mBiomeChooser = new BiomeSwapper();

    public StackedContinentsGenerator(ClimateControlSettings settings) {
        super(settings);
    }

    public GenLayerRiverMix fromSeed(long worldSeed) {

        this.subBiomeChooser.clear();
        this.subBiomeChooser.set(settings().biomeSettings());
        setOceanSubBiomes();
        this.mBiomeChooser.set(settings().biomeSettings());
        setRules();
        boolean climatesAssigned = false;

        GenLayer emptyOcean = new GenLayerConstant(0);
        GenLayer genlayerisland = new GenLayerOceanicIslands(1L,emptyOcean,
                settings().largeContinentFrequency.value(),settings().separateLandmasses.value());
        GenLayerFuzzyZoom genlayerfuzzyzoom = new GenLayerFuzzyZoom(2000L, genlayerisland);
        GenLayer genlayeraddisland = new GenLayerAddLand(1L, genlayerfuzzyzoom);
        GenLayer smallContinents = new GenLayerOceanicIslands(2L, genlayeraddisland,
                settings().mediumContinentFrequency.value(),settings().separateLandmasses.value());
        try {
            DataOutputStream recording = new DataOutputStream(new FileOutputStream(new File("/StackedRecordedDim.txt")));
            smallContinents = new GenLayerCache(smallContinents,recording);
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        }
        genlayeraddisland = new GenLayerAddLand(1L, smallContinents);
        GenLayerZoom genlayerzoom = new GenLayerFuzzyZoom(2001L, genlayeraddisland);
        genlayeraddisland = new GenLayerAddLand(2L, smallContinents);
        smallContinents = new GenLayerOceanicIslands(2L, genlayerzoom,
                settings().smallContinentFrequency.value(),settings().separateLandmasses.value());
        genlayeraddisland = new GenLayerAddLand(2L, smallContinents);
        if (settings().doFull()) {
            genlayeraddisland = new GenLayerDefineClimate(2L, genlayeraddisland,2,1,1,2);
            climatesAssigned = true;
        }
        genlayeraddisland = new GenLayerAddLand(50L, genlayeraddisland);
        genlayeraddisland = new GenLayerSmooth(1001L, genlayeraddisland);
        genlayeraddisland = new GenLayerAdjustIsland(70L,genlayeraddisland ,1,5,6);
        genlayeraddisland = new GenLayerZoom(2002L, genlayeraddisland);
        //genlayeraddisland = new GenLayerAddIsland(90L, new GenLayerSmooth(1002L,genlayeraddisland));
        if (climatesAssigned) {
            // climates are already defined so the island creator has to use a climate definer;
            genlayeraddisland = new GenLayerOceanicIslands(
                    2L, genlayeraddisland,settings().largeIslandFrequency.value(),this.islandClimates()
                    ,settings().separateLandmasses.value());
        } else {
            genlayeraddisland = new GenLayerOceanicIslands(2L, genlayeraddisland,
                    settings().largeIslandFrequency.value(),settings().separateLandmasses.value());
        }
        genlayeraddisland = new GenLayerAddLand(3L, genlayeraddisland);
        if (settings().doHalf()) {
            genlayeraddisland = new GenLayerDefineClimate(2L, genlayeraddisland,settings());
            climatesAssigned = true;
        }
        genlayeraddisland = new GenLayerAdjustIsland(4L, genlayeraddisland,1,5,6);
        genlayeraddisland = new GenLayerSmoothWithClimates(4L, genlayeraddisland);
        genlayeraddisland = new GenLayerZoom(2003L, genlayeraddisland);
        if (climatesAssigned) {
            // climates are already defined so the island creator has to use a climate definer;
            genlayeraddisland = new GenLayerOceanicIslands(
                    3L, genlayeraddisland,settings().mediumIslandFrequency.value(),this.islandClimates()
                    ,settings().separateLandmasses.value());
        } else {
            genlayeraddisland = new GenLayerOceanicIslands(2L, genlayeraddisland,
                    settings().mediumIslandFrequency.value(),settings().separateLandmasses.value());
        }

        genlayeraddisland = new GenLayerAddLand(3L, genlayeraddisland);
        if (settings().quarterSize.value()) {
            genlayeraddisland = new GenLayerDefineClimate(2L, genlayeraddisland,settings());
            climatesAssigned = true;
        }
        genlayeraddisland = new GenLayerAdjustIsland(4L, genlayeraddisland,1,5,6);
        genlayeraddisland = new GenLayerSmoothWithClimates(4L, genlayeraddisland);
        genlayeraddisland = this.smoothClimates(settings(), worldSeed, genlayeraddisland);
        //genlayeraddisland = new GenLayerTestClimateSmooth(genlayeraddisland);
        GenLayer genlayerdeepocean = new GenLayerContinentalShelf(4L, genlayeraddisland);
        GenLayer genlayeraddmushroomisland = new GenLayerOceanicMushroomIsland(5L, genlayerdeepocean, settings().mushroomIslandIncidence.value());
        GenLayer genlayer3 = GenLayerZoom.magnify(1000L, new GenLayerSmoothWithClimates(3000L,genlayeraddmushroomisland), 0);
        genlayer3.initWorldGenSeed(worldSeed);
        if (settings().smootherCoasts.value()) {
            return climateControlExpansion(worldSeed,WorldType.DEFAULT,genlayer3,settings());
        }
        return this.vanillaExpansion(worldSeed,WorldType.DEFAULT,genlayer3,settings());
    }

    protected void setOceanSubBiomes() {

    }

    protected RandomIntUser islandClimates() {
        return new ClimateChooser(settings().hotIncidence.value(),
                settings().warmIncidence.value(),
                settings().coolIncidence.value(),
                settings().snowyIncidence.value());
    }

    public GenLayerRiverMix climateControlExpansion(long par0, WorldType par2WorldType,
            GenLayer genlayer3,ClimateControlSettings settings){
        byte b0 = 4;

        if (par2WorldType == WorldType.LARGE_BIOMES)
        {
            b0 = 6;
        } else b0 = settings.biomeSize.value().byteValue();

        GenLayer genlayer = GenLayerZoom.magnify(1000L, genlayer3, 0);
        GenLayer genlayerriverinit = new GenLayerLessRiver(100L, genlayer,settings().percentageRiverReduction.value());
        GenLayer biomes = null;
        if (settings.randomBiomes.value()) {
            biomes = new GenLayerRandomBiomes(par0,genlayer3,settings());
        } else {
            biomes = new GenLayerBiomeByClimate(par0,genlayer3,settings());
        }//par2WorldType.getBiomeLayer(par0, genlayer3);
        GenLayer object = new GenLayerZoomBiome(1000L, biomes);
        object = new GenLayerAddBiome(1000L, object);
        object  = new GenLayerSmoothWithBiomes(100L,object);
        object = new GenLayerZoom(1001L, object);
        object = new GenLayerAddBiome(1000L, object);
        object  = new GenLayerSmoothWithBiomes(101L,object);
        //object = biomes; //this is to shrink maps for viewing in AMIDST
        GenLayer genlayer1 = GenLayerZoom.magnify(1000L, genlayerriverinit, 2);
        GenLayer genlayerhills = null;
        if (false) {//if (settings.climateControlcompatibility.value()) {
            genlayerhills = new GenLayerDederpedHills(1000L, object, genlayer1);
        } else {
            genlayerhills = new GenLayerSubBiome(1000L, object, genlayer1,subBiomeChooser,mBiomeChooser,
                settings().doBoPSubBiomes());
        }
        genlayer = GenLayerZoom.magnify(1000L, genlayerriverinit, 2);
        genlayer = GenLayerZoom.magnify(1000L, genlayer, b0);
        GenLayer genlayerriver = new GenLayerRiver(1L, genlayer);
        if (settings.widerRivers.value()) {
            genlayerriver = new GenLayerWidenRiver(1L, genlayerriver);
        }
        GenLayerSmooth genlayersmooth = new GenLayerSmooth(1000L, genlayerriver);
        object = new GenLayerRareBiome(1001L, genlayerhills);

        for (int j = 0; j < b0; ++j)
        {
            object = new GenLayerZoom((long)(1000 + j), (GenLayer)object);

            if (j == 1)
            {
                object = new GenLayerAddIsland(3L, (GenLayer)object);
                //object  = new GenLayerSmoothWithClimates(100L,object);
            }

            if (j == 0)
            {
                try {
                    object = new HighlandsShoreGenGetter().shoreGen(object);
                } catch (java.lang.NoClassDefFoundError e) {
                    object = new GenLayerShore(1000L, object);
                }
            }
        }

        GenLayerSmooth genlayersmooth1 = new GenLayerSmooth(1000L, (GenLayer)object);
        //GenLayerRiverMix genlayerrivermix = new GenLayerLowlandRiverMix(100L, genlayersmooth1, genlayersmooth,
                //(settings().maxRiverChasm.value().floatValue()),rules);
        GenLayerRiverMix genlayerrivermix = new GenLayerRiverMix(100L, genlayersmooth1, genlayersmooth);
        GenLayerVoronoiZoom genlayervoronoizoom = new GenLayerVoronoiZoom(10L, genlayerrivermix);
        genlayerrivermix.initWorldGenSeed(par0);
        genlayervoronoizoom.initWorldGenSeed(par0);
        return genlayerrivermix;
    }

    public GenLayerRiverMix encodedExpansion(long par0, WorldType par2WorldType,
            GenLayer genlayer3,ClimateControlSettings settings){
        byte b0 = 4;

        if (par2WorldType == WorldType.LARGE_BIOMES)
        {
            b0 = 6;
        } else b0 = settings.biomeSize.value().byteValue();

        GenLayer genlayer = GenLayerZoom.magnify(1000L, genlayer3, 0);
        //GenLayerRiverInit genlayerriverinit = new GenLayerRiverInit(100L, genlayer);
        GenLayer biomes = null;
        IndirectDecoder biomeCodes = new IndirectDecoder();
        IndirectDecoder climateCodes = new IndirectDecoder();
        if (settings.randomBiomes.value()) {
            throw new RuntimeException();
            //biomes = new GenLayerRandomBiomes(par0,genlayer3);
        } else {
            biomes = new HashEncodedBiomeByClimate(par0,genlayer3,settings,biomeCodes,climateCodes);
        }//par2WorldType.getBiomeLayer(par0, genlayer3);
        GenLayer object = new EncodedZoom(1000L, biomes,biomeCodes);
        if (1>0) throw new RuntimeException();
        object = new HashEncodedAddLand(1000L, object,biomeCodes);
        object  = new HashEncodedSmoothWithClimates(100L,object,biomeCodes);
        object = new EncodedZoom(1001L, object,biomeCodes);
        object = new HashEncodedAddLand(1000L, object,biomeCodes);
        object  = new HashEncodedSmoothWithClimates(101L,object,biomeCodes);
        //object = biomes; //this is to shrink maps for viewing in AMIDST
        GenLayer genlayer1 = object;// multipurpose
        GenLayer genlayerhills = null;
        if (false) {//if (settings.climateControlcompatibility.value()) {
            genlayerhills = new GenLayerDederpedHills(1000L, object, genlayer1);
        } else {
            genlayerhills = new DecodingSubBiome(1000L, object,subBiomeChooser,mBiomeChooser,
                    biomeCodes,climateCodes,new BiomeRandomizer(settings().biomeSettings()).pickByClimate());
        }
        //genlayer = GenLayerZoom.magnify(1000L, genlayerriverinit, 2);
        genlayer = object;// the encoding is now used for the river demarcations
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
                //object  = new GenLayerSmoothWithClimates(100L,object);
            }

            if (j == 1)
            {

                try {
                    object = new HighlandsShoreGenGetter().shoreGen(object);
                } catch (java.lang.NoClassDefFoundError e) {
                    object = new GenLayerShore(1000L, object);
                }

                
            }
        }

        GenLayerSmooth genlayersmooth1 = new GenLayerSmooth(1000L, (GenLayer)object);
        GenLayerRiverMix genlayerrivermix = new GenLayerRiverMix(100L, genlayersmooth1, genlayersmooth);
        GenLayerRiverMixWrapper.logger.info(genlayerrivermix.toString());
        GenLayerVoronoiZoom genlayervoronoizoom = new GenLayerVoronoiZoom(10L, genlayerrivermix);
        genlayerrivermix.initWorldGenSeed(par0);
        genlayervoronoizoom.initWorldGenSeed(par0);
        return genlayerrivermix;
    }
}
