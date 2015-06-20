
package climateControl.biomeSettings;

import climateControl.api.BiomePackage;
import climateControl.api.BiomeSettings;
import highlands.api.HighlandsBiomes;

/**
 *
 * @author Zeno410
 */
public class HighlandsPackage extends BiomePackage {

    public HighlandsPackage() {
        super("HighlandsInCC.cfg");
        // confirm Highlands is there.
        Class highlandsBiomesClass = HighlandsBiomes.class;
    }

    @Override
    public BiomeSettings freshBiomeSetting() {
        return new HighlandsBiomeSettings();
    }

}
