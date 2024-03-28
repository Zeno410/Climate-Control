
package com.Zeno410Utils;


import java.io.File;
import java.util.logging.Logger;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.config.Configuration;

/**
 *
 * @author Zeno410
 */

public class TaggedConfigManager<Type extends Settings> {
    private final String modConfigName;
    private final String groupDirectoryName;
    public final static String worldSpecificConfigFileName = "worldSpecificConfig";
    //public static Logger logger = new Zeno410Logger("TaggedConfigManager").logger();

    public TaggedConfigManager(String generalConfigName, String groupDirectoryName) {
        this.modConfigName = generalConfigName;
        this.groupDirectoryName = groupDirectoryName;
    }
    public void updateConfig(Named<Type> namedSettings, File generalDirectory, File specificDirectory) {
        Settings settings = namedSettings.object;
        { // block to localize vars
            File generalModFile = new File(generalDirectory,modConfigName);
            File generalAddOnDirectory = new File(generalDirectory,groupDirectoryName);
            if (!generalAddOnDirectory.exists()) generalAddOnDirectory.mkdir();
            File generalAddonFile = new File(generalAddOnDirectory,namedSettings.name);
            readConfigs(generalModFile,generalAddonFile,settings,generalDirectory);
        }

        File specificModFile = new File(specificDirectory,modConfigName);
        File specificAddOnDirectory = new File(specificDirectory,groupDirectoryName);
        if (!specificAddOnDirectory.exists()) specificAddOnDirectory.mkdir();
        if (!specificAddOnDirectory.exists()) throw new RuntimeException();
        File specificAddonFile = new File(specificAddOnDirectory,namedSettings.name);
        readConfigs(specificModFile,specificAddonFile,settings,generalDirectory);

    }

    public void initializeConfig(Named<Type> namedSettings, File generalDirectory) {
        Settings settings = namedSettings.object;
        File generalModFile = new File(generalDirectory,modConfigName);
        File generalAddOnDirectory = new File(generalDirectory,groupDirectoryName);
        File generalAddonFile = new File(generalAddOnDirectory,namedSettings.name);
        readConfigs(generalModFile,generalAddonFile,settings,generalDirectory);
    }

    private void readConfigs(File generalFile, File specificFile, Settings settings, File generalDirectory) {
        Configuration specific = null;
        try {
            // need to develop an alternative to this forced cast.
            //((BiomeSettings) (settings)).setNativeBiomeIDs(generalDirectory);
            //((BiomeSettings) (settings)).nameDefaultClimates();
        } catch (ClassCastException e) {
            // not a biome settings
        }
        if (specificFile.exists()) {
            specific = new Configuration(specificFile);
            settings.readFrom(specific);
        } else {
            if (generalFile.exists()) {
                settings.readFrom(new Configuration(generalFile));
            }
            specific = new Configuration(specificFile);
            settings.copyTo(specific);
        }
        settings.copyTo(specific);
        specific.save();
    }

    private boolean usable(File tested) {
        return tested != null;
    }

    public void updateConfig(Named<Type> namedSettings, File generalDirectory, WorldServer server) {
        File configDirectory = new File(server.getChunkSaveLocation(),worldSpecificConfigFileName);
        configDirectory.mkdir();
        this.updateConfig(namedSettings, generalDirectory, configDirectory);
    }
}
