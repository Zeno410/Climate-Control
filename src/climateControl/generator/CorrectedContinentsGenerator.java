package climateControl.generator;
import climateControl.BiomeRandomizer;
import climateControl.ClimateChooser;
import climateControl.api.ClimateControlRules;
import climateControl.api.ClimateControlSettings;
import climateControl.api.BiomeSettings;
import climateControl.customGenLayer.ConfirmBiome;
//import climateControl.customGenLayer.ConfirmClimate;
import climateControl.customGenLayer.DecodingSubBiome;
import climateControl.customGenLayer.HashEncodedAddLand;
import climateControl.customGenLayer.HashEncodedBiomeByClimate;
import climateControl.customGenLayer.HashEncodedSmoothWithClimates;
import climateControl.customGenLayer.EncodedZoom;
import climateControl.customGenLayer.GenLayerAddBiome;
import climateControl.customGenLayer.GenLayerDederpedHills;
import climateControl.customGenLayer.GenLayerOceanicIslands;
import climateControl.utils.RandomIntUser;
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
import climateControl.customGenLayer.GenLayerConfirm;
import climateControl.customGenLayer.GenLayerConfirmEncodings;
import climateControl.customGenLayer.GenLayerSmoothWithClimates;
import climateControl.customGenLayer.GenLayerConstant;
import climateControl.customGenLayer.GenLayerContinentalShelf;
import climateControl.customGenLayer.GenLayerDefineClimate;
import climateControl.customGenLayer.GenLayerLessRiver;
import climateControl.customGenLayer.GenLayerLowlandRiverMix;
import climateControl.customGenLayer.GenLayerNoPlains;
import climateControl.customGenLayer.GenLayerOceanicMushroomIsland;
import climateControl.customGenLayer.GenLayerPrettyShore;
import climateControl.customGenLayer.GenLayerRandomBiomes;

import climateControl.customGenLayer.GenLayerSmoothCoast;
import climateControl.customGenLayer.GenLayerSmoothWithBiomes;
import climateControl.customGenLayer.GenLayerSubBiome;

import climateControl.customGenLayer.GenLayerWidenRiver;
import climateControl.customGenLayer.GenLayerZoomBiome;
import net.minecraft.world.WorldType;
/**
 * This class creates a world generator from a ClimateControlSettings and a world seed
 * @author Zeno410
 */
public class CorrectedContinentsGenerator extends StackedContinentsGenerator {

    public CorrectedContinentsGenerator(ClimateControlSettings settings) {
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
        GenLayer genlayerisland = new GenLayerOceanicIslands(1L,emptyOcean,settings().largeContinentFrequency.value()
                ,settings().separateLandmasses.value());
        GenLayer genlayerfuzzyzoom = new GenLayerFuzzyZoom(2000L, genlayerisland);        
        GenLayer genlayeraddisland = new GenLayerAddLand(3L, genlayerfuzzyzoom);
        GenLayer smallContinents = new GenLayerOceanicIslands(4L, genlayeraddisland,
                settings().mediumContinentFrequency.value(),settings().separateLandmasses.value());
        genlayeraddisland = new GenLayerAddLand(5L, smallContinents);
        GenLayerZoom genlayerzoom = new GenLayerFuzzyZoom(2001L, genlayeraddisland);
        genlayeraddisland = new GenLayerAddLand(7L, smallContinents);
        smallContinents = new GenLayerOceanicIslands(8L, genlayerzoom,
                settings().smallContinentFrequency.value(),settings().separateLandmasses.value());
        genlayeraddisland = new GenLayerAddLand(9L, smallContinents);
        if (settings().doFull()) {
            genlayeraddisland = new GenLayerDefineClimate(10L, genlayeraddisland,2,1,1,2);
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
                    11L, genlayeraddisland,settings().largeIslandFrequency.value(),this.islandClimates()
                    ,settings().separateLandmasses.value());
        } else {
            genlayeraddisland = new GenLayerOceanicIslands(12L, genlayeraddisland,
                    settings().largeIslandFrequency.value(),settings().separateLandmasses.value());
        }
        genlayeraddisland = new GenLayerAddLand(13L, genlayeraddisland);
        if (settings().doHalf()) {
            genlayeraddisland = new GenLayerDefineClimate(14L, genlayeraddisland,settings());
            climatesAssigned = true;
        }
        genlayeraddisland = new GenLayerAdjustIsland(15L, genlayeraddisland,1,5,6);
        genlayeraddisland = new GenLayerSmoothWithClimates(16L, genlayeraddisland);
        genlayeraddisland = new GenLayerZoom(2003L, genlayeraddisland);
        if (climatesAssigned) {
            // climates are already defined so the island creator has to use a climate definer;
            genlayeraddisland = new GenLayerOceanicIslands(
                    17L, genlayeraddisland,settings().mediumIslandFrequency.value(),this.islandClimates()
                    ,settings().separateLandmasses.value());
        } else {
            genlayeraddisland = new GenLayerOceanicIslands(18L, genlayeraddisland,
                    settings().mediumIslandFrequency.value(),settings().separateLandmasses.value());
        }

        genlayeraddisland = new GenLayerAddLand(19L, genlayeraddisland);
        if (settings().quarterSize.value()) {
            genlayeraddisland = new GenLayerDefineClimate(20L, genlayeraddisland,settings());
            climatesAssigned = true;
        }
        genlayeraddisland = new GenLayerAdjustIsland(21L, genlayeraddisland,1,5,6);
        genlayeraddisland = new GenLayerSmoothWithClimates(22L, genlayeraddisland);
        genlayeraddisland = this.smoothClimates(settings(), worldSeed, genlayeraddisland);
        if (settings().testingMode.value()) genlayeraddisland = new GenLayerConfirm(genlayeraddisland);
        //genlayeraddisland = new GenLayerTestClimateSmooth(genlayeraddisland);
        GenLayer genlayerdeepocean = new GenLayerContinentalShelf(23L, genlayeraddisland);
        GenLayer genlayeraddmushroomisland = new GenLayerOceanicMushroomIsland(24L, genlayerdeepocean, settings().mushroomIslandIncidence.value());
        GenLayer genlayer3 = GenLayerZoom.magnify(1002L, new GenLayerSmoothWithClimates(3000L,genlayeraddmushroomisland), 0);
        genlayer3.initWorldGenSeed(worldSeed);
        if (settings().smootherCoasts.value()) {
            return climateControlExpansion(worldSeed,WorldType.DEFAULT,genlayer3,settings());
        }
        return this.vanillaExpansion(worldSeed,WorldType.DEFAULT,genlayer3,settings());
    }

    @Override
    public GenLayerRiverMix climateControlExpansion(long par0, WorldType par2WorldType,
            GenLayer genlayer3,ClimateControlSettings settings){
        byte b0 = 4;

        if (par2WorldType == WorldType.LARGE_BIOMES)
        {
            b0 = 6;
        } else b0 = settings.biomeSize.value().byteValue();

        GenLayer genlayer = GenLayerZoom.magnify(1003L, genlayer3, 0);
        GenLayer genlayerriverinit = new GenLayerLessRiver(102L, genlayer,settings().percentageRiverReduction.value());
        GenLayer biomes = null;
        if (settings.randomBiomes.value()) {
            biomes = new GenLayerRandomBiomes(par0,genlayer3,settings);
        } else {
            biomes = new GenLayerBiomeByClimate(par0,genlayer3,settings);
        }//par2WorldType.getBiomeLayer(par0, genlayer3);
        GenLayer object = new GenLayerZoomBiome(1004L, biomes);
        object = new GenLayerAddBiome(1005L, object);
        object  = new GenLayerSmoothWithBiomes(103L,object);
        object = new GenLayerZoom(1006L, object);
        object = new GenLayerAddBiome(1007L, object);
        object  = new GenLayerSmoothWithBiomes(104L,object);
        //object = biomes; //this is to shrink maps for viewing in AMIDST
        GenLayer genlayer1 = GenLayerZoom.magnify(1008L, genlayerriverinit, 2);
        GenLayer genlayerhills = null;
        if (false) {//if (settings.climateControlcompatibility.value()) {
            genlayerhills = new GenLayerDederpedHills(1009L, object, genlayer1);
        } else {
            genlayerhills = new GenLayerSubBiome(1009L, object, genlayer1,subBiomeChooser,mBiomeChooser,
                settings().doBoPSubBiomes());
        }
        genlayer = GenLayerZoom.magnify(1010L, genlayerriverinit, 2);
        genlayer = GenLayerZoom.magnify(1010L, genlayer, b0);
        GenLayer genlayerriver = new GenLayerRiver(1L, genlayer);
        if (settings.widerRivers.value()) {
            genlayerriver = new GenLayerWidenRiver(1L, genlayerriver);
        }
        GenLayerSmooth genlayersmooth = new GenLayerSmooth(1000L, genlayerriver);
        //object = new ConfirmBiome(genlayerhills);
        object = new GenLayerRareBiome(1001L, genlayerhills);

        for (int j = 0; j < b0; ++j)
        {
            object = new GenLayerZoom((long)(1000 + j), (GenLayer)object);

            if (j == 0)
            {
                object = new GenLayerAddIsland(3L, (GenLayer)object);
                object  = new GenLayerSmoothCoast(100L,object);
            }

            if (j == 1)
            {
                object  = new GenLayerSmoothCoast(100L,object);
                object = new GenLayerPrettyShore(1000L, (GenLayer)object,1.0F,rules());
                try {
                    //object = new GenLayerShoreHL(1000L,object);
                } catch (java.lang.NoClassDefFoundError e) {
                    //object = new GenLayerShore(1000L, (GenLayer)object);
                }
            }
        }

        //object = new ConfirmBiome(object);
        GenLayer genlayersmooth1 = new GenLayerSmooth(1000L, (GenLayer)object);
        //genlayersmooth1 = new ConfirmBiome(genlayersmooth1);
        //GenLayerRiverMix genlayerrivermix = new GenLayerRiverMix(100L, genlayersmooth1, genlayersmooth);
        GenLayerRiverMix genlayerrivermix = new GenLayerLowlandRiverMix(100L, genlayersmooth1, genlayersmooth,
                (settings().maxRiverChasm.value().floatValue()),rules());
        
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
        if (settings().testingMode.value()) {
            genlayer = new GenLayerConfirm(genlayer);
        }
        //GenLayerRiverInit genlayerriverinit = new GenLayerRiverInit(100L, genlayer);
        GenLayer biomes = null;
        IndirectDecoder biomeCodes = new IndirectDecoder();
        IndirectDecoder climateCodes = new IndirectDecoder();
        if (settings.randomBiomes.value()) {
            throw new RuntimeException("not yet implemented");
            //biomes = new GenLayerRandomBiomes(par0,genlayer3);
        } else {
            biomes = new HashEncodedBiomeByClimate(par0,genlayer3,settings,biomeCodes,climateCodes);
        }//par2WorldType.getBiomeLayer(par0, genlayer3);

        if (settings().testingMode.value()) {
            biomes = new GenLayerConfirm(biomes);
        }
        GenLayer object = new EncodedZoom(1000L, biomes,biomeCodes);
        object = new GenLayerConfirm(object);
        if (settings().testingMode.value()) {
            object = new GenLayerConfirmEncodings(object,biomeCodes);
        }
        object = new HashEncodedAddLand(1000L, object,biomeCodes);
        object  = new HashEncodedSmoothWithClimates(100L,object,biomeCodes);
        object = new EncodedZoom(1001L, object,biomeCodes);
        object = new HashEncodedAddLand(1000L, object,biomeCodes);
        object  = new HashEncodedSmoothWithClimates(101L,object,biomeCodes);
        GenLayer genlayer1 = object;// multipurpose
        GenLayer genlayerhills = null;
        if (false) {//if (settings.climateControlcompatibility.value()) {
            genlayerhills = new GenLayerDederpedHills(1000L, object, genlayer1);
        } else {
            genlayerhills = new DecodingSubBiome(1000L, object,subBiomeChooser,mBiomeChooser,
                    biomeCodes,climateCodes,new BiomeRandomizer(settings.biomeSettings()).pickByClimate());
        }
        //genlayer = GenLayerZoom.magnify(1000L, genlayerriverinit, 2);
        GenLayer riverlayer = object;// the encoding is now used for the river demarcations
        object = new GenLayerRareBiome(1001L, genlayerhills);

        for (int j = 0; j < b0; ++j)
        {
            object = new GenLayerZoom((long)(1000 + j), (GenLayer)object);
            object = new GenLayerSmoothCoast((long)(1001 + j), (GenLayer)object);
            riverlayer = new GenLayerZoom((long)(1000 + j), riverlayer);
            riverlayer = new GenLayerSmooth((long)(1001 + j), riverlayer);

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
                    object = new GenLayerShore(1000L, (GenLayer)object);
                }
            }
        }

        GenLayerRiver genlayerriver = new GenLayerRiver(1L, riverlayer);
        GenLayer genlayersmooth = new GenLayerSmooth(1000L, genlayerriver);

        GenLayerSmooth genlayersmooth1 = new GenLayerSmooth(1000L, (GenLayer)object);
        //GenLayerRiverMix genlayerrivermix = new GenLayerRiverMix(100L, genlayersmooth1, genlayersmooth);
        GenLayerRiverMix genlayerrivermix = new GenLayerLowlandRiverMix(100L, genlayersmooth1, genlayersmooth,
                (settings().maxRiverChasm.value().floatValue()),rules());
        GenLayerVoronoiZoom genlayervoronoizoom = new GenLayerVoronoiZoom(10L, genlayerrivermix);
        genlayerrivermix.initWorldGenSeed(par0);
        genlayervoronoizoom.initWorldGenSeed(par0);
        return genlayerrivermix;
    }
}