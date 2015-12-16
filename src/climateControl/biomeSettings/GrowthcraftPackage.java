
package climateControl.biomeSettings;

import climateControl.api.BiomePackage;
import climateControl.api.BiomeSettings;
import growthcraft.bamboo.BiomeGenBamboo;

/**
 *
 * @author Zeno410
 */

public class GrowthcraftPackage extends BiomePackage {

    public GrowthcraftPackage() {
        super("GrowthcraftInCC.cfg");
        // confirm Growthcraft is there.
        Class sampleClass = BiomeGenBamboo.class;
    }

    @Override
    public BiomeSettings freshBiomeSetting() {
        return new GrowthcraftBiomeSettings();
    }

}