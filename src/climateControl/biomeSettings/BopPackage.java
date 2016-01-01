
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
        try {
            int throwaway = BOPCBiomes.alps.biomeID;
        } catch (NullPointerException e) {
            //not yet set is fine, this is testing for the field
        }
    }

    @Override
    public BiomeSettings freshBiomeSetting() {
        return new BoPSettings();
    }

}
