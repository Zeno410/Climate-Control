
package climateControl.biomeSettings;

import climateControl.api.BiomePackage;
import climateControl.api.BiomeSettings;

import climateControl.api.ClimateControlRules;
import climateControl.utils.Mutable;
import climateControl.utils.Settings;
import de.teamlapen.vampirism.biome.BiomeVampireForest;
import java.io.File;
import net.minecraftforge.common.config.Configuration;
/**
 *
 * @author Zeno410
 */
public class VampirismPackage extends BiomePackage {

    public VampirismPackage() {
        super("VampirismInCC.cfg");
        // confirm Vampirism is there.
        Class vampireForestClass = BiomeVampireForest.class;
    }

    @Override
    public BiomeSettings freshBiomeSetting() {
        return new VampirismBiomeSettings();
    }

    public static class VampirismBiomeSettings extends BiomeSettings {
        public static final String biomeCategory = "VampirismBiome";
        public static final String vampirismCategory = "VampirismSettings";
        public final Category vampirismSettings = new Category(vampirismCategory);

        public final Element  vampirismBiome = new Element("Vampirism Biome", 40,"LAND");

        public VampirismBiomeSettings() {
            super(biomeCategory);
        }

        @Override
        public void setRules(ClimateControlRules rules) {
            // no action
        }

        @Override
        public void setNativeBiomeIDs(File configDirectory) {
            NativeVampirismSettings result = new NativeVampirismSettings();
            File configFile = new File(configDirectory,"vampirism.cfg");
            result.readFrom(new Configuration(configFile));
            vampirismBiome.biomeID().set(result.vampirismBiomeID.value());
            //vampirismBiome.biomeID().set(40);
        }

        static final String biomesOnName = "VampirismBiomeOn";

        public final Mutable<Boolean> biomesFromConfig = vampirismSettings.booleanSetting(
                            biomesOnName, "", false);
        @Override
        public boolean biomesAreActive() {
            return this.biomesFromConfig.value();
        }

        private class NativeVampirismSettings extends Settings {
            public static final String biomeIDName = "general";
            public static final String disableName = "disable";
            public final Category biomeIDs = new Category(biomeIDName);
            public final Category disable = new Category(disableName);

            Mutable<Integer> vampirismBiomeID =biomeIDs.intSetting("vampirism_biome_id", 40);
            Mutable<Boolean> vampirismBiomeDisabled =disable.booleanSetting("disable_vampire_biome", false);

        }
    }
}
