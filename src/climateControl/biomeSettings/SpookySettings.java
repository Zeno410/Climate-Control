package climateControl.biomeSettings;

import climateControl.api.BiomeSettings;
import climateControl.api.Climate;
import climateControl.api.ClimateControlRules;
import climateControl.api.ClimateDistribution;

import com.Zeno410Utils.Mutable;

import java.io.File;

/**
 *
 * @author Zeno410
 */

public class SpookySettings extends BiomeSettings {
    public static final String biomeCategory = "SpookyBiome";

    public SpookySettings() {
        super(biomeCategory);
    }

    public final Element bloodedHills = new Element("Blooded Hills",-1,5,Climate.WARM.name);
    public final Element ghostlyForest = new Element("Ghostly Forest",-1,5,Climate.COOL.name);
    public final Element seepingForest = new Element("Seeping Forest",-1,5,Climate.WARM.name);
    public final Element sorbusForest = new Element("Sorbus Forest",-1,5,Climate.WARM.name);
    

    @Override
    public void setNativeBiomeIDs(File configDirectory) { 
    	bloodedHills.setIDFrom("spookybiomes:bloodied_hills");
    	ghostlyForest.setIDFrom("spookybiomes:ghostly_forest");
    	seepingForest.setIDFrom("spookybiomes:seeping_forest");
    	sorbusForest.setIDFrom("spookybiomes:sorbus_forest");
    }

    @Override
    public void setRules(ClimateControlRules rules) {
        // nothing yet
    }
    static final String biomesOnName = "SpookyBiomesOn";

    public final Mutable<Boolean> biomesFromConfig = climateControlCategory.booleanSetting(
                        biomesOnName, "", false);
    
    static final String configName = "Spooky";
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