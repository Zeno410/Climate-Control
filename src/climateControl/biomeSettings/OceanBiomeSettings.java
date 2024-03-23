
package climateControl.biomeSettings;

import climateControl.api.BiomeSettings;
import climateControl.api.ClimateControlRules;
import climateControl.api.ClimateDistribution;
import com.Zeno410Utils.Mutable;
import java.io.File;

/**
 * This is separate from vanilla settings because vanilla biomes are sometimes removed
 * and replaced but at present that's not happening with ocean biomes
 * @author Zeno410
 */
public class OceanBiomeSettings extends BiomeSettings {

    public static final String biomeCategory = "OceanBiome";

    public final Element coastalOcean = new OceanElement("Ocean",0,100);
    public final Element deepOcean = new DeepOceanElement("DeepOcean",24,100);


    public OceanBiomeSettings() {
        super(biomeCategory);
    }

    @Override
    public void setNativeBiomeIDs(File configDirectory) {
        // no configs for vanilla biomes
    }

    @Override
    public void setRules(ClimateControlRules rules) {
        // nothing yet
    }

    @Override
    public void onNewWorld() {
        // no action
    }

    protected class OceanElement extends Element {
        OceanElement(String name, int ID, int incidence) {
            super(name,ID,incidence);
            this.setDistribution(ClimateDistribution.OCEAN);
        }
    }

    protected class DeepOceanElement extends Element {
        DeepOceanElement(String name, int ID, int incidence) {
            super(name,ID,incidence);
            this.setDistribution(ClimateDistribution.DEEP_OCEAN);
        }
    }
    @Override
    public boolean biomesAreActive() {
        return true;
    }
}
