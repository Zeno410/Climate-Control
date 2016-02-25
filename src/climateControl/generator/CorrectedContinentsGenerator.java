package climateControl.generator;
import climateControl.ClimateChooser;
import climateControl.api.ClimateControlSettings;
//import climateControl.customGenLayer.ConfirmClimate;
import climateControl.customGenLayer.ConfirmBiome;
import climateControl.customGenLayer.GenLayerAddBiome;
import climateControl.customGenLayer.GenLayerOceanicIslands;
import climateControl.utils.IntRandomizer;
import climateControl.utils.RandomIntUser;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerAddIsland;
import net.minecraft.world.gen.layer.GenLayerFuzzyZoom;
import net.minecraft.world.gen.layer.GenLayerRareBiome;
import net.minecraft.world.gen.layer.GenLayerRiver;
import net.minecraft.world.gen.layer.GenLayerRiverMix;
import climateControl.genLayerPack.GenLayerSmooth;
import net.minecraft.world.gen.layer.GenLayerVoronoiZoom;
import net.minecraft.world.gen.layer.GenLayerZoom;
import climateControl.customGenLayer.GenLayerAddLand;
import climateControl.customGenLayer.GenLayerAdjustIsland;
import climateControl.customGenLayer.GenLayerBiomeByClimate;
import climateControl.customGenLayer.GenLayerBiomeByTaggedClimate;
import climateControl.customGenLayer.GenLayerConfirm;
import climateControl.customGenLayer.GenLayerSmoothWithClimates;
import climateControl.customGenLayer.GenLayerConstant;
import climateControl.customGenLayer.GenLayerContinentalShelf;
import climateControl.customGenLayer.GenLayerDefineClimate;
import climateControl.customGenLayer.GenLayerForceStartLand;
import climateControl.customGenLayer.GenLayerGrowWithoutMerge;
import climateControl.customGenLayer.GenLayerIdentifiedClimate;
import climateControl.customGenLayer.GenLayerLessRiver;
import climateControl.customGenLayer.GenLayerLowlandRiverMix;
import climateControl.customGenLayer.GenLayerOceanicMushroomIsland;
import climateControl.customGenLayer.GenLayerPrettyShore;
import climateControl.customGenLayer.GenLayerRandomBiomes;

import climateControl.customGenLayer.GenLayerSmoothCoast;
import climateControl.customGenLayer.GenLayerSmoothWithBiomes;
import climateControl.customGenLayer.GenLayerSubBiome;

import climateControl.customGenLayer.GenLayerLandReport;
import climateControl.customGenLayer.GenLayerLimitedCache;
import climateControl.customGenLayer.GenLayerSmoothClimate;
import climateControl.customGenLayer.GenLayerWidenRiver;
import climateControl.customGenLayer.GenLayerZoomBiome;
import climateControl.genLayerPack.GenLayerBreakMergers;
import climateControl.genLayerPack.GenLayerShore;
import climateControl.utils.StringWriter;
import java.io.File;
import java.io.IOException;
import net.minecraft.world.WorldType;
/**
 * This class creates a world generator from a ClimateControlSettings and a world seed
 * @author Zeno410
 */
public class CorrectedContinentsGenerator extends AbstractWorldGenerator {

    private File serverDirectoryFile;
    public CorrectedContinentsGenerator(ClimateControlSettings settings, File serverDirectory) {
        super(settings);
        serverDirectoryFile = serverDirectory;
    }

    protected RandomIntUser islandClimates() {
        return new ClimateChooser(settings().hotIncidence.value(),
                settings().warmIncidence.value(),
                settings().coolIncidence.value(),
                settings().snowyIncidence.value());
    }

    protected RandomIntUser landmassIdentifier() {
        if (!this.settings().separateLandmasses.value()) return this.justLand();
        return new RandomIntUser() {
            @Override
            public int value(IntRandomizer randomizer) {
                return randomizer.nextInt(10000)+256  ;
            }

        };
    }
    protected void setOceanSubBiomes() {

    }
    public GenLayerRiverMix fromSeed(long worldSeed, WorldType worldType) {

        this.subBiomeChooser.clear();
        this.subBiomeChooser.set(settings().biomeSettings());
        setOceanSubBiomes();
        this.mBiomeChooser.set(settings().biomeSettings());
        setRules();
        boolean climatesAssigned = false;

        GenLayer emptyOcean = new GenLayerConstant(0);
        GenLayer genlayerisland = new GenLayerOceanicIslands(1L,emptyOcean,settings().largeContinentFrequency.value()
                ,this.landmassIdentifier(),settings().separateLandmasses.value(),"Large Continent");
        GenLayer genlayeraddisland = growRound(genlayerisland,2L,3L,climatesAssigned);

        genlayeraddisland = new GenLayerFuzzyZoom(2000L, genlayeraddisland);
        genlayeraddisland = new GenLayerSmooth(2004L,genlayeraddisland);
        genlayeraddisland = separatedGrowth(genlayeraddisland,30L,climatesAssigned);
        GenLayer mediumContinents = new GenLayerOceanicIslands(4L, genlayeraddisland,
                settings().mediumContinentFrequency.value(),this.landmassIdentifier(),
                settings().separateLandmasses.value(),"Medium Continent");

        genlayeraddisland = growRound(mediumContinents,5L,7L,climatesAssigned);
        GenLayer genlayerzoom = new GenLayerFuzzyZoom(2001L, genlayeraddisland);
        genlayerzoom = new GenLayerSmooth(2008l,genlayerzoom);
        genlayeraddisland = separatedGrowth(genlayeraddisland,31L,climatesAssigned);

        GenLayer smallContinents = new GenLayerOceanicIslands(8L, genlayerzoom,
                settings().smallContinentFrequency.value(),this.landmassIdentifier(),
                settings().separateLandmasses.value(),"Small Continent");
        if (settings().forceStartContinent.value()) {
            smallContinents = new GenLayerForceStartLand(smallContinents);
        }
        genlayeraddisland = growRound(smallContinents,2L,3L,climatesAssigned);
        if (settings().doFull()) {
            //genlayeraddisland = new GenLayerDefineClimate(10L, genlayeraddisland,settings());
            //genlayeraddisland = new GenLayerSmoothClimate(1010L,genlayeraddisland);
            genlayeraddisland = new GenLayerIdentifiedClimate(1014L,genlayeraddisland,settings());
            climatesAssigned = true;
        }

        if (settings().testingMode.value()) {
            genlayeraddisland = new GenLayerConfirm(genlayeraddisland);
            //genlayeraddisland = this.reportOn(genlayeraddisland, "smoothed.txt");
        }
    
        genlayeraddisland = new GenLayerZoom(2002L, genlayeraddisland);
        genlayeraddisland = new GenLayerSmooth(2012L,genlayeraddisland);
        genlayeraddisland = separatedGrowth(genlayeraddisland,32L,climatesAssigned);

        if (climatesAssigned) {
            // climates are already defined so the island creator has to use a climate definer;
            genlayeraddisland = new GenLayerOceanicIslands(
                    11L, genlayeraddisland,settings().largeIslandFrequency.value(),
                    // if separating have to use identifiedClimates();
                    settings().separateLandmasses.value()? this.identifiedClimate() : this.islandClimates()
                    ,settings().separateLandmasses.value(),"Large Island");
        } else {
            genlayeraddisland = new GenLayerOceanicIslands(11L, genlayeraddisland,
                    settings().largeIslandFrequency.value(),this.landmassIdentifier(),
                    settings().separateLandmasses.value(),"Large Island");
        }
        
        // add land without merging if separating and just add otherwise
        genlayeraddisland = growRound(genlayeraddisland,13L,15L,climatesAssigned);
        if (settings().testingMode.value()) {
            genlayeraddisland = this.reportOn(genlayeraddisland, "largeIslands.txt");
        }
        if (settings().doHalf()) {
            //genlayeraddisland = new GenLayerDefineClimate(14L, genlayeraddisland,settings());
            //genlayeraddisland = new GenLayerSmoothClimate(1014L,genlayeraddisland);
            genlayeraddisland = new GenLayerIdentifiedClimate(1014L,genlayeraddisland,settings());
            climatesAssigned = true;
        }
        if (settings().testingMode.value()) {
            genlayeraddisland = new GenLayerConfirm(genlayeraddisland);
            //genlayeraddisland = this.reportOn(genlayeraddisland, "smoothed.txt");
        }
        genlayeraddisland = new GenLayerZoom(2003L, genlayeraddisland);
        genlayeraddisland = new GenLayerSmooth(2017L,genlayeraddisland);
        if (climatesAssigned) {
            // climates are already defined so the island creator has to use a climate definer;
            genlayeraddisland = new GenLayerOceanicIslands(
                    17L, genlayeraddisland,settings().mediumIslandFrequency.value(),
                    // if separating have to use identifiedClimates();
                    settings().separateLandmasses.value()? this.identifiedClimate() : this.islandClimates()
                    ,settings().separateLandmasses.value(),"Medium Island");
        } else {
            genlayeraddisland = new GenLayerOceanicIslands(17L, genlayeraddisland,
                    settings().mediumIslandFrequency.value(),this.landmassIdentifier(),
                    settings().separateLandmasses.value(),"Medium Island");
        }
        //genlayeraddisland = new GenLayerAddLand(19L, genlayeraddisland);
         genlayeraddisland = new GenLayerAdjustIsland(21L, genlayeraddisland,1,12,12,
                 settings().separateLandmasses.value()||climatesAssigned);
        if (settings().quarterSize.value()) {
            //genlayeraddisland = new GenLayerDefineClimate(20L, genlayeraddisland,settings());
            //genlayeraddisland = new GenLayerSmoothClimate(1014L,genlayeraddisland);
            genlayeraddisland = new GenLayerIdentifiedClimate(1014L,genlayeraddisland,settings());
            climatesAssigned = true;
        }
        genlayeraddisland = new GenLayerSmoothClimate(22L,genlayeraddisland);
        if (settings().testingMode.value()) {
            genlayeraddisland = this.reportOn(genlayeraddisland, "mediumIslands.txt");
        }
        if (settings().testingMode.value()) {
            genlayeraddisland = new GenLayerConfirm(genlayeraddisland);
            //genlayeraddisland = this.reportOn(genlayeraddisland, "smoothed.txt");
        }
         genlayeraddisland = new GenLayerLimitedCache(genlayeraddisland,100);
        //genlayeraddisland = new GenLayerTestClimateSmooth(genlayeraddisland);
        GenLayer genlayerdeepocean = new GenLayerContinentalShelf(23L, genlayeraddisland);
        GenLayer genlayeraddmushroomisland = new GenLayerOceanicMushroomIsland(24L, genlayerdeepocean, settings().mushroomIslandIncidence.value());

        GenLayer genlayer3 = GenLayerZoom.magnify(1002L, genlayeraddmushroomisland, 0);
        genlayer3.initWorldGenSeed(worldSeed);
        if (settings().testingMode.value()) {
            genlayeraddisland = this.reportOn(genlayeraddisland, "preBiome.txt");
        }
        if (settings().smootherCoasts.value()) {
            return climateControlExpansion(worldSeed,worldType,genlayer3,settings());
        }
        return this.vanillaExpansion(worldSeed,worldType,genlayer3,settings());
    }

    private GenLayer growRound(GenLayer genlayeraddisland,long firstSeed,long secondSeed, boolean climatesAssigned) {
                // add land without merging if separating and just add otherwise
        if (settings().separateLandmasses.value()||climatesAssigned) {
            genlayeraddisland = new GenLayerAddLand(firstSeed, genlayeraddisland,true);
            genlayeraddisland = new GenLayerBreakMergers(firstSeed+1000,genlayeraddisland);
            genlayeraddisland = new GenLayerAdjustIsland(secondSeed, genlayeraddisland,3,11,12,true);
            genlayeraddisland = new GenLayerBreakMergers(secondSeed+1000,genlayeraddisland);
            genlayeraddisland = new GenLayerAdjustIsland(secondSeed, genlayeraddisland,3,11,12,true);
            genlayeraddisland = new GenLayerBreakMergers(secondSeed+1000,genlayeraddisland);
            //genlayeraddisland = new GenLayerGrowWithoutMerge(firstSeed, genlayeraddisland,3,12,12);
            //genlayeraddisland = new GenLayerGrowWithoutMerge(secondSeed, genlayeraddisland,3,12,12);
        } else {
            genlayeraddisland = new GenLayerAddLand(firstSeed, genlayeraddisland,climatesAssigned);
            genlayeraddisland = new GenLayerAdjustIsland(secondSeed, genlayeraddisland,3,11,12,
                    climatesAssigned);
            genlayeraddisland = new GenLayerAdjustIsland(secondSeed, genlayeraddisland,3,11,12,
                    climatesAssigned);
        }
        return genlayeraddisland;
    }

    private GenLayer separatedGrowth(GenLayer genlayeraddisland,long secondSeed, boolean climatesAssigned) {
                // add land without merging if separating and just add otherwise
            genlayeraddisland = new GenLayerAddLand(secondSeed, genlayeraddisland,true);
            genlayeraddisland = new GenLayerBreakMergers(secondSeed+1000,genlayeraddisland);
            genlayeraddisland = new GenLayerAdjustIsland(secondSeed+2000, genlayeraddisland,3,11,12,true);
            genlayeraddisland = new GenLayerBreakMergers(secondSeed+3000,genlayeraddisland);
            genlayeraddisland = new GenLayerAdjustIsland(secondSeed+2000, genlayeraddisland,3,11,12,true);
            genlayeraddisland = new GenLayerBreakMergers(secondSeed+3000,genlayeraddisland);
        return genlayeraddisland;
    }

    GenLayer reportOn(GenLayer reportedOn, String fileName) {
        if (this.serverDirectoryFile!= null) {
            try {
                StringWriter target = new StringWriter(new File(serverDirectoryFile, fileName));
                reportedOn = new GenLayerLandReport(reportedOn,40,target);
                return reportedOn;
            } catch (IOException iOException) {
                throw new RuntimeException(iOException);
            }
        }
        return reportedOn;
    }

    private RandomIntUser justLand() {
        return new RandomIntUser() {
            @Override
            public int value(IntRandomizer randomizer) {
                return 1;
            }

        };

    }

    private RandomIntUser identifiedClimate() {
        return new RandomIntUser() {
            RandomIntUser climate = islandClimates();
            RandomIntUser identifier = landmassIdentifier();

            @Override
            public int value(IntRandomizer randomizer) {
                return climate.value(randomizer) + 4 *identifier.value(randomizer);
            }
        };
    }

    public GenLayerRiverMix climateControlExpansion(long par0, WorldType par2WorldType,
            GenLayer genlayer3,ClimateControlSettings settings){
        byte b0 = 4;

        if (par2WorldType == WorldType.LARGE_BIOMES)
        {
            b0 = 6;
        } else b0 = settings.biomeSize.value().byteValue();

        GenLayer genlayer = GenLayerZoom.magnify(1003L, genlayer3, 0);
        GenLayer genlayerriverinit = new GenLayerLessRiver(102L, genlayer,
                rtgAwareRiverReduction(settings().percentageRiverReduction.value(), par2WorldType));
        GenLayer biomes = null;
        if (settings.randomBiomes.value()) {
            biomes = new GenLayerRandomBiomes(par0,genlayer3,settings);
        } else {
            biomes = new GenLayerBiomeByTaggedClimate(par0,genlayer3,settings);
        }
        if (settings().testingMode.value()) {
            biomes = this.reportOn(biomes, "Biomes.txt");
        }
        GenLayer object = new GenLayerZoom(1004L, biomes);
        object = new GenLayerAddBiome(1005L, object);
        object  = new GenLayerSmooth(103L,object);
        object = new GenLayerZoomBiome(1006L, object);
        object = new GenLayerAddBiome(1007L, object);
        object  = new GenLayerSmoothCoast(104L,object);
        //object = biomes; //this is to shrink maps for viewing in AMIDST
        GenLayer genlayer1 = GenLayerZoom.magnify(1008L, genlayerriverinit, 2);
        GenLayer genlayerhills = null;
            genlayerhills = new GenLayerSubBiome(1009L, object, genlayer1,subBiomeChooser,mBiomeChooser,
                settings().doBoPSubBiomes());
        genlayer = GenLayerZoom.magnify(1010L, genlayerriverinit, 2);
        genlayer = GenLayerZoom.magnify(1010L, genlayer, b0);
        GenLayer genlayerriver = new GenLayerRiver(1L, genlayer);
        if (settings.widerRivers.value()) {
            genlayerriver = new GenLayerWidenRiver(1L, genlayerriver);
        }
        GenLayer genlayersmooth = new GenLayerSmoothCoast(1000L, genlayerriver);
        object = new GenLayerRareBiome(1001L, genlayerhills);
        object = new ConfirmBiome(object);

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
            }
            if (settings.wideBeaches.value()) {
                if (j==0) {
                    object = new GenLayerPrettyShore(1000L, (GenLayer)object,1.0F,rules());
                }
            } else {
                if (j==1) {
                    object = new GenLayerPrettyShore(1000L, (GenLayer)object,1.0F,rules());
                }
            }
        }

        GenLayer genlayersmooth1 = new GenLayerSmooth(1000L, (GenLayer)object);
        if (settings().cachingOn()) {
           genlayersmooth1 = new GenLayerLimitedCache(genlayersmooth1,16*settings().cacheSize());
        }
        GenLayerRiverMix genlayerrivermix = new GenLayerLowlandRiverMix(100L, genlayersmooth1, genlayersmooth,
                (settings().maxRiverChasm.value().floatValue()),rules());
        
        GenLayerVoronoiZoom genlayervoronoizoom = new GenLayerVoronoiZoom(10L, genlayerrivermix);
        genlayerrivermix.initWorldGenSeed(par0);
        genlayervoronoizoom.initWorldGenSeed(par0);

        return genlayerrivermix;
    }
      /*   Here follows a lot of stuff for testing the spawn chunks problem

      int [] test = genlayervoronoizoom.getInts(-460, 164, 16, 16);
        for (int i = 0; i < test.length;i++) {
            if (test[i]==0) throw new RuntimeException();
        }
        genlayerconfirm.testing= true;
        if (previousTest != null) {
            for (int i = 0; i < test.length;i++) {
                if (test[i]!=previousTest[i]) {

                    String message = "" +test[i] + " " + previousTest[i]
                            + previousSeed + " "+par0 + " " + previousWarm +
                            " "+settings().warmIncidence.value()
                            + " " + previousSize + " " + (new BiomeRandomizer(settings().biomeSettings()).size())
                            + " " + previousSeparate + " " + settings().separateLandmasses.value()
                            + " "+ previousLargeContinents + " " + settings().largeContinentFrequency.value();


                    throw new RuntimeException(message);
                }
            }
        }

        previousTest = new int[test.length];
        for (int i = 0; i< test.length;i++) {
            previousTest[i] = test[i];
        }
        previousSeed = par0;
        previousWarm = settings().warmIncidence.value();
        previousSize = new BiomeRandomizer(settings().biomeSettings()).size();
        previousSeparate = settings().separateLandmasses.value();
        previousLargeContinents = settings().largeContinentFrequency.value();
    }
    private static int [] previousTest;
    private static long previousSeed;
    private static int previousWarm;
    private static int previousSize;
    private static boolean previousSeparate;
    private static int previousLargeContinents;*/

}