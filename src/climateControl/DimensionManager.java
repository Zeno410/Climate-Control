
package climateControl;

import climateControl.utils.PlaneLocation;
import climateControl.utils.Accessor;
import climateControl.utils.Named;
import climateControl.api.BiomeSettings;
import climateControl.api.CCDimensionSettings;
import climateControl.api.ClimateControlSettings;
import climateControl.api.DimensionalSettingsRegistry;
import climateControl.biomeSettings.BoPSettings;
import climateControl.customGenLayer.GenLayerLowlandRiverMix;
import climateControl.customGenLayer.GenLayerRiverMixWrapper;
import climateControl.genLayerPack.GenLayerPack;
import climateControl.generator.CorrectedContinentsGenerator;
import climateControl.generator.OneSixCompatibleGenerator;
import climateControl.generator.StackedContinentsGenerator;
import climateControl.generator.TestGeneratorPair;
import climateControl.generator.VanillaCompatibleGenerator;
import climateControl.utils.ChunkGeneratorExtractor;
import climateControl.utils.ChunkLister;
import climateControl.utils.ConfigManager;
import climateControl.utils.TaggedConfigManager;
import climateControl.utils.Zeno410Logger;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Logger;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerRiverMix;
import net.minecraft.world.gen.layer.IntCache;
import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.terraingen.WorldTypeEvent;
import net.minecraftforge.event.world.WorldEvent;

/**
 *
 * @author Zeno410
 */
public class DimensionManager {
    public static Logger logger = new Zeno410Logger("DimensionManager").logger();

    private Accessor<GenLayerRiverMix,GenLayerPack> riverMixBiome =
            new Accessor<GenLayerRiverMix,GenLayerPack>("field_75910_b");

    private final ClimateControlSettings newSettings;
    private final CCDimensionSettings dimensionSettings;
    private GenLayerUpdater genLayerUpdater = new GenLayerUpdater();

    public DimensionManager(ClimateControlSettings newSettings,CCDimensionSettings dimensionSettings,MinecraftServer server) {
        this.newSettings = newSettings;
        this.dimensionSettings = dimensionSettings;
        if (server == null) {
            this.configDirectory= null;
            this.suggestedConfigFile = null;
            return;
        }
        this.configDirectory= server.getFile("config");
        this.suggestedConfigFile = new File(configDirectory,"climatecontrol.cfg");
    }
    
    private GenLayerRiverMix patchedGenLayer(ClimateControlSettings settings,
            WorldType worldType,
            long worldSeed) {

        logger.info("patching GenLayer: world seed "+worldSeed+"world type "+worldType.getWorldTypeName());
        for (BiomeSettings biomeSettings: settings.biomeSettings()) {
            //biomeSettings.report();
        }
        if (ignore(worldType,settings)) return null;
        if (settings.noGenerationChanges.value()) {
            if (settings.oneSixCompatibility.value()) {
                return new OneSixCompatibleGenerator(settings).fromSeed(worldSeed);
            } else {
                return null;
            }
        }
        // check settings
        new SettingsTester().test(settings);
        GenLayerRiverMix newMix = null;
        //logger.info("world seed " + worldSeed);
        if (settings.vanillaLandAndClimate.value()) {
             newMix = new VanillaCompatibleGenerator(settings).fromSeed(worldSeed);
        } else {
            if (settings.zeroPointFive.value()) {
                newMix = new CorrectedContinentsGenerator(settings).fromSeed(worldSeed);
            } else {
                StackedContinentsGenerator generator = new StackedContinentsGenerator(settings);
                newMix = generator.fromSeed(worldSeed);
            }
        }
        GenLayer oldGen = null;//riverMixBiome.get(activeRiverMix);
        GenLayer newGen = riverMixBiome.get(newMix);
        TestGeneratorPair pair = new TestGeneratorPair(oldGen,newGen);
        while (true) {
            //logger.info(pair.description());
            if (!pair.hasNext()) break;
            pair = pair.next();
        }
        if (newMix instanceof GenLayerLowlandRiverMix) {
            ((GenLayerLowlandRiverMix)newMix).setMaxChasm(settings.maxRiverChasm.value().floatValue());
        }
        for (BiomeSettings biomeSettings: settings.biomeSettings()) {
            //biomeSettings.report();
        }
        return newMix;
    }

    private ClimateControlSettings newSettings() {
        return newSettings;
    }

    //private GenLayerRiverMixWrapper riverLayerWrapper = new GenLayerRiverMixWrapper(0L);

    private HashMap<Integer,ClimateControlSettings> dimensionalSettings = new HashMap<Integer,ClimateControlSettings>();
    private HashMap<Integer,GenLayerRiverMixWrapper> wrappers = new HashMap<Integer,GenLayerRiverMixWrapper>();

    private GenLayerRiverMixWrapper riverLayerWrapper(int dimension) {
        GenLayerRiverMixWrapper result = wrappers.get(dimension);
        if (result == null) {
            result = new GenLayerRiverMixWrapper(0L);
            result.setOriginal(original);
            wrappers.put(dimension,result);
        }
        return result;
    }

    private final File suggestedConfigFile;
    private final File configDirectory;
    
    private TaggedConfigManager addonConfigManager = 
            new TaggedConfigManager("climatecontrol.cfg","ClimateControl");

    private ClimateControlSettings defaultSettings(MinecraftFilesAccess dimension, boolean newWorld) {
        ClimateControlSettings result = defaultSettings(newWorld);
        //logger.info(dimension.baseDirectory().getAbsolutePath());
        if (!dimension.baseDirectory().exists()) {
            dimension.baseDirectory().mkdir();
            if (!dimension.baseDirectory().exists()) {
            }
        }
        Configuration workingConfig = new Configuration(suggestedConfigFile);
        workingConfig.load();
        ConfigManager<ClimateControlSettings> workingConfigManager = new ConfigManager<ClimateControlSettings>(
        workingConfig,result,suggestedConfigFile);
        workingConfigManager.setWorldFile(dimension.baseDirectory());
        workingConfigManager.saveWorldSpecific();

        for (Named<BiomeSettings> addonSetting: result.registeredBiomeSettings()) {
            addonConfigManager.initializeConfig(addonSetting, configDirectory);
            addonConfigManager.updateConfig(addonSetting, configDirectory, dimension.configDirectory());
            if (newWorld) {
                addonSetting.object.onNewWorld();
                addonConfigManager.saveConfigs( dimension.configDirectory(), addonSetting);
            }
        }
        return result;
    }

    private ClimateControlSettings defaultSettings(boolean newWorld) {
        ClimateControlSettings result = new ClimateControlSettings();
        Configuration workingConfig = new Configuration(suggestedConfigFile);
        workingConfig.load();
        result.readFrom(workingConfig);
        result.setDefaults(configDirectory);
        for (Named<BiomeSettings> addonSetting: result.registeredBiomeSettings()) {
            if (newWorld) addonSetting.object.onNewWorld();
            addonConfigManager.initializeConfig(addonSetting, configDirectory);
        }
        return result;
    }

    private ClimateControlSettings dimensionalSettings(DimensionAccess dimension, boolean newWorld) {
        ClimateControlSettings result = dimensionalSettings.get(dimension.dimension);
        if (result == null) {
            result = defaultSettings(dimension,newWorld);
            DimensionalSettingsRegistry.instance.modify(dimension.dimension, result);
            dimensionalSettings.put(dimension.dimension,result);
        }
        return result;

    }

    private GenLayer original;
    //GenLayer modified;
    /*GenLayer [] modifiedGenerators(long worldSeed) {
        GenLayer voronoi = new GenLayerVoronoiZoom(0L,original);
        return new GenLayer[] {original,voronoi,original};
    }

    GenLayer [] originalGenerators(long worldSeed) {
        GenLayer voronoi = new GenLayerVoronoiZoom(0L,original);
        return new GenLayer[] {modified,voronoi,modified};
    }*/

    public void onBiomeGenInit(WorldTypeEvent.InitBiomeGens event) {
        // skip if ignoring

        ClimateControlSettings generationSettings = defaultSettings(true);
        // this only gets used for new worlds,
        //when WorldServer is initializing and there are no spawn chunks yet
        generationSettings.onNewWorld();
        if (this.ignore(event.worldType,this.newSettings)) return;
        logger.info("not ignored");
        MinecraftServer server = MinecraftServer.getServer();
        if (server== null) {
            logger.info("blanked");
            original = event.originalBiomeGens[0];
            riverLayerWrapper(0).setOriginal(event.originalBiomeGens[0]);
            riverLayerWrapper(0).useOriginal();
            return;
        }

        // get overworld dimension;
        boolean newWorld = true;
        for (WorldServer worldServer: server.worldServers){
            if (worldServer.getTotalWorldTime()>0 ) newWorld = false;
        }
        //logger.info(worldType.getTranslateName());
        //logger.info(event.originalBiomeGens[0].toString());
        // if not a recognized world type ignore
        //logBiomes();
        //this.activeRiverMix = (GenLayerRiverMix)(event.originalBiomeGens[0]);

        original = event.originalBiomeGens[0];
        riverLayerWrapper(0).setOriginal(event.originalBiomeGens[0]);

        if (generationSettings.noGenerationChanges.value()) {
            event.newBiomeGens = riverLayerWrapper(0).modifiedGenerators();
            riverLayerWrapper(0).useOriginal();
            return;
        } else {
            // continue below
        }
        if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
            riverLayerWrapper(0).setRedirection(patchedGenLayer(generationSettings, event.worldType, event.seed));
            event.newBiomeGens = riverLayerWrapper(0).modifiedGenerators();
            event.setResult(WorldTypeEvent.Result.ALLOW);
        } else {
            //riverLayerWrapper(0).useOriginal();
            //event.newBiomeGens = modifiedGenerators(event.seed);
        }
    }

    public void serverStopped(FMLServerStoppedEvent event) {
        new HashMap<Integer,GenLayerRiverMixWrapper>();
        dimensionalSettings = new HashMap<Integer,ClimateControlSettings>();
    }

    private boolean ignore(WorldType considered, ClimateControlSettings settings) {
        if (considered == null) return true;
        if (considered.equals(WorldType.AMPLIFIED)) return false;
        if (considered.equals(WorldType.DEFAULT)) {
            return false;
        }
        if (considered.equals(WorldType.DEFAULT_1_1)) return false;
        if (considered.equals(WorldType.LARGE_BIOMES)) return false;
        if (considered.equals(WorldType.FLAT)) return true;
        if (settings.interveneInBOPWorlds.value()) {
            if (considered.getWorldTypeName().equalsIgnoreCase("BIOMESOP")) return false;
        }
        if (settings.interveneInHighlandsWorlds.value()) {

            if (considered.getWorldTypeName().equalsIgnoreCase("Highlands")) return false;
            if (considered.getWorldTypeName().equalsIgnoreCase("HighlandsLB")) return false;
        }
        logger.info(considered.getWorldTypeName());
        if (true) {
            if (considered.getWorldTypeName().equalsIgnoreCase("FWG")) return false;
        }

        if (considered.getWorldTypeName().equalsIgnoreCase("RTG")) return false;
        return true;
    }

    public void onWorldLoad(WorldEvent.Load event) {
        logger.info(event.world.provider.terrainType.getWorldTypeName()+ " "+ event.world.provider.dimensionId);
        if (ignore(event.world.provider.terrainType,this.newSettings)) {
            return;
        }
        World world = event.world;
        int dimension = world.provider.dimensionId;
        logger.info(""+this.dimensionSettings.ccOnIn(dimension));
        if (!this.dimensionSettings.ccOnIn(dimension)) {
            if (!DimensionalSettingsRegistry.instance.useCCIn(dimension)) {
                return;
            }
        }// only change dimensions we're supposed to;
        if (world.isRemote) {
            return;
        }
        // pull out the Chunk Generator
        // this whole business will crash if things aren't right. Probably the best behavior,
        // although a polite message might be appropriate

        // salvage spawn if new world

        // do nothing for client worlds

        if (!(world instanceof WorldServer)) return;

        WorldServer worldServer = (WorldServer)(event.world);
        DimensionAccess dimensionAccess = new DimensionAccess(dimension,worldServer);

        long worldSeed = world.getSeed();
        if (world instanceof WorldServer&&worldSeed!=0)  {
            ClimateControlSettings currentSettings = null;
            boolean newWorld = false;
            if(world.getWorldInfo().getWorldTotalTime()<10) {
                // new world
                newWorld = true;
            }
            currentSettings = dimensionalSettings(dimensionAccess,newWorld);
            logger.info(""+dimension +" "+ currentSettings.snowyIncidence.value() +" "+ currentSettings.coolIncidence.value());

            riverLayerWrapper(dimension).setOriginal(original);

            try {
                GenLayerRiverMix patched = patchedGenLayer(currentSettings,world.provider.terrainType,worldSeed);
                if (patched != null) {
                    riverLayerWrapper(dimension).setRedirection(patched);
                    genLayerUpdater.update(this.riverLayerWrapper(dimension), world.provider);
                    this.riverLayerWrapper(dimension).lock(dimension, world,currentSettings);
                } else {
                    // lock manually
                    LockGenLayers biomeLocker = new LockGenLayers();
                    WorldChunkManager chunkGenerator = world.getWorldChunkManager();
                    Accessor<WorldChunkManager,GenLayer> worldGenLayer =
                        new Accessor<WorldChunkManager,GenLayer>("field_76944_d");
                    GenLayer toLock = worldGenLayer.get(chunkGenerator);
                    if (toLock instanceof GenLayerRiverMixWrapper) {
                       toLock = original;
                    }
                        Accessor<GenLayerRiverMix,GenLayer> riverMixBiome =
                        new Accessor<GenLayerRiverMix,GenLayer>("field_75910_b");
                        toLock = riverMixBiome.get((GenLayerRiverMix)toLock);
                    biomeLocker.lock(toLock, dimension, world, currentSettings);
                }
            // spawn rescue
            if (currentSettings.vanillaLandAndClimate.value() == false){
                if (currentSettings.noGenerationChanges.value() == false) {
                    //logger.info(new ChunkGeneratorExtractor().extractFrom((WorldServer)world).toString());
                    try {
                        new ChunkGeneratorExtractor().impose((WorldServer) world, new MapGenVillage());
                    } catch (Exception e) {
                    }
                    if(world.getWorldInfo().getWorldTotalTime()<10) {
                        ArrayList<PlaneLocation> existingChunks =
                        new ChunkLister().savedChunks(levelSavePath((WorldServer)world));
                        logger.info("existing chunks:"+existingChunks.size());
                        //world.provider.worldChunkMgr.
                        salvageSpawn(world);
                    }
                }
            }
            } catch (Exception e) {
                logger.info(e.toString());
                logger.info(e.getMessage());
                throw new RuntimeException(e);
            } catch (Error e) {
                logger.info(e.toString());
                logger.info(e.getMessage());
            }
            logger.info("start rescued");
        } else {
            genLayerUpdater.update(this.riverLayerWrapper(dimension), world.provider);
            LockGenLayer.logger.info(world.toString());
            /*
            if (!(world instanceof WorldClient)) {
                logger.info("World Client problem");
                throw new RuntimeException();
                //logger.info("locking "+riverLayerWrapper.forLocking().toString());
                //this.biomeLocker.lock(riverMixBiome.get(activeRiverMix), dimension, this.servedWorlds.get(dimension),newSettings);
            } else {
                //logger.info("client world");
            }*/
            //this.riverMixBiome.setField(this.vanillaGenLayer,replacement);
            //this.riverMixRiver.setField(this.vanillaGenLayer,replacement);

        }
        //this.biomeLocker.showGenLayers(accessGenLayer.get(world.provider.worldChunkMgr));
    }

   PlaneLocation lastTry = new PlaneLocation(Integer.MIN_VALUE,Integer.MIN_VALUE);

    private void salvageSpawn(World world) {
        WorldInfo info = world.getWorldInfo();
        int x= info.getSpawnX();
        int z= info.getSpawnZ();
        //x = x/16;
        //z = z/16;
        int move = 0;
        int ring = 0;
        int xMove = 0;
        int zMove = 0;
        int spawnX = 0;
        int spawnZ = 0;
        int spawnY = 0;
        BiomeGenBase checkSpawn = world.getBiomeGenForCoords(x, z);
        int nextTry = 50;
        int nextTryIncrement = 50;
        while(spawnY < 64) {
            //while (isSea(checkSpawn)) {
                //spiral out around spawn;
                move ++;
                if (move>((ring)*(ring+1)*4)) {
                    ring ++;
                }
                // do 1 in 1000
                if (move<nextTry) continue;
                nextTryIncrement += 50;
                nextTry += nextTryIncrement;
                world.getChunkProvider().unloadQueuedChunks();
                int inRing = move - (ring-1)*(ring)*4;
                // find which side of the ring we are on;
                // note inRing is 1-based not 0-base
                if (inRing > ring * 6) {
                    // left side
                    xMove = -ring;
                    zMove = ring - (inRing-ring*6) + 1;
                } else if (inRing > ring *4) {
                    zMove = ring;
                    xMove = ring - (inRing-ring*4) + 1;
                } else if (inRing > ring * 2) {
                    xMove = ring;
                    zMove = -ring + (inRing-ring*2) -1;
                } else {
                    zMove = -ring;
                    xMove = -ring + (inRing) -1;
                }
                IntCache.resetIntCache();
                logger.info("checking for spawn at "+ (x+xMove*16) + ","+ (z+zMove*16) + "move " + move
                        + " ring "+ ring + " inRing " + inRing+ " caches " + IntCache.getCacheSizes());
                checkSpawn = world.getBiomeGenForCoords(x+xMove*16, z+zMove*16);
            //}
            //int spawnY = checkSpawn.getHeightValue(8,8);
            spawnX = x+xMove*16;
            spawnZ = z+zMove*16;
            logger.info("setting spawn at "+ spawnX + ","+ spawnZ);
            if (checkSpawn.biomeID == BiomeGenBase.mushroomIsland.biomeID) continue;
            spawnY = world.getTopSolidOrLiquidBlock(spawnX, spawnZ)+1;
            PlaneLocation newLocation = new PlaneLocation(spawnX,spawnZ);
            logger.info(lastTry.toString()+ newLocation.toString());
            if (newLocation.equals(lastTry)) break;
        }
        world.setSpawnLocation(spawnX,spawnY,spawnZ);
    }

    private String levelSavePath(WorldServer world) {
        String result = "";
        result = world.getChunkSaveLocation().getAbsolutePath();
        return result;
    }
    private boolean hasOnlySea(Chunk tested) {
        byte [] biomes = tested.getBiomeArray();
        for (byte biome: biomes) {
            if (biome == BiomeGenBase.ocean.biomeID) continue;
            if (biome == BiomeGenBase.deepOcean.biomeID) continue;
            return false;
        }
        return true;
    }

    private boolean isSea(BiomeGenBase tested) {
        if (tested.biomeID == BiomeGenBase.ocean.biomeID) return true;
        if (tested.biomeID == BiomeGenBase.deepOcean.biomeID) return true;
        if (tested.biomeID == BiomeGenBase.mushroomIsland.biomeID) return true;
        return false;
    }
}
