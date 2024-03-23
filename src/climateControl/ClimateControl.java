package climateControl;

import climateControl.api.BiomePackageRegistry;
import climateControl.api.BiomeSettings;
import climateControl.api.CCDimensionSettings;
import climateControl.api.ClimateControlSettings;
import climateControl.api.DimensionalSettingsRegistry;
import climateControl.biomeSettings.BYGPackage;
import climateControl.biomeSettings.BopPackage;
import climateControl.biomeSettings.ExternalBiomePackage;
import climateControl.biomeSettings.ThaumcraftPackage;
import climateControl.customGenLayer.GenLayerRiverMixWrapper;
import climateControl.utils.BiomeConfigManager;
import com.Zeno410Utils.ConfigManager;
import com.Zeno410Utils.Zeno410Logger;
import java.util.logging.Logger;



import com.Zeno410Utils.Named;
import com.Zeno410Utils.PropertyManager;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.World;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.event.terraingen.WorldTypeEvent;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.*;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.event.*;

import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldType;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;

@Mod(modid = "geographicraft", name = "GeographiCraft", version = "0.9.4",acceptableRemoteVersions = "*", acceptedMinecraftVersions = "[1.12,1.12.2]")

public class ClimateControl {
    public static Logger logger = new Zeno410Logger("ClimateControlLogs").logger();
    public static boolean testing = false;

    //private Configuration config;
    //private OldClimateControlSettings settings = new OldClimateControlSettings();
    //private OldClimateControlSettings defaultSettings = new OldClimateControlSettings();
    //private OverworldDataStorage storage;

    private ClimateControlSettings newSettings;
    //private ConfigManager<ClimateControlSettings> configManager;
    private BiomeConfigManager addonConfigManager;
    private CCDimensionSettings dimensionSettings;

    private HashMap<Integer,WorldServer> servedWorlds = new HashMap<Integer,WorldServer>();
    private Named<ClimateControlSettings> masterSettings;
    //private GenLayer[] vanillaGenerators;
    //private GenLayer[] modifiedGenerators;

    private File configDirectory;
    private File suggestedConfigFile;

    private ExternalBiomePackage externalBiomesPackage;

    public final static String geographicraftFolderName = "GeographiCraft";
    public final static String geographicraftConfigName = "geographicraft.cfg";

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        addonConfigManager = new BiomeConfigManager(geographicraftFolderName);
        BiomePackageRegistry.instance = new BiomePackageRegistry(
                event.getSuggestedConfigurationFile().getParentFile(),addonConfigManager);
        externalBiomesPackage = new ExternalBiomePackage();
        // trying to set it up in ClimateControlSettings instead
        //BiomePackageRegistry.instance.register(externalBiomesPackage);

        DimensionalSettingsRegistry.instance= new DimensionalSettingsRegistry();
        
        newSettings = new ClimateControlSettings(WorldType.DEFAULT);
        masterSettings = Named.from(geographicraftConfigName, newSettings);
        configDirectory = event.getSuggestedConfigurationFile().getParentFile();
        //config = new Configuration(suggestedConfigFile);
        //config.load();
        //if (1>0) throw new RuntimeException(configDirectory.getAbsolutePath());
        addonConfigManager.initializeConfig(masterSettings, configDirectory);

        this.dimensionSettings = new CCDimensionSettings();


    }

    @EventHandler
    public void load(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.TERRAIN_GEN_BUS.register(this);
    }
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) throws Exception {
        setupRegistry();
        // settings need to be reset so the mod-specific configs go into the CC file.
        newSettings.setDefaults(configDirectory);

        addonConfigManager.saveConfigs(configDirectory, configDirectory, masterSettings);
        //newSettings.copyTo(config);
        //config.save();
        //logger.info("biome setting count: "+BiomePackageRegistry.instance.biomeSettings().size());
        for (Named<BiomeSettings> addonSetting: BiomePackageRegistry.instance.biomeSettings()) {
            this.addonConfigManager.initializeConfig(addonSetting, configDirectory);
        }
        addonConfigManager.initializeConfig(dimensionSettings.named(), configDirectory);
        //try {
            //dimensionManager = new DimensionManager(newSettings,MinecraftServer.getServer());
        //} catch (Exception e) {}
    }

    private DimensionManager dimensionManager;
    @SubscribeEvent(priority=EventPriority.LOWEST)
    public void onWorldLoad(WorldEvent.Load event) {
    	if (testing) logger.info("Mod " + Loader.instance().activeModContainer().getModId());
        DimensionalSettingsRegistry.instance.onWorldLoad(event);
        if (dimensionManager == null) {
            MinecraftServer server = event.getWorld().getMinecraftServer();
            if (server!=null)
                dimensionManager = new DimensionManager(masterSettings,dimensionSettings,server);
        }
        if (dimensionManager != null) {
            dimensionManager.onWorldLoad(event.getWorld());
        }
    }

    @SubscribeEvent
    public void onCreateSpawn(WorldEvent.CreateSpawnPosition event) {
        if (dimensionManager == null) {
            MinecraftServer server = event.getWorld().getMinecraftServer();
            if (server!=null)
                dimensionManager = new DimensionManager(masterSettings,dimensionSettings,server);
        }
        if (dimensionManager != null) {
            dimensionManager.onCreateSpawn(event);
        } 

    }

    @SubscribeEvent
    public void unloadWorld(WorldEvent.Unload event) {
        DimensionalSettingsRegistry.instance.unloadWorld(event);
        if (event.getWorld() instanceof WorldServer) {
            servedWorlds.remove(event.getWorld().provider.getDimension());
        }
    }
    @EventHandler
    public void serverStarted(FMLServerStartedEvent event) {
    	
    	logBiomes();
        newSettings.setDefaults(configDirectory);
        addonConfigManager.updateConfig(masterSettings, configDirectory, configDirectory);
        DimensionalSettingsRegistry.instance.serverStarted(event);
        File worldSaveDirectory = null;
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        String worldName = server.getFolderName();
        if (server.isSinglePlayer()) {
            File saveDirectory = server.getFile("saves");
            worldSaveDirectory = new File(saveDirectory,worldName);
        } else {
            PropertyManager settings = new PropertyManager(server.getFile("server.properties"));
            worldName = settings.getProperty("level-name", worldName);
            worldSaveDirectory = server.getFile(worldName);
        }
        File worldConfigDirectory = new File(worldSaveDirectory,BiomeConfigManager.worldSpecificConfigFileName);
        addonConfigManager.updateConfig(dimensionSettings.named(), configDirectory, worldConfigDirectory);
    }

    @EventHandler
    public void serverStopped(FMLServerStoppedEvent event) {

        addonConfigManager.updateConfig(masterSettings, configDirectory, configDirectory);
        for (Named<BiomeSettings> addonSetting: BiomePackageRegistry.instance.biomeSettings()) {
            this.addonConfigManager.initializeConfig(addonSetting, configDirectory);
        }
        addonConfigManager.initializeConfig(dimensionSettings.named(), configDirectory);
        DimensionalSettingsRegistry.instance.serverStopped(event);
        dimensionManager = null;
        //this.riverLayerWrapper.clearRedirection();
    }

    @SubscribeEvent
    public void onWorldSave(WorldEvent.Save event){
        World world = event.getWorld();
        if (world.isRemote) return;
        int dimension = world.provider.getDimension();
        if (dimension != 0) return;
        //storage.onWorldSave(event,this.settings);


    }
    @EventHandler
    public void onServerStarted(FMLServerStartedEvent event) {
    }

    @EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {

        File directory = event.getServer().worlds[0].getChunkSaveLocation();
        directory = new File(directory,"worldSpecificConfig");
        directory.mkdir();
        if (dimensionManager == null) {
            if (event.getServer()!=null)
                dimensionManager = new DimensionManager(masterSettings,dimensionSettings,event.getServer());
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onBiomeGenInit(WorldTypeEvent.InitBiomeGens event) {
        if (dimensionManager == null) {
           MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
            if (server!= null)
            dimensionManager = new DimensionManager(masterSettings,dimensionSettings,server);
        }
        if (dimensionManager != null) {
            dimensionManager.onBiomeGenInit(event);
        }
    }

    public void setupRegistry() {
        try {
            // see if BoP is there biomesoplenty
            BiomePackageRegistry.instance.register(new BopPackage());
        } catch (java.lang.NoClassDefFoundError e){
            // BoP isn't installed
        }
        try {
            // see if Thaumcraft is there thaumcraft
            BiomePackageRegistry.instance.register(new ThaumcraftPackage());
        } catch (java.lang.NoClassDefFoundError e){
            // BoP isn't installed
        }
        if (Loader.isModLoaded("byg")) {

            BiomePackageRegistry.instance.register(new BYGPackage());
        }
    }

    public void logBiomes(){
    	if (!testing) return;
    	for (ModContainer container: Loader.instance().getActiveModList()) {
    		logger.info(container.getModId());
    	}
        Iterator<ResourceLocation> registries = Biome.REGISTRY.getKeys().iterator();
        while (registries.hasNext()) {
        	ResourceLocation location = registries.next();
        	Biome biome = Biome.REGISTRY.getObject(location);
            int number = Biome.getIdForBiome(biome);
            logger.info(biome.getBiomeName()+" "+number+ " temp " +biome.getTempCategory().toString()+ " rain "  + biome.getRainfall());
        }
    }

}
