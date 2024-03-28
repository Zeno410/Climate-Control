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

public class EnvironsSettings extends BiomeSettings {
    public static final String biomeCategory = "EnvironsBiome";

    public EnvironsSettings() {
        super(biomeCategory);
    }

    public final Element canyon = new Element("Canyon",-1,10,Climate.HOT.name);
    public final Element coldPineTaiga = new Element("Cold Pine Taiga",-1,10,Climate.SNOWY.name);
    public final Element coldPineTaigaM = new Element("Cold Pine Taiga M",-1,3,Climate.SNOWY.name);
    public final Element dryOakForest = new Element("Dry Oak Forest",-1,10,Climate.HOT.name);
    public final Element dryOakForestM = new Element("Dry Oak Forest M",-1,3,Climate.HOT.name);
    public final Element emeraldCliffs = new Element("Emerald Cliffs",-1,5,Climate.WARM.name);
    public final Element endForest = new Element("End Forest",-1,5,Climate.COOL.name);
    public final Element floatingEndForest = new Element("Floating End Forest",-1,2,Climate.COOL.name);
    public final Element extremeJungle = new Element("Extreme Jungle",-1,5,Climate.HOT.name);
    public final Element icyHills = new Element("IcyHills",-1,10,Climate.SNOWY.name);
    public final Element kakadu = new Element("Kakadu",-1,5,Climate.HOT.name);
    public final Element lushForest = new Element("Lush Forest",-1,10,Climate.WARM.name);
    public final Element mushroomRainforests = new Element("Mushroom Rainforest",-1,5,Climate.HOT.name);
    public final Element overgrownPlains = new Element("OvergrownPlains",-1,10,Climate.WARM.name);
    public final Element pineTaiga = new Element("Pine Taiga",-1,10,Climate.COOL.name);
    public final Element pineTaigaM = new Element("Pine Taiga M",-1,3,Climate.COOL.name);
    public final Element roofedSwamp = new Element("Roofed Swamp",-1,5,Climate.WARM.name);
    public final Element sandstoneRanges = new Element("Sandstone Ranges",-1,10,Climate.HOT.name);
    public final Element silkForest = new Element("Silk Forest",-1,5,Climate.WARM.name);
    public final Element stoneBasin = new Element("Stone Basin",-1,10,true,Climate.COOL.name);
    public final Element stoneHills = new Element("Stone Hills",-1,3,Climate.COOL.name);
    public final Element tallOakForest = new Element("Tall Oak Forest",-1,10,Climate.WARM.name);
    public final Element tallOakForestM = new Element("Tall Oak Forest M",-1,3,Climate.WARM.name);
    public final Element tallOakWetlands = new Element("Tall Oak Wetlands",-1,5,Climate.WARM.name);
    public final Element wastelandEroded = new Element("Eroded Wasteland",-1,3,Climate.HOT.name);
    public final Element wastelandSpikes = new Element("Wasteland Spikes",-1,10,Climate.HOT.name);
    

    @Override
    public void setNativeBiomeIDs(File configDirectory) {   
    	canyon.setIDFrom("environs:canyon");
	    coldPineTaiga.setIDFrom("environs:cold_pine_taiga");
	    coldPineTaigaM.setIDFrom("environs:cold_pine_taiga_m");
	    dryOakForest.setIDFrom("environs:dry_oak_forest");
	    dryOakForestM.setIDFrom("environs:dry_oak_forest_m");
	    emeraldCliffs.setIDFrom("environs:emerald_cliffs");
	    endForest.setIDFrom("environs:end_forest");
	    floatingEndForest.setIDFrom("environs:floating_end_forest");
	    extremeJungle.setIDFrom("");
	    icyHills.setIDFrom("environs:icy_hills");
	    kakadu.setIDFrom("environs:kakadu");
	    lushForest.setIDFrom("environs:lush_forest");
	    mushroomRainforests.setIDFrom("environs:mushroom_rainforest");
	    overgrownPlains.setIDFrom("environs:overgrown_plains");
	    pineTaiga.setIDFrom("environs:pine_taiga");
	    pineTaigaM.setIDFrom("environs:pine_taiga_m");
	    roofedSwamp.setIDFrom("environs:roofed_swamp");
	    sandstoneRanges.setIDFrom("environs:sandstone_ranges");
	    silkForest.setIDFrom("environs:silk_wetland");
	    stoneBasin.setIDFrom("environs:stone_basin");
	    stoneHills.setIDFrom("environs:stone_hills");
	    tallOakForest.setIDFrom("environs:tall_oak_forest");
	    tallOakForestM.setIDFrom("environs:tall_oak_forest_m");
	    tallOakWetlands.setIDFrom("environs:tall_oak_wetland");
	    wastelandEroded.setIDFrom("environs:wasteland_eroded");
	    wastelandSpikes.setIDFrom("environs:wasteland_spikes");
    }

    @Override
    public void setRules(ClimateControlRules rules) {
        // nothing yet
    }
    static final String biomesOnName = "EnvironsBiomesOn";

    public final Mutable<Boolean> biomesFromConfig = climateControlCategory.booleanSetting(
                        biomesOnName, "", false);
    
    static final String configName = "Environs";
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