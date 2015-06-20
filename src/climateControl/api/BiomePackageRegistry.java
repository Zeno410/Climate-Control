
package climateControl.api;

import climateControl.utils.Named;
import climateControl.utils.TaggedConfigManager;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 *
 * @author Zeno410
 */
public class BiomePackageRegistry {

    public static BiomePackageRegistry instance;

    private HashMap<String,BiomePackage> namedPackages = new HashMap<String,BiomePackage>();
    private ArrayList<Named<BiomeSettings>> settings = new ArrayList<Named<BiomeSettings>>();
    private final File configDirectory;
    private final TaggedConfigManager taggedConfigManager;

    public BiomePackageRegistry(File configDirectory,TaggedConfigManager taggedConfigManager) {
        this.configDirectory= configDirectory;
        this.taggedConfigManager = taggedConfigManager;
    }
    
    public final void register(BiomePackage biomePackage) {
        if (namedPackages.containsKey(new String(biomePackage.configFileName()))) {
            throw new BiomePackageAlreadyRegistered(biomePackage.configFileName());
        }
        namedPackages.put(biomePackage.configFileName(),biomePackage);
        Named<BiomeSettings> namedSettings = biomePackage.namedBiomeSetting();
        namedSettings.object.setNativeBiomeIDs(configDirectory);
        taggedConfigManager.initializeConfig(namedSettings, configDirectory);
        settings.add(namedSettings);
    }

    private final Collection<BiomePackage> packages() {
        return namedPackages.values();
    }

    public final Collection<Named<BiomeSettings>> biomeSettings() {
        return settings;
    }

    public static class BiomePackageAlreadyRegistered extends RuntimeException {
        private final String name;
        public BiomePackageAlreadyRegistered(String name) {
            super("config file name "+name+" is already registered with Climate Control");
            this.name = name;
        }
    }
}
