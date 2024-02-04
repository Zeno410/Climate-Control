package climateControl;

import climateControl.api.BiomePackageRegistry;
import climateControl.api.BiomeSettings;
import climateControl.api.CCDimensionSettings;
import climateControl.api.ClimateControlSettings;
import climateControl.api.DimensionalSettingsRegistry;
import climateControl.biomeSettings.ArsMagicaPackage;
import climateControl.biomeSettings.BopPackage;
import climateControl.biomeSettings.EBPackage;
import climateControl.biomeSettings.EBXLController;
import climateControl.biomeSettings.ExternalBiomePackage;
import climateControl.biomeSettings.GrowthcraftPackage;
import climateControl.biomeSettings.HighlandsPackage;
import climateControl.biomeSettings.ReikasPackage;
import climateControl.biomeSettings.ThaumcraftPackage;
import climateControl.biomeSettings.VampirismPackage;
import climateControl.customGenLayer.GenLayerRiverMixWrapper;
import climateControl.utils.ConfigManager;
import climateControl.utils.Zeno410Logger;
import java.util.logging.Logger;



import climateControl.utils.Named;
import climateControl.utils.PropertyManager;
import climateControl.utils.TaggedConfigManager;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.World;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.event.terraingen.WorldTypeEvent;


import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.*;
import cpw.mods.fml.common.event.*;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

@Mod(modid = "climatecontrol", name = "Climate Control", version = "0.8.2.1",acceptableRemoteVersions = "*")

public class ClimateControl {
    public static Logger logger = new Zeno410Logger("ClimateControl").logger();
    public static boolean testing = true;

    private Configuration config;
    //private OldClimateControlSettings settings = new OldClimateControlSettings();
    //private OldClimateControlSettings defaultSettings = new OldClimateControlSettings();
    //private OverworldDataStorage storage;

    private ClimateControlSettings newSettings;
    private ConfigManager<ClimateControlSettings> configManager;
    private TaggedConfigManager addonConfigManager;
    private CCDimensionSettings dimensionSettings;

    private HashMap<Integer,WorldServer> servedWorlds = new HashMap<Integer,WorldServer>();
    private GenLayerRiverMixWrapper riverLayerWrapper = new GenLayerRiverMixWrapper(0L);
    //private GenLayer[] vanillaGenerators;
    //private GenLayer[] modifiedGenerators;

    private File configDirectory;
    private File suggestedConfigFile;

    private ExternalBiomePackage externalBiomesPackage;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        addonConfigManager = new TaggedConfigManager("climatecontrol.cfg","ClimateControl");
        BiomePackageRegistry.instance = new BiomePackageRegistry(
                event.getSuggestedConfigurationFile().getParentFile(),addonConfigManager);
        externalBiomesPackage = new ExternalBiomePackage();
        // trying to set it up in ClimateControlSettings instead
        //BiomePackageRegistry.instance.register(externalBiomesPackage);

        DimensionalSettingsRegistry.instance= new DimensionalSettingsRegistry();
        
        newSettings = new ClimateControlSettings();
        suggestedConfigFile = event.getSuggestedConfigurationFile();
        config = new Configuration(suggestedConfigFile);
        config.load();
        //if (this.rescueOldCCMode) defaultSettings.set(config);
        //this.settings = defaultSettings.clone();

        setupRegistry();
        newSettings.readFrom(config);
        
        // settings need to be reset so the mod-specific configs go into the CC file.
        // can't be done first because we don't have a set of biomeSettings yet.

        configDirectory = event.getSuggestedConfigurationFile().getParentFile();
        newSettings.setDefaults(configDirectory);
        logger.info(configDirectory.getAbsolutePath());
        this.dimensionSettings = new CCDimensionSettings();

        configManager = new ConfigManager<ClimateControlSettings>(
                config,newSettings,event.getSuggestedConfigurationFile());
        config.save();

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
        addonConfigManager.initializeConfig(dimensionSettings.named(), configDirectory);
        //try {
            //dimensionManager = new DimensionManager(newSettings,MinecraftServer.getServer());
        //} catch (Exception e) {}
    }

    private DimensionManager dimensionManager;
    @SubscribeEvent(priority=EventPriority.LOWEST)
    public void onWorldLoad(WorldEvent.Load event) {
        DimensionalSettingsRegistry.instance.onWorldLoad(event);
        if (dimensionManager == null) {
            if (MinecraftServer.getServer()!=null)
                dimensionManager = new DimensionManager(newSettings,dimensionSettings,MinecraftServer.getServer());
        }
        if (dimensionManager != null) {
            dimensionManager.onWorldLoad(event.world);
        }
    }

    @SubscribeEvent
    public void onCreateSpawn(WorldEvent.CreateSpawnPosition event) {
        if (dimensionManager == null) {
            if (MinecraftServer.getServer()!=null)
                dimensionManager = new DimensionManager(newSettings,dimensionSettings,MinecraftServer.getServer());
        }
        if (dimensionManager != null) {
            dimensionManager.onCreateSpawn(event);
        } 

    }

    @SubscribeEvent
    public void unloadWorld(WorldEvent.Unload event) {
        DimensionalSettingsRegistry.instance.unloadWorld(event);
        if (event.world instanceof WorldServer) {
            servedWorlds.remove(event.world.provider.dimensionId);
        }
    }
    @EventHandler
    public void serverStarted(FMLServerStartedEvent event) {
        newSettings.setDefaults(configDirectory);
        newSettings.copyTo(config);
        DimensionalSettingsRegistry.instance.serverStarted(event);
        File worldSaveDirectory = null;
        String worldName = MinecraftServer.getServer().getFolderName();
        if (MinecraftServer.getServer().isSinglePlayer()) {
            File saveDirectory = MinecraftServer.getServer().getFile("saves");
            worldSaveDirectory = new File(saveDirectory,worldName);
        } else {
            PropertyManager settings = new PropertyManager(MinecraftServer.getServer().getFile("server.properties"));
            worldName = settings.getProperty("level-name", worldName);
            worldSaveDirectory = MinecraftServer.getServer().getFile(worldName);
        }
        File worldConfigDirectory = new File(worldSaveDirectory,TaggedConfigManager.worldSpecificConfigFileName);
        addonConfigManager.updateConfig(dimensionSettings.named(), configDirectory, worldConfigDirectory);
    }

    @EventHandler
    public void serverStopped(FMLServerStoppedEvent event) {
        this.riverLayerWrapper =new GenLayerRiverMixWrapper(0L);
        this.configManager.clearWorldFile();
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

        File directory = event.getServer().worldServerForDimension(0).getChunkSaveLocation();
        directory = new File(directory,"worldSpecificConfig");
        directory.mkdir();
        if (dimensionManager == null) {
            if (event.getServer()!=null)
                dimensionManager = new DimensionManager(newSettings,dimensionSettings,event.getServer());
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onBiomeGenInit(WorldTypeEvent.InitBiomeGens event) {
        if (dimensionManager == null) {
            if (MinecraftServer.getServer()!= null)
            dimensionManager = new DimensionManager(newSettings,dimensionSettings,MinecraftServer.getServer());
        }
        if (dimensionManager != null) {
            dimensionManager.onBiomeGenInit(event);
        }
    }

    //@SubscribeEvent(priority = EventPriority.HIGHEST)
    public void earlyOnBiomeGenInit(WorldTypeEvent.InitBiomeGens event) {
        if (dimensionManager == null) {
            if (MinecraftServer.getServer()!= null)
            dimensionManager = new DimensionManager(newSettings,dimensionSettings,MinecraftServer.getServer());
        }
        if (dimensionManager != null) {
            dimensionManager.onBiomeGenInit(event);
        }
    }

    public void setupRegistry() {
                try {
            // see if highlands is there
            BiomePackageRegistry.instance.register(new HighlandsPackage());
        } catch (java.lang.NoClassDefFoundError e){
            // Highlands isn't installed
        }
        try {
            // see if BoP is there
            BiomePackageRegistry.instance.register(new BopPackage());
        } catch (java.lang.NoClassDefFoundError e){
            // BoP isn't installed
        }
        try {
            // see if EB is there
            BiomePackageRegistry.instance.register(new EBPackage());
        } catch (java.lang.NoClassDefFoundError e){
            // EB isn't installed
        }
        try {
            // see if thaumcraft is there
            // attach a setting
            BiomePackageRegistry.instance.register(new ThaumcraftPackage());

        } catch (java.lang.NoClassDefFoundError e){
            // thaumcraft isn't installed
        }
        try {
            // see if EBXL is there
            BiomePackageRegistry.instance.register(new EBXLController());

        } catch (java.lang.NoClassDefFoundError e){
            // EBXL isn't installed
        }
        try {
            // see if ChromatiCraft is there
            BiomePackageRegistry.instance.register(new ReikasPackage());

        } catch (java.lang.NoClassDefFoundError e){
            // ChromatiCraft isn't installed
        }
        try {
            // see if ArsMagica is there
            BiomePackageRegistry.instance.register(new ArsMagicaPackage());
        } catch (java.lang.NoClassDefFoundError e){
            // ArsMagica isn't installed
        }
        try {
            // see if Growthcraft is there
            BiomePackageRegistry.instance.register(new GrowthcraftPackage());
        } catch (java.lang.NoClassDefFoundError e){
            // Growthcraft isn't installed
        }

        try {
            // see if Vampirism is there
            BiomePackageRegistry.instance.register(new VampirismPackage());
        } catch (java.lang.NoClassDefFoundError e){
            // Vampirism isn't installed
        }
    }

    public void logBiomes(){
        BiomeGenBase [] biomes = BiomeGenBase.getBiomeGenArray();
        for (BiomeGenBase biome: biomes ) {
            if (biome == null) continue;
            logger.info(biome.biomeName+" "+biome.biomeID+ " temp " +biome.getTempCategory().toString() +
                   " " + biome.getFloatTemperature(0, 64, 0)+ " rain "  + biome.getFloatRainfall());
        }
    }

}
