
package climateControl;

import com.Zeno410Utils.Accessor;
import com.Zeno410Utils.Named;
import climateControl.api.BiomeSettings;
import climateControl.api.CCDimensionSettings;
import climateControl.api.ClimateControlSettings;
import climateControl.api.DimensionalSettingsRegistry;
import climateControl.customGenLayer.GenLayerLowlandRiverMix;
import climateControl.customGenLayer.GenLayerRiverMixWrapper;
import climateControl.genLayerPack.GenLayerPack;
import climateControl.generator.CorrectedContinentsGenerator;
import climateControl.generator.OneSixCompatibleGenerator;
import climateControl.generator.TestGeneratorPair;
import climateControl.generator.VanillaCompatibleGenerator;
import climateControl.utils.BiomeConfigManager;
import com.Zeno410Utils.Zeno410Logger;
import com.Zeno410Utils.ChunkGeneratorExtractor;
import com.Zeno410Utils.ChunkLister;
import com.Zeno410Utils.Maybe;
import com.Zeno410Utils.PlaneLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.logging.Logger;
import net.minecraft.init.Biomes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraft.world.gen.feature.WorldGeneratorBonusChest;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerRiverMix;
import net.minecraft.world.gen.layer.IntCache;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.util.math.BlockPos;
//import net.minecraftforge.common.config.Configuration;
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
    private final Named<ClimateControlSettings> masterSettings;
    //private GenLayerUpdater genLayerUpdater = new GenLayerUpdater();

    public DimensionManager(Named<ClimateControlSettings> masterSettings,CCDimensionSettings dimensionSettings,MinecraftServer server) {
        this.masterSettings = masterSettings;
        this.newSettings = masterSettings.object;
        this.dimensionSettings = dimensionSettings;
        if (server == null) {
            this.configDirectory= null;
            this.suggestedConfigFile = null;
            return;
        }
        this.configDirectory= server.getFile("config");
        this.suggestedConfigFile = new File(configDirectory,"geographicraft.cfg");
    }
    
    private GenLayerRiverMix patchedGenLayer(ClimateControlSettings settings,
            WorldType worldType,
            long worldSeed) {

        //logger.info("patching GenLayer: world seed "+worldSeed+"world type "+worldType.getWorldTypeName());
        for (BiomeSettings biomeSettings: settings.biomeSettings()) {
            //biomeSettings.report();
        }
        if (ignore(worldType,settings)) return null;
        if (settings.noGenerationChanges.value()) {
            if (settings.oneSixCompatibility.value()) {
                return new OneSixCompatibleGenerator(settings).fromSeed(worldSeed,worldType);
            } else {
                return null;
            }
        }
        // check settings
        new SettingsTester().test(settings);
        GenLayerRiverMix newMix = null;
        //logger.info("world seed " + worldSeed);
        if (settings.vanillaLandAndClimate.value()) {
             newMix = new VanillaCompatibleGenerator(settings).fromSeed(worldSeed,worldType);
        } else {
             newMix = new CorrectedContinentsGenerator(settings,this.configDirectory.getParentFile()).fromSeed(worldSeed,worldType);
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

    private final File suggestedConfigFile;
    private final File configDirectory;
    
    private BiomeConfigManager addonConfigManager =
            new BiomeConfigManager("GeographiCraft");

    private ClimateControlSettings defaultSettings(MinecraftFilesAccess dimension, boolean newWorld, WorldType worldType) {
        ClimateControlSettings result = defaultSettings(newWorld, worldType);
        //logger.info(dimension.baseDirectory().getAbsolutePath());
        if (!dimension.baseDirectory().exists()) {
            dimension.baseDirectory().mkdir();
            if (!dimension.baseDirectory().exists()) {
            }
        }
        Named<ClimateControlSettings> dimensionSetting = Named.from(masterSettings.name, result);
        addonConfigManager.updateConfig(dimensionSetting, configDirectory, dimension.configDirectory());
        /*Configuration workingConfig = new Configuration(suggestedConfigFile);
        workingConfig.load();
        ConfigManager<ClimateControlSettings> workingConfigManager = new ConfigManager<ClimateControlSettings>(
        workingConfig,result,suggestedConfigFile);
        workingConfigManager.setWorldFile(dimension.baseDirectory());
        workingConfigManager.saveWorldSpecific();*/

        addonConfigManager.saveConfigs(configDirectory, dimension.configDirectory(), dimensionSetting);

        for (Named<BiomeSettings> addonSetting: result.registeredBiomeSettings()) {
            addonConfigManager.initializeConfig(addonSetting, configDirectory);
            addonConfigManager.updateConfig(addonSetting, configDirectory, dimension.configDirectory());
            if (newWorld) {
                addonSetting.object.onNewWorld();
                addonConfigManager.saveConfigs(this.configDirectory, dimension.configDirectory(), addonSetting);
            }
        }
        return result;
    }

    private ClimateControlSettings defaultSettings(boolean newWorld, WorldType worldType) {
        ClimateControlSettings result = new ClimateControlSettings(worldType);
        Named<ClimateControlSettings> namedResult = Named.from(ClimateControl.geographicraftConfigName, result);
        addonConfigManager.initializeConfig(namedResult, configDirectory);
        result.setDefaults(configDirectory);
        for (Named<BiomeSettings> addonSetting: result.registeredBiomeSettings()) {
            if (newWorld) addonSetting.object.onNewWorld();
            addonConfigManager.initializeConfig(addonSetting, configDirectory);
            if (newWorld) addonSetting.object.onNewWorld();
            addonSetting.object.setNativeBiomeIDs(configDirectory);
        }
        return result;
    }

    private ClimateControlSettings dimensionalSettings(DimensionAccess dimension, boolean newWorld, WorldType worldType) {
        ClimateControlSettings result = dimensionalSettings.get(dimension.dimension);
        if (result == null) {
            result = defaultSettings(dimension,newWorld,worldType);
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

        ClimateControlSettings generationSettings = defaultSettings(true,event.getWorldType());
        // this only gets used for new worlds,
        //when WorldServer is initializing and there are no spawn chunks yet
        generationSettings.onNewWorld();
        if (this.ignore(event.getWorldType(),this.newSettings)){
            return;
        }
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        if (server== null) {
            original = event.getOriginalBiomeGens()[0];
            // maybe no longer necessary
            return;
        }

        // get overworld dimension;
        boolean newWorld = true;
        for (WorldServer worldServer: server.worlds){
            if (worldServer.getTotalWorldTime()>0 ) newWorld = false;
        }
        //logger.info(worldType.getTranslateName());
        //logger.info(event.originalBiomeGens[0].toString());
        // if not a recognized world type ignore
        //logBiomes();
        //this.activeRiverMix = (GenLayerRiverMix)(event.originalBiomeGens[0]);

        original = event.getOriginalBiomeGens()[0];
        GenLayerRiverMixWrapper result = new GenLayerRiverMixWrapper(event.getSeed(),original,this);

        if (generationSettings.noGenerationChanges.value()) {
            event.setNewBiomeGens(result.modifiedGenerators()) ;
            event.setResult(WorldTypeEvent.Result.ALLOW);
            return;
        } //implied else
        if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
            GenLayerRiverMix patched = patchedGenLayer(generationSettings, event.getWorldType(), event.getSeed());
            //riverLayerWrapper(0).setRedirection(patched);
            event.setNewBiomeGens(result.modifiedGenerators());
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
        if (!settings.suppressInStandardWorlds.value()) {
            if (considered.equals(WorldType.AMPLIFIED)) return false;
            if (considered.equals(WorldType.DEFAULT)) {
                return false;
            }
            if (considered.equals(WorldType.DEFAULT_1_1)) return false;
            if (considered.equals(WorldType.LARGE_BIOMES)) return false;
        }
        if (settings.interveneInCustomizedWorlds.value()) {
            if (considered.equals(WorldType.CUSTOMIZED)) return false;
        }
        if (considered.equals(WorldType.FLAT)) return true;
        if (considered.getName().equalsIgnoreCase("TerrainControl")) return false;
        if (settings.interveneInBOPWorlds.value()) {
            if (considered.getName().equalsIgnoreCase("BIOMESOP")) return false;
        }
        if (settings.interveneInHighlandsWorlds.value()) {

            if (considered.getName().equalsIgnoreCase("Highlands")) return false;
            if (considered.getName().equalsIgnoreCase("HighlandsLB")) return false;
        }
        if (true) {
            if (considered.getName().equalsIgnoreCase("FWG")) return false;
        }

        if (considered.getName().equalsIgnoreCase("RTG")) return false;
        return true;
    }

    public void onCreateSpawn(WorldEvent.CreateSpawnPosition event) {
        WorldServer world = (WorldServer)(event.getWorld());
        if (ignore(world.getWorldType(),this.newSettings)) {
            return;
        }
        int dimension = world.provider.getDimension();
        if (!this.dimensionSettings.ccOnIn(dimension)) {
            if (!DimensionalSettingsRegistry.instance.useCCIn(dimension)) {
                return;
            }
        }// only change dimensions we're supposed to;
        onWorldLoad(event.getWorld());
        salvageSpawn(event.getWorld());
        if (event.getSettings().isBonusChestEnabled()) {
            Random rand = new Random(world.getSeed());
            WorldGeneratorBonusChest worldgeneratorbonuschest =new WorldGeneratorBonusChest();

            for (int i = 0; i < 100; ++i){
                int j = world.getWorldInfo().getSpawnX() + rand.nextInt(6+i/10) - rand.nextInt(6+i/10);
                int k = world.getWorldInfo().getSpawnZ() + rand.nextInt(6+i/10) - rand.nextInt(6+i/10);

                BlockPos topBlockSpot = new BlockPos(j,world.getActualHeight()-1,k);
                while (!world.getBlockState(topBlockSpot).isSideSolid(world, topBlockSpot, EnumFacing.UP)) {
                    topBlockSpot = topBlockSpot.down();
                }
                BlockPos above = topBlockSpot.up();

                if (world.getBlockState(above).getBlock().isAir(world.getBlockState(above),world, above)){
                    if (worldgeneratorbonuschest.generate(world, rand, above)) break;
                }
            }
        }
        event.setCanceled(true);
    }

    private HashSet<Integer> dimensionsDone = new HashSet<Integer>();

    public void onWorldLoad(World world) {
        // functionalty moved
    }

    public Maybe<GenLayerRiverMix> getGeographicraftGenlayers(WorldServer world, int dimension, GenLayer original) {
        // returns the geographicraft layers if appropriate. 
        // If not and chunk wall prevention is on, installs chunk layer prevention into the originals

        if (ignore(world.getWorldType(),this.newSettings)) {
            return Maybe.unknown();
        }
        
        if (!this.dimensionSettings.ccOnIn(dimension)) {
            if (!DimensionalSettingsRegistry.instance.useCCIn(dimension)) {
                return Maybe.unknown();
            }
        }
        DimensionAccess dimensionAccess = new DimensionAccess(dimension,world);

        long worldSeed = world.getSeed();
        if (world instanceof WorldServer&&worldSeed!=0)  {
            ClimateControlSettings currentSettings = null;
            boolean newWorld = false;
            //logger.info("time "+world.getWorldInfo().getWorldTotalTime());
            if(world.getWorldInfo().getWorldTotalTime()<100) {
                // new world
                newWorld = true;
            }
            currentSettings = dimensionalSettings(dimensionAccess,newWorld,world.getWorldType());
            //logger.info(""+dimension +" "+ currentSettings.snowyIncidence.value() +" "+ currentSettings.coolIncidence.value());

            try {
                GenLayerRiverMix patched = patchedGenLayer(currentSettings,world.getWorldType(),worldSeed);
                if (patched != null) {
                    //genLayerUpdater.update(this.riverLayerWrapper(dimension), world.provider);
                    
                    // locking has to get a "normal" GenLayer with a "normal" parent
                        Accessor<GenLayerRiverMix,GenLayer> riverMixBiome =
                        new Accessor<GenLayerRiverMix,GenLayer>("field_75910_b");
                        GenLayer lockable = riverMixBiome.get((GenLayerRiverMix)patched);
                    new LockGenLayers().lock(lockable, dimension, world, currentSettings);
                    
                    return Maybe.definitely(patched);
                } else {
                    // lock manually
                    LockGenLayers biomeLocker = new LockGenLayers();
                        Accessor<GenLayerRiverMix,GenLayer> riverMixBiome =
                        new Accessor<GenLayerRiverMix,GenLayer>("field_75910_b");
                        original = riverMixBiome.get((GenLayerRiverMix)original);
                    biomeLocker.lock(original, dimension, world, currentSettings);
                    return Maybe.unknown();
                }
            } catch (Exception e) {
                logger.info(e.toString());
                logger.info(e.getMessage());
                return Maybe.unknown();
            } catch (Error e) {
                logger.info(e.toString());
                logger.info(e.getMessage());
                return Maybe.unknown();
            }
            //logger.info("start rescued");
        }
        return Maybe.unknown();
    }
    
    private String controllingGenLayer(World world) {
        BiomeProvider chunkManager = world.getBiomeProvider();

        Accessor<BiomeProvider,GenLayer> worldGenLayer =
            new Accessor<BiomeProvider,GenLayer>("field_76944_d");

        return worldGenLayer.get(chunkManager).toString();

    }
   PlaneLocation lastTry = new PlaneLocation(Integer.MIN_VALUE,Integer.MIN_VALUE);


    private void salvageSpawn(World world) {
        WorldInfo info = world.getWorldInfo();
        int x= info.getSpawnX()/16*16 + this.newSettings().xSpawnOffset.value();
        int z= info.getSpawnZ()/16*16 + this.newSettings().zSpawnOffset.value();
        //x = x/16;
        //z = z/16;
        int move = 0;
        int ring = 0;
        int xMove = 0;
        int zMove = 0;
        int spawnX = 0;
        int spawnZ = 0;
        int spawnY = 0;
        Biome checkSpawn = world.getBiomeForCoordsBody(new BlockPos(x,64,z));
        int nextTry = 50;
        int nextTryIncrement = 80;
        int nextTryStretch = 20;
        IChunkProvider chunkManager = world.getChunkProvider();
        ChunkProviderServer chunkServer = null;
        try {
            chunkServer = (ChunkProviderServer)chunkManager;
        } catch (ClassCastException e) {
            throw e;
        }
        int checked = 0;
        int rescueTries = 0;
        while(spawnY < 64) {
            if (newSettings.rescueSearchLimit.value() == rescueTries++) {
                // limit reached, give up and quit
                return;
            }
            //while (isSea(checkSpawn)) {
                //spiral out around spawn;
            if (checked > 50) {
                if (chunkServer != null) {
                    chunkServer.queueUnloadAll();
                    chunkServer.chunkLoader.flush();
                    //chunkServer.unloadQueuedChunks();
                }
                checked = 0;
            }
            if (chunkServer != null) {
                //chunkServer.unloadChunksIfNotNearSpawn(checked, checked);
            }
            checked++;
                move ++;
                if (move>((ring)*(ring+1)*4)) {
                    ring ++;
                }
                // do 1 in 1000
                if (move<nextTry) continue;
                nextTryIncrement += nextTryStretch++;
                nextTry += nextTryIncrement;
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
                        + " ring "+ ring + " inRing " + inRing+ " caches " + IntCache.getCacheSizes()
                        + " dimension " + world.provider.getDimension());
                checkSpawn = world.getBiomeForCoordsBody(new BlockPos(x+xMove*16, 64, z+zMove*16));
            //}
            //int spawnY = checkSpawn.getHeightValue(8,8);
            spawnX = x+xMove*16;
            spawnZ = z+zMove*16;
            logger.info("setting spawn at "+ spawnX + ","+ spawnZ);
            if (checkSpawn == Biomes.MUSHROOM_ISLAND) continue;
            spawnY = world.getTopSolidOrLiquidBlock(new BlockPos(spawnX, 64, spawnZ)).getY()+1;
            PlaneLocation newLocation = new PlaneLocation(spawnX,spawnZ);
            if (newLocation.equals(lastTry)) break;
        }
        world.setSpawnPoint(new BlockPos(spawnX,spawnY,spawnZ));
    }

    private String levelSavePath(WorldServer world) {
        String result = "";
        result = world.getChunkSaveLocation().getAbsolutePath();
        return result;
    }
    private boolean hasOnlySea(Chunk tested) {
        byte [] biomes = tested.getBiomeArray();
        for (byte biome: biomes) {
            if (biome == 0) continue;
            if (biome == Biome.getIdForBiome(Biomes.DEEP_OCEAN)) continue;
            return false;
        }
        return true;
    }

    private boolean isSea(Biome tested) {
        if (tested == Biomes.OCEAN) return true;
        if (tested == Biomes.DEEP_OCEAN) return true;
        if (tested == Biomes.MUSHROOM_ISLAND) return true;
        return false;
    }
}
