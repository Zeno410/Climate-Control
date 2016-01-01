
package climateControl.biomeSettings;

import am2.api.ArsMagicaApi;
import climateControl.api.BiomePackage;
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
public class ArsMagicaPackage extends BiomePackage {

    public ArsMagicaPackage() {
        super("ArsMagicaInCC.cfg");
        // confirm Ars Magica is there.
        Class apiClass = ArsMagicaApi.class;
    }

    @Override
    public BiomeSettings freshBiomeSetting() {
        return new ArsMagicaBiomeSettings();
    }

    public static class ArsMagicaBiomeSettings extends BiomeSettings {
        public static final String biomeCategory = "ArsMagicaBiome";
        public static final String arsMagicaCategory = "ArsMagicaSettings";
        public final Category arsMagicaSettings = new Category(arsMagicaCategory);

        public final Element  witchwoodForest = new Element("Witchwood Forest", 100,"LAND");

        public ArsMagicaBiomeSettings() {
            super(biomeCategory);
        }

        @Override
        public void setRules(ClimateControlRules rules) {
            // no action
        }

        @Override
        public void setNativeBiomeIDs(File configDirectory) {
            NativeArsMagicaSettings result = new NativeArsMagicaSettings();
            File arsMagicaDirectory = new File(configDirectory,"AM2");
            File configFile = new File(arsMagicaDirectory,"AM2.cfg");
            result.readFrom(new Configuration(configFile));
            witchwoodForest.biomeID().set(result.witchwoodForestID.value());
        }

        static final String biomesOnName = "ArsMagicaBiomesOn";

        public final Mutable<Boolean> biomesFromConfig = climateControlCategory.booleanSetting(
                            biomesOnName, "", false);

        static final String configName = "ArsMagica";
        public final Mutable<Boolean> biomesInNewWorlds = climateControlCategory.booleanSetting(
                            this.startBiomesName(configName), 
                            "Use biome in new worlds and dimensions", true);

        @Override
        public void onNewWorld() {
            biomesFromConfig.set(biomesInNewWorlds);
        }

        @Override
        public boolean biomesAreActive() {
            return this.biomesFromConfig.value();
        }

        private class NativeArsMagicaSettings extends Settings {
            public static final String biomeIDName = "general";
            public final Category biomeIDs = new Category(biomeIDName);

            Mutable<Integer> witchwoodForestID =biomeIDs.intSetting("WitchwoodForestBiomeID", 100);

        }
    }
}