
package climateControl.api;

import com.Zeno410Utils.Named;

/**
 *
 * @author Zeno410
 */
public abstract class BiomePackage {
    private final String configFileName;

    public BiomePackage(String configFileName) {
        this.configFileName = configFileName;
    }

    public String configFileName() {
        return configFileName;
    }
    public abstract BiomeSettings freshBiomeSetting();

    public Named<BiomeSettings> namedBiomeSetting() {
        return Named.from(configFileName, freshBiomeSetting());
    }
}
