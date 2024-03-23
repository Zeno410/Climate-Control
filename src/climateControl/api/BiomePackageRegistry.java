
package climateControl.api;

import climateControl.utils.BiomeConfigManager;
import com.Zeno410Utils.Named;
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
    private ArrayList<BiomePackage> orderedPackages  = new ArrayList<BiomePackage>();
    private ArrayList<Named<BiomeSettings>> settings = new ArrayList<Named<BiomeSettings>>();
    private final File configDirectory;
    private final BiomeConfigManager taggedConfigManager;

    public BiomePackageRegistry(File configDirectory,BiomeConfigManager taggedConfigManager) {
        this.configDirectory= configDirectory;
        this.taggedConfigManager = taggedConfigManager;
    }
    
    public final void register(BiomePackage biomePackage) {
        if (namedPackages.containsKey(new String(biomePackage.configFileName()))) {
            //throw new BiomePackageAlreadyRegistered(biomePackage.configFileName());
            return;
        }
        namedPackages.put(biomePackage.configFileName(),biomePackage);
        orderedPackages.add(biomePackage);
        Named<BiomeSettings> namedSettings = biomePackage.namedBiomeSetting();
        //namedSettings.object.setNativeBiomeIDs(configDirectory);
        //namedSettings.object.nameDefaultClimates();
        taggedConfigManager.initializeConfig(namedSettings, configDirectory);
        settings.add(namedSettings);
    }

    public final Collection<Named<BiomeSettings>> biomeSettings() {
        return settings;
    }

    public final Collection<Named<BiomeSettings>> freshBiomeSettings() {
        Collection<Named<BiomeSettings>> result = new ArrayList<Named<BiomeSettings>>();
        for (BiomePackage biomePackage: orderedPackages) {
            result.add(biomePackage.namedBiomeSetting());
        }
        return result;
    }
    
    public static class BiomePackageAlreadyRegistered extends RuntimeException {
        private final String name;
        public BiomePackageAlreadyRegistered(String name) {
            super("config file name "+name+" is already registered with Climate Control");
            this.name = name;
        }
    }
}
