
package climateControl.biomeSettings;

import climateControl.api.BiomePackage;
import climateControl.api.BiomeSettings;
import enhancedbiomes.EnhancedBiomesMod;

/**
 *
 * @author Zeno410
 */
public class EBPackage extends BiomePackage {

    public EBPackage() {
        super("EnhancedBiomesInCC.cfg");
        // confirm EB is there.
        Class EBModClass = EnhancedBiomesMod.class;
    }

    @Override
    public BiomeSettings freshBiomeSetting() {
        return new EBBiomeSettings();
    }

}
