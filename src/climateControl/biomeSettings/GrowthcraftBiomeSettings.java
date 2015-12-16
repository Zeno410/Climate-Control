
package climateControl.biomeSettings;

import climateControl.api.BiomeSettings;
import climateControl.api.ClimateControlRules;
import climateControl.utils.Mutable;
import climateControl.utils.Settings;
import java.io.File;
import net.minecraftforge.common.config.Configuration;

/**
 *
 * @author Zeno410
 */

public class GrowthcraftBiomeSettings extends BiomeSettings {
    public static final String biomeCategory = "GrowthcraftBiome";
    public static final String growthcraftCategory = "GrowthcrafSettings";
    public final Category growthcraftSettings = new Category(growthcraftCategory);

    public final Element  bambooForest = new Element("Bamboo Forest", 170,"WARM");

    public GrowthcraftBiomeSettings() {
        super(biomeCategory);
    }

    @Override
    public void setRules(ClimateControlRules rules) {
        // no action
    }

    @Override
    public void setNativeBiomeIDs(File configDirectory) {
        GrowthcraftSettings result = new GrowthcraftSettings();
        File arsMagicaDirectory = new File(configDirectory,"growthcraft");
        File configFile = new File(arsMagicaDirectory,"bamboo.conf");
        result.readFrom(new Configuration(configFile));
        bambooForest.biomeID().set(result.bambooForestID.value());
    }

    static final String biomesOnName = "GrowthcraftBiomesOn";

    public final Mutable<Boolean> biomesFromConfig = climateControlCategory.booleanSetting(
                        biomesOnName, "", false);
    @Override
    public boolean biomesAreActive() {
        return this.biomesFromConfig.value();
    }

    private class GrowthcraftSettings extends Settings {
        public static final String biomeIDName = "biomes";
        public final Category biomeIDs = new Category(biomeIDName);

        Mutable<Integer> bambooForestID =biomeIDs.intSetting("Bamboo Forest biome ID", 170);

    }
}