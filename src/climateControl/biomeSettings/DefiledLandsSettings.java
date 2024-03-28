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

public class DefiledLandsSettings extends BiomeSettings {
    public static final String biomeCategory = "DefiledLandsBiome";

    public DefiledLandsSettings() {
        super(biomeCategory);
    }

    public final Element defiledDesert = new Element("Defiled Desert",-1,6,Climate.HOT.name);
    public final Element defiledPlains = new Element("Defiled Plains",-1,6,ClimateDistribution.MEDIUM.name());
    public final Element tenebraForest = new Element("Tenebra Forest",-1,3,ClimateDistribution.MEDIUM.name());
    public final Element vilespineForest = new Element("Vilespine Forest",-1,3,ClimateDistribution.MEDIUM.name());
    public final Element defiledHills = new Element("Defiled Hills",-1,6,ClimateDistribution.MEDIUM.name());
    public final Element defiledSwamp = new Element("Defiled Swamp",-1,2,ClimateDistribution.WARM.name());
    public final Element defiledIcePlains = new Element("Defiled Ice Plains",-1,6,ClimateDistribution.SNOWY.name());

    @Override
    public void setNativeBiomeIDs(File configDirectory) {
    	defiledDesert.setIDFrom("defiledlands:desert_defiled");
    	defiledPlains.setIDFrom("defiledlands:plains_defiled");
    	tenebraForest.setIDFrom("defiledlands:forest_tenebra");
    	vilespineForest.setIDFrom("defiledlands:forest_vilespine");
    	defiledHills.setIDFrom("defiledlands:hills_defiled");
    	defiledSwamp.setIDFrom("defiledlands:swamp_defiled");
    	defiledIcePlains.setIDFrom("defiledlands:ice_plains_defiled");
    }

    @Override
    public void setRules(ClimateControlRules rules) {
        // nothing yet
    }
    static final String biomesOnName = "DefiledLandsBiomesOn";

    public final Mutable<Boolean> biomesFromConfig = climateControlCategory.booleanSetting(
                        biomesOnName, "", false);
    
    static final String configName = "Defiled_Lands";
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