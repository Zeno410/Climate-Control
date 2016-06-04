
package climateControl.biomeSettings;

import climateControl.api.BiomeSettings;
import climateControl.api.ClimateControlRules;
import climateControl.api.ClimateDistribution;
import climateControl.utils.Mutable;
import java.io.File;
import net.minecraftforge.common.config.Configuration;

/**
 *
 * @author Zeno410
 */
public class ThaumcraftBiomeSettings extends BiomeSettings {
    public static final String biomeCategory = "ThaumcraftBiome";

    public ThaumcraftBiomeSettings() {
        super(biomeCategory);
    }

    public final Element tainted = new Element("Tainted", 192,1);
    public final Element magicalForest = new Element("Magical Forest", 193,7);
    public final Element eerie = new Element("Eerie", 194,3);

    @Override
    public void setNativeBiomeIDs(File configDirectory) {
        Configuration config = new Configuration(new File(configDirectory,"Thaumcraft.cfg"));
        tainted.biomeID().set(config.get("biomes","biome_taint", 192).getInt());
        magicalForest.biomeID().set(config.get("biomes","biome_magical_forest", 193).getInt());
        eerie.biomeID().set(config.get("biomes","biome_eerie", 194).getInt());
    }

    @Override
    public void setRules(ClimateControlRules rules) {
        // nothing yet
    }
    static final String biomesOnName = "ThaumcraftBiomesOn";

    public final Mutable<Boolean> biomesFromConfig = climateControlCategory.booleanSetting(
                        biomesOnName, "", false);
    
    static final String configName = "Thaumcraft";
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
}
