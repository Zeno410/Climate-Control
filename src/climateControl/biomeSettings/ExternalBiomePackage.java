
package climateControl.biomeSettings;

/**
 *
 * @author Zeno410
 */

import climateControl.api.BiomePackage;
import climateControl.api.BiomeSettings;
import climateControl.api.ClimateControlRules;
import climateControl.api.ClimateDistribution;
import com.Zeno410Utils.Mutable;
import com.Zeno410Utils.Settings;
import java.io.File;
import java.util.ArrayList;
import net.minecraftforge.common.config.Configuration;
/**
 *
 * @author Zeno410
 */
public class ExternalBiomePackage extends BiomePackage {

    public ExternalBiomePackage() {
        super("ExternalBiomesinCC");
    }
    
    private String externalBiomeNames;

    @Override
    public BiomeSettings freshBiomeSetting() {
        return new ExternalBiomeSettings(externalBiomeNames);
    }

     public static class ExternalBiomeSettings extends BiomeSettings {
        public static final String biomeCategory = "ExternalBiome";
        public static final String externalCategory = "ExternalSettings";
        public final Category externalSettings = new Category(externalCategory);

        public final ArrayList<Element> biomes = new ArrayList<Element>();

        public ExternalBiomeSettings(String nameList) {
            super(biomeCategory);
            String [] names = nameList.split(",");
            for (int i = 0; i < names.length; i++) {
                biomes.add(new Element(names[i],-1,10,false,ClimateDistribution.MEDIUM.name()));
            }
        }

        @Override
        public void stripIDsFrom(Configuration config) {
            // no action because we actually want the IDs in the general configs
        }

        @Override
        public void setRules(ClimateControlRules rules) {
            // no action
        }

        @Override
        public void setNativeBiomeIDs(File configDirectory) {
        }

        // no need for the usual configs; users can turn it off with a blank name list

        @Override
        public void onNewWorld() {
        }

        @Override
        public boolean biomesAreActive() {
            return true;
        }
    }
}