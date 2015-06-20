
package climateControl.biomeSettings;

import climateControl.api.BiomePackage;
import climateControl.api.BiomeSettings;
import biomesoplenty.api.content.BOPCBiomes;

/**
 *
 * @author Zeno410
 */
public class BopPackage extends BiomePackage {

    public BopPackage() {
        super("biomesoplentyInCC.cfg");
        // confirm Highlands is there.
        Class biomesClass = BOPCBiomes.class;
    }

    @Override
    public BiomeSettings freshBiomeSetting() {
        return new BoPSettings();
    }

}
