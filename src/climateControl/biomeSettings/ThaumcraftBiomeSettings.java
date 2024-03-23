
package climateControl.biomeSettings;

import climateControl.api.BiomeSettings;
import climateControl.api.ClimateControlRules;
import com.Zeno410Utils.Mutable;
import com.Zeno410Utils.Zeno410Logger;

import java.io.File;
import java.util.Iterator;
import java.util.logging.Logger;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.config.Configuration;

/**
 *
 * @author Zeno410
 */
public class ThaumcraftBiomeSettings extends BiomeSettings {
    public static final String biomeCategory = "ThaumcraftBiome";
    public static Logger logger = new Zeno410Logger("ThaumcraftIDs").logger();

    public ThaumcraftBiomeSettings() {
        super(biomeCategory);
    }

    public final Element magicalForest = new Element("Magical Forest", 40,5);
    public final Element eerie = new Element("Eerie", 41,3);

    @Override
    public void setNativeBiomeIDs(File configDirectory) {//thaumcraft:magical_forest thaumcraft:eerie
        Configuration config = new Configuration(new File(configDirectory,"ThaumcraftinCC.cfg"));
        magicalForest.biomeID().set(config.get("biomes","biome_magical_forest", 40).getInt());
        eerie.biomeID().set(config.get("biomes","biome_eerie", 41).getInt());
        Iterator<ResourceLocation> registries = Biome.REGISTRY.getKeys().iterator();
        while (registries.hasNext()) {
        	ResourceLocation location = registries.next();
        	if (location.toString().equals("thaumcraft:magical_forest")) {
                magicalForest.biomeID().set(Biome.getIdForBiome(Biome.REGISTRY.getObject(location)));
        	}
        	if (location.toString().equals("thaumcraft:eerie")) {
        		eerie.biomeID().set(Biome.getIdForBiome(Biome.REGISTRY.getObject(location)));
        	}
        }
        config.save();
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
