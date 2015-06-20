package climateControl;

import climateControl.api.BiomePackageRegistry;
import climateControl.api.BiomeSettings;
import climateControl.api.ClimateControlSettings;
import climateControl.customGenLayer.GenLayerLowlandRiverMix;
import climateControl.customGenLayer.GenLayerRiverMixWrapper;
import climateControl.generator.StackedContinentsGenerator;
import climateControl.utils.Accessor;
import climateControl.utils.ConfigManager;
import climateControl.utils.PlaneLocation;
import climateControl.utils.Zeno410Logger;
import java.util.logging.Logger;


import climateControl.genLayerPack.*;
import climateControl.generator.CorrectedContinentsGenerator;
import climateControl.generator.MapGenVillageCC;
import climateControl.generator.OneSixCompatibleGenerator;
import climateControl.generator.TestGeneratorPair;
import climateControl.generator.VanillaCompatibleGenerator;
import climateControl.utils.ChunkGeneratorExtractor;
import climateControl.utils.ChunkLister;
import climateControl.utils.Named;
import climateControl.utils.TaggedConfigManager;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.layer.GenLayerRiverMix;
import net.minecraft.world.gen.layer.IntCache;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.WorldType;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.event.terraingen.WorldTypeEvent;


import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.*;
import cpw.mods.fml.common.event.*;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import java.io.File;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.WorldServer;

@Mod(modid = "climatecontrol", name = "Climate Control", version = "0.4")

public class ClimateControl {
    public static Logger logger = new Zeno410Logger("ClimateControl").logger();
    public static boolean testing = true;

    private Configuration config;
    //private OldClimateControlSettings settings = new OldClimateControlSettings();
    //private OldClimateControlSettings defaultSettings = new OldClimateControlSettings();
    //private OverworldDataStorage storage;
    private WorldType worldType;
    private long worldSeed;
    private boolean rescueOldCCMode = false;

    private ClimateControlSettings newSettings;
    public ClimateControlSettings newSettings() {return newSettings;}
    private ConfigManager<ClimateControlSettings> configManager;
    private TaggedConfigManager addonConfigManager;


    private Accessor<GenLayerPack,GenLayerPack> genLayerParent =
            new Accessor<GenLayerPack,GenLayerPack>("field_75909_a");

    private Accessor<GenLayerRiverMix,GenLayerPack> riverMixBiome =
            new Accessor<GenLayerRiverMix,GenLayerPack>("field_75910_b");

    private Accessor<GenLayerRiverMix,GenLayerPack> riverMixRiver =
            new Accessor<GenLayerRiverMix,GenLayerPack>("field_75911_c");

    private Accessor<WorldChunkManager,GenLayer> accessGenLayer =
            new Accessor<WorldChunkManager,GenLayer>("field_76944_d");

    private Accessor<WorldChunkManager,GenLayer> accessBiomeIndex =
            new Accessor<WorldChunkManager,GenLayer>("field_76945_e");


    private HashMap<Integer,WorldServer> servedWorlds = new HashMap<Integer,WorldServer>();
    private GenLayerRiverMixWrapper riverLayerWrapper = new GenLayerRiverMixWrapper(0L);
    //private GenLayer[] vanillaGenerators;
    //private GenLayer[] modifiedGenerators;

    private File configDirectory;
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        addonConfigManager = new TaggedConfigManager("climatecontrol.cfg","ClimateControl");
        BiomePackageRegistry.instance = new BiomePackageRegistry(
                event.getSuggestedConfigurationFile().getParentFile(),addonConfigManager);
        
        newSettings = new ClimateControlSettings();
        config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();
        //if (this.rescueOldCCMode) defaultSettings.set(config);
        //this.settings = defaultSettings.clone();

        newSettings.readFrom(config);
        
        // settings need to be reset so the mod-specific configs go into the CC file.
        // can't be done first because we don't have a set of biomeSettings yet.
        newSettings.setDefaults(configDirectory);
        newSettings.readFrom(config);

        configDirectory = event.getSuggestedConfigurationFile().getParentFile();
        newSettings.setDefaults(configDirectory);
        logger.info(configDirectory.getAbsolutePath());

        configManager = new ConfigManager<ClimateControlSettings>(
                config,newSettings,event.getSuggestedConfigurationFile());

        config.save();
        /*Acceptor<OldClimateControlSettings> acceptor = new Acceptor<OldClimateControlSettings>() {
            public void accept(OldClimateControlSettings accepted) {
                ClimateControl.this.settings = accepted;
                patchGenLayer();
            }
        };*/
        
        /*storage = new OverworldDataStorage(
                "CaveControlSettings",
                this.settings,
                new Default.Self(this.defaultSettings),
                acceptor);*/
    }

    private void resetGeneralConfigs() {
        newSettings.readFrom(config);
    }
    @EventHandler
    public void load(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.TERRAIN_GEN_BUS.register(this);
    }
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) throws Exception {
        newSettings.setDefaults(configDirectory);
        newSettings.copyTo(config);
        config.save();
        logger.info("biome setting count: "+BiomePackageRegistry.instance.biomeSettings().size());
        for (Named<BiomeSettings> addonSetting: BiomePackageRegistry.instance.biomeSettings()) {
            this.addonConfigManager.initializeConfig(addonSetting, configDirectory);
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        if (ignore(event.world.provider.terrainType)) {
            return;
        }
        logger.info(BiomeGenBase.getBiomeGenArray()[2].biomeName+ " present");
        World world = event.world;
        int dimension = world.provider.dimensionId;
        logger.info("Loading dimension "+dimension + " " +world.toString());
        if (dimension != 0) return; // only set Overworld
        if (world instanceof WorldServer) {
            this.servedWorlds.put(dimension, (WorldServer)world);
            WorldServer server = (WorldServer)world;
            logger.info("Managing configs");
            configManager.setWorldFile(server);
            this.newSettings.setDefaults(configDirectory);
            configManager.saveWorldSpecific();
            for (Named<BiomeSettings> addonSetting: BiomePackageRegistry.instance.biomeSettings()) {
               this.addonConfigManager.updateConfig(addonSetting, configDirectory, server);

            }
        }
        if (world.isRemote) {
            return;
        }
        // pull out the Chunk Generator
        // this whole business will crash if things aren't right. Probably the best behavior,
        // although a polite message might be appropriate

        if (world instanceof WorldServer) {
            if (this.rescueOldCCMode) {
                //storage.onWorldLoad(event);
            }
        }
        //logger.info("last Time played"+world.getWorldInfo().getLastTimePlayed());
        //logger.info("total time played"+world.getWorldInfo().getWorldTotalTime());
        // salvage spawn if new world

        this.worldSeed = world.getSeed();
        if (world instanceof WorldServer&&this.worldSeed!=0)  {
            logger.info("patching genlayers with seed"+worldSeed);
            this.patchGenLayer();
            LockGenLayer.logger.info(world.toString());
            this.riverLayerWrapper.lock(dimension, world,newSettings);
        } else {
            //this.accessGenLayer.setField(world.provider.worldChunkMgr, this.vanillaGenerators[0]);
            //this.accessBiomeIndex.setField(world.provider.worldChunkMgr, this.vanillaGenerators[1]);
            //this.accessGenLayer.setField(world.provider.worldChunkMgr, this.modifiedGenerators[0]);
            //this.accessBiomeIndex.setField(world.provider.worldChunkMgr, this.modifiedGenerators[1]);
            this.accessGenLayer.setField(world.provider.worldChunkMgr, this.riverLayerWrapper);
            this.accessBiomeIndex.setField(world.provider.worldChunkMgr, this.riverLayerWrapper.voronoi());
            LockGenLayer.logger.info(world.toString());
            if (!(world instanceof WorldClient)) {
                throw new RuntimeException();
                //logger.info("locking "+riverLayerWrapper.forLocking().toString());
                //this.biomeLocker.lock(riverMixBiome.get(activeRiverMix), dimension, this.servedWorlds.get(dimension),newSettings);
            } else {
                logger.info("client world");
            }
            //this.riverMixBiome.setField(this.vanillaGenLayer,replacement);
            //this.riverMixRiver.setField(this.vanillaGenLayer,replacement);

        }
        //this.biomeLocker.showGenLayers(accessGenLayer.get(world.provider.worldChunkMgr));
        
        if (world instanceof WorldServer) {
            if (newSettings.vanillaLandAndClimate.value() == false){
                if (newSettings.noGenerationChanges.value() == false) {
                    logger.info(new ChunkGeneratorExtractor().extractFrom((WorldServer)world).toString());
                    new ChunkGeneratorExtractor().impose((WorldServer)world, new MapGenVillage());
                    if(world.getWorldInfo().getWorldTotalTime()<10) {
                        ArrayList<PlaneLocation> existingChunks =
                        new ChunkLister().savedChunks(levelSavePath((WorldServer)world));
                        logger.info("existing chunks:"+existingChunks.size()); 
                        //world.provider.worldChunkMgr.
                        salvageSpawn(world);
                    }
                }
            }
        } else {
            
        }
        logger.info(BiomeGenBase.getBiomeGenArray()[2].biomeName+ " present");
        logger.info(BiomeGenBase.getBiomeGenArray()[14].biomeName+ " present");
    }

    private String levelSavePath(WorldServer world) {
        String result = "";
        result = world.getChunkSaveLocation().getAbsolutePath();
        return result;
    }

    @SubscribeEvent
    public void unloadWorld(WorldEvent.Unload event) {
        if (event.world instanceof WorldServer) {
            servedWorlds.remove(event.world.provider.dimensionId);
        }
    }
    @EventHandler
    public void serverStarted(FMLServerStartedEvent event) {
        newSettings.setDefaults(configDirectory);
        newSettings.copyTo(config);
        config.save();
    }

    @EventHandler
    public void serverStopped(FMLServerStoppedEvent event) {
        this.riverLayerWrapper =new GenLayerRiverMixWrapper(0L);
        this.configManager.clearWorldFile();
        for (Named<BiomeSettings> addonSetting: BiomePackageRegistry.instance.biomeSettings()) {
            this.addonConfigManager.initializeConfig(addonSetting, configDirectory);
        }
        //this.riverLayerWrapper.clearRedirection();
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

    @SubscribeEvent
    public void onWorldSave(WorldEvent.Save event){
        World world = event.world;
        if (world.isRemote) return;
        int dimension = world.provider.dimensionId;
        if (dimension != 0) return;
        //storage.onWorldSave(event,this.settings);


    }
    @EventHandler
    public void onServerStarted(FMLServerStartedEvent event) {
    }

    @EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        logger.info("starting server");
    }
    
    private static Accessor<MapStorage,ISaveHandler> saveHandlerAccess =
            new Accessor<MapStorage,ISaveHandler>("field_75751_a");

    private static Accessor<MapStorage,Map> loadedMapAccess =
            new Accessor<MapStorage,Map>("field_75749_b");

    private static Accessor<MapStorage,List> loadedListAccess =
            new Accessor<MapStorage,List>("field_75750_c");

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onBiomeGenInit(WorldTypeEvent.InitBiomeGens event) {
        /* a bit of a dance here. When this is called to setup the biome generators
         * the world hasn't been loaded and we don't have the settings saved for the world
         * So we save the info from the event call for use when that comes in
         * In addition we have already handed back a GenLayer sequence and it's hard to
         * change what's been saved. So we will actually go into the GenLayer sequence and replace the
         * first parent with the first parent of the "correct" GenLayer sequence.
         * The top GenLayer is always the same so it doesn't make any difference.
        */
        // skip if ignoring

        logger.info("initializing biome gen");
        if (this.ignore(event.worldType)) return;
        MinecraftServer server = MinecraftServer.getServer();
        if (server== null) return;
        // get overworld dimension;
        boolean newWorld = true;
        for (WorldServer worldServer: server.worldServers){
            if (worldServer.getTotalWorldTime()>0 ) newWorld = false;
        }
        // set to defaults in case of new world
        if (newWorld) this.resetGeneralConfigs();
        this.worldType = event.worldType;
        //logger.info(worldType.getTranslateName());
        //logger.info(event.originalBiomeGens[0].toString());
        // if not a recognized world type ignore
        //logBiomes();
        //this.activeRiverMix = (GenLayerRiverMix)(event.originalBiomeGens[0]);

        this.worldSeed = event.seed;
        this.riverLayerWrapper.setOriginal((event.originalBiomeGens[0]));
        if (newSettings.noGenerationChanges.value()) {
            this.riverLayerWrapper.setRedirection(((GenLayerRiverMix)(event.originalBiomeGens[0])));
            event.newBiomeGens = this.riverLayerWrapper.modifiedGenerators();
            return;
        } else {
            
        }
        if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
            this.patchGenLayer();
            event.newBiomeGens = this.riverLayerWrapper.modifiedGenerators();
            event.setResult(WorldTypeEvent.Result.ALLOW);

        } else {
            event.newBiomeGens = this.riverLayerWrapper.modifiedGenerators();;
        }
        logger.info("passed through");
        //boobytrap
        //this.riverMixBiome.setField(this.vanillaGenLayer,null);
        
    }

    public boolean ignore(WorldType considered) {
        if (considered == null) return true;
        if (considered.equals(WorldType.AMPLIFIED)) return false;
        if (considered.equals(WorldType.DEFAULT)) {
            logger.info("not ignoring");
            return false;
        }
        if (considered.equals(WorldType.DEFAULT_1_1)) return false;
        if (considered.equals(WorldType.LARGE_BIOMES)) return false;
        if (considered.equals(WorldType.FLAT)) return true;
        if (this.newSettings().interveneInBOPWorlds.value()) {
            if (considered.getWorldTypeName().equalsIgnoreCase("BIOMESOP")) return false;
        }
        if (this.newSettings().interveneInHighlandsWorlds.value()) {

            if (considered.getWorldTypeName().equalsIgnoreCase("Highlands")) return false;
            if (considered.getWorldTypeName().equalsIgnoreCase("HighlandsLB")) return false;
        }
        return true;
    }

    public void logBiomes(){
        BiomeGenBase [] biomes = BiomeGenBase.getBiomeGenArray();
        for (BiomeGenBase biome: biomes ) {
            if (biome == null) continue;
            logger.info(biome.biomeName+" "+biome.biomeID+ " temp " +biome.getTempCategory().toString() +
                   " " + biome.getFloatTemperature(0, 64, 0)+ " rain "  + biome.getFloatRainfall());
        }
    }
    public void patchGenLayer() {
        
        for (BiomeSettings biomeSettings: newSettings.biomeSettings()) {
            //biomeSettings.report();
        }
        if (ignore(worldType)) return;
        if (newSettings.noGenerationChanges.value()) {
            if (newSettings.oneSixCompatibility.value()) {
                this.riverLayerWrapper.setRedirection(
                        ((GenLayerRiverMix)(new OneSixCompatibleGenerator(newSettings).fromSeed(worldSeed))));
            } else {
                this.riverLayerWrapper.useOriginal();
            }
            return;
        }
        GenLayerRiverMix newMix = null;
        logger.info("world seed " + worldSeed);
        if (newSettings.vanillaLandAndClimate.value()) {
             newMix = new VanillaCompatibleGenerator(newSettings).fromSeed(worldSeed);
             this.riverLayerWrapper.setRedirection(newMix);
        } else {
            if (newSettings.zeroPointFive.value()) {
                newMix = new CorrectedContinentsGenerator(newSettings).fromSeed(worldSeed);
                this.riverLayerWrapper.setRedirection(newMix);
            } else {
                StackedContinentsGenerator generator = new StackedContinentsGenerator(newSettings);
                newMix = generator.fromSeed(worldSeed);
            this.riverLayerWrapper.setRedirection(newMix);
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
        logger.info("newMix "+newMix.toString());
        if (newMix instanceof GenLayerLowlandRiverMix) {
            ((GenLayerLowlandRiverMix)newMix).setMaxChasm(this.newSettings.maxRiverChasm.value().floatValue());
        }
        for (BiomeSettings biomeSettings: newSettings.biomeSettings()) {
            //biomeSettings.report();
        }
        BiomeRandomizer.instance.set(newSettings.biomeSettings());
    }
}
