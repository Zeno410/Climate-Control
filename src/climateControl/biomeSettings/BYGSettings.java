package climateControl.biomeSettings;

import java.io.File;

import com.Zeno410Utils.Mutable;

import climateControl.api.BiomeSettings;
import climateControl.api.Climate;
import climateControl.api.ClimateControlRules;
import climateControl.api.ClimateDistribution;

public class BYGSettings extends BiomeSettings {
    public static final String biomeCategory = "BYGBiome";
    

    //public final ID alpsForestSubBiome = new ID("Alps Forest (Sub-Biome)",63);
    public final Element alliumFields = new Element("Allium Fields",-1,5,Climate.WARM.name);
    public final Element amaranthFields = new Element("Amaranth Fields",-1,5,Climate.WARM.name);
    public final Element alps = new Element("Alps",-1,5,Climate.COOL.name);
    public final Element ancientForest = new Element("Ancient Forest",-1,5,Climate.WARM.name);
    public final Element aspenForest = new Element("Aspen Forest",-1,5,Climate.COOL.name);
    public final Element baobabSavanna = new Element("Baobab Savanna",-1,1,Climate.HOT.name);
    public final Element bayou = new Element("Bayou",-1,5,Climate.WARM.name);
    public final Element blueTaiga = new Element("Blue Taiga",-1,5,Climate.COOL.name);
    public final Element bluffMountains = new Element("Bluff Mountains",-1,5,Climate.COOL.name);
    public final Element bog = new Element("Bog",-1,5,Climate.COOL.name);
    public final Element borealForest = new Element("Boreal Forest",-1,5,Climate.COOL.name);
    public final Element chapparralLowlands = new Element("Chaparral Lowlands",-1,5,Climate.WARM.name);
    public final Element cherryGrove = new Element("Cherry Grove",-1,5,Climate.WARM.name);
    public final Element cikaForest = new Element("Cika Forest",-1,5,Climate.COOL.name);
    public final Element coloredCanyons = new Element("Colored Canyons",-1,5,Climate.HOT.name);
    public final Element coniferousForest = new Element("Coniferous Forest",-1,5,Climate.COOL.name);
    public final Element crystalCanyons = new Element("Crystal Canyons",-1,5,Climate.HOT.name);
    public final Element cypressSwamplands = new Element("Cypress Swamplands",-1,5,Climate.WARM.name);
    public final Element deadSea = new Element("Dead Sea",-1,1,Climate.HOT.name);
    public final Element deciduousForest = new Element("Deciduous Forest",-1,5,Climate.WARM.name);
    public final Element doverMountains = new Element("Dover Mountains",-1,5,Climate.COOL.name);
    public final Element dunes = new Element("Dunes",-1,5,Climate.HOT.name);
    public final Element ebonyWoods = new Element("Ebony Woods",-1,5,Climate.WARM.name);
    public final Element enchantedForest = new Element("Enchanted Forest",-1,2,ClimateDistribution.MEDIUM.name());
    public final Element evergreenTaiga = new Element("Evergreen Taiga",-1,5,Climate.COOL.name);
    public final Element frostyForest = new Element("Frosty Forest",-1,5,Climate.SNOWY.name);
    public final Element floweringPlains = new Element("Flowering Plains",-1,5,Climate.WARM.name);
    public final Element fungalJungle = new Element("Fungal Jungle",-1,2,Climate.HOT.name);
    public final Element giantBlueSpruceTaiga = new Element("Giant Blue Spruce Taiga",-1,2,Climate.COOL.name);
    public final Element giantSeasonalSpruceTaiga = new Element("Giant Seasonal Spruce Taiga",-1,2,Climate.COOL.name);
    public final Element giantSnowySpruceTaiga = new Element("Giant Snowy Spruce Taiga",-1,2,Climate.SNOWY.name);
    public final Element glaciers = new Element("Glaciers",-1,5,Climate.SNOWY.name);
    public final Element glowshroomBayou = new Element("Glowshroom Bayou",-1,1,Climate.WARM.name);
    public final Element grasslandPlateau = new Element("Grassland Plateau",-1,5,Climate.WARM.name);
    public final Element greatLakes = new Element("Great Lakes",-1,5,Climate.COOL.name);
    public final Element greatOakLowlands = new Element("GreatOakLowlands",-1,5,ClimateDistribution.MEDIUM.name());
    public final Element jacarandaForest = new Element("Jacaranda Forest",-1,5,Climate.WARM.name);
    public final Element mangroveMarshes = new Element("Mangrove Marshes",-1,5,Climate.WARM.name);
    public final Element lushDesert = new Element("Lush Desert",-1,5,Climate.HOT.name);
    public final Element marshlands = new Element("Marshlands",-1,5,Climate.WARM.name);
    public final Element mapleForest = new Element("Maple Forest",-1,5,Climate.COOL.name);
    public final Element meadow = new Element("Meadow",-1,5,ClimateDistribution.MEDIUM.name());
    public final Element northernForest = new Element("Northern Forest",-1,5,Climate.COOL.name);
    public final Element orchard = new Element("Orchard",-1,5,ClimateDistribution.MEDIUM.name());
    public final Element outback = new Element("Outback",-1,5,Climate.HOT.name);
    public final Element outlands = new Element("Outlands",-1,5,Climate.HOT.name);
    public final Element pineLowlands = new Element("Pine Lowlands",-1,5,Climate.WARM.name);
    public final Element pineMountains = new Element("Pine Mountains",-1,5,Climate.WARM.name);
    public final Element prairie = new Element("Prairie",-1,5,ClimateDistribution.MEDIUM.name());
    public final Element quagmire = new Element("Quagmire",-1,2,Climate.WARM.name);
    public final Element redDesert = new Element("Red Desert",-1,2,Climate.HOT.name);
    public final Element redOakForest = new Element("Red Oak Forest",-1,5,ClimateDistribution.MEDIUM.name());
    public final Element redOutlands = new Element("Red Outlands",-1,5,Climate.HOT.name);
    public final Element redwoodTropics = new Element("Redwood Tropics",-1,2,ClimateDistribution.MEDIUM.name());
    public final Element savannaCanopy = new Element("Savanna Canopy",-1,5,Climate.HOT.name);
    public final Element seasonalBirchForest = new Element("Seasonal Birch Forest",-1,5,Climate.COOL.name);
    public final Element seasonalDeciduous = new Element("Seasonal Deciduous Forest",-1,5,Climate.COOL.name);
    public final Element seasonalForest = new Element("Seasonal Forest",-1,5,Climate.COOL.name);
    public final Element seasonalTaiga = new Element("Seasonal Taiga",-1,5,Climate.COOL.name);
    public final Element shrublands = new Element("Shrublands",-1,5,Climate.HOT.name);
    public final Element skyrisHighlands = new Element("Skyris Highlands",-1,5,ClimateDistribution.MEDIUM.name());
    public final Element snowyConiferousForest = new Element("Snowy Coniferous Forest",-1,5,Climate.SNOWY.name);
    public final Element snowyDeciduousForest = new Element("Snowy Deciduous Forest",-1,5,Climate.SNOWY.name);
    public final Element snowyEvergreenTaiga = new Element("Snowy Evergreen Taiga",-1,5,Climate.SNOWY.name);
    public final Element snowyPineMountains = new Element("Snowy Pine Mountains",-1,5,Climate.SNOWY.name);
    public final Element sonoranDesert = new Element("Sonoran Desert",-1,5,Climate.HOT.name);
    public final Element stellataPasture = new Element("Stellata Pasture",-1,5,Climate.WARM.name);
    public final Element stoneBrushlands = new Element("Stone Brushlands",-1,5,Climate.HOT.name);
    public final Element tropicalIslands = new Element("Tropical Mountains",-1,1,Climate.DEEP_OCEAN.name);
    public final Element tropicalMountains = new Element("Tropical Mountains",-1,5,Climate.HOT.name);
    public final Element tropicalRainforest = new Element("Tropical Rainforest",-1,5,Climate.HOT.name);
    public final Element weepingWitchForest = new Element("Weeping Witch Forest",-1,2,Climate.COOL.name);
    public final Element whisperingWoods = new Element("Whispering Woods",-1,5,Climate.WARM.name);
    public final Element woodlands = new Element("Woodlands",-1,5,Climate.WARM.name);
    public final Element zelkovaForest = new Element("Zelkova Forest",-1,5,Climate.COOL.name);

	public BYGSettings() {
		super(biomeCategory);
	}

	@Override
	public void setRules(ClimateControlRules rules) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setNativeBiomeIDs(File configDirectory) {
		alliumFields.setIDFrom("byg:balliumfields");
		amaranthFields.setIDFrom("bamaranth_fields");
		alps.setIDFrom("byg:balps");
		ancientForest.setIDFrom("byg:bancientforest");
		aspenForest.setIDFrom("byg:baspenforest");
		bayou.setIDFrom("byg:bbayou");
		blueTaiga.setIDFrom("byg:bbluetaiga");
		bluffMountains.setIDFrom("byg:bbluff_mountains");
		baobabSavanna.setIDFrom("byg:bbaobabsavanna");
		bog.setIDFrom("byg:bbog");
		borealForest.setIDFrom("byg:bborealforest");
		chapparralLowlands.setIDFrom("byg:bchaparrallowlands");
		cherryGrove.setIDFrom("byg:bcherrygrove");
		cikaForest.setIDFrom("byg:bcikaforest");
		coloredCanyons.setIDFrom("byg:bcolored_canyons");
		coniferousForest.setIDFrom("byg:bconiferousforest");
		crystalCanyons.setIDFrom("byg:bcrystal_canyons");
		cypressSwamplands.setIDFrom("byg:bcypress_swamplands");
		deadSea.setIDFrom("byg:bdeadsea");
		deciduousForest.setIDFrom("byg:bdeciduousforest");
		doverMountains.setIDFrom("byg:bdovermoutains");
		dunes.setIDFrom("byg:bdunes");
		ebonyWoods.setIDFrom("byg:bebonywoods");
		enchantedForest.setIDFrom("byg:benchantedforest");
		evergreenTaiga.setIDFrom("byg:bevergreentaiga");
		floweringPlains.setIDFrom("byg:bfloweringplains");
		frostyForest.setIDFrom("byg:bfrostyforest");
		fungalJungle.setIDFrom("byg:bfungaljungle");
		giantBlueSpruceTaiga.setIDFrom("byg:bgiant_blue_spruce_taiga");
		giantSeasonalSpruceTaiga.setIDFrom("byg:bgiant_seasonal_spruce_taiga");
		giantSnowySpruceTaiga.setIDFrom("byg:bgiant_snowy_spruce_taiga");
		glaciers.setIDFrom("byg:bglaciers");
		glowshroomBayou.setIDFrom("byg:bglowshroombayou");
		grasslandPlateau.setIDFrom("byg:bgrasslandplateau");
		greatLakes.setIDFrom("byg:bgreatlakes");
		greatOakLowlands.setIDFrom("byg:bgreatoaklowlands");
		jacarandaForest.setIDFrom("byg:bjacarandaforest");
		lushDesert.setIDFrom("byg:blushdesert");
		mangroveMarshes.setIDFrom("byg:bmangrovemarshes");
		mapleForest.setIDFrom("byg:bmapleforest");
		marshlands.setIDFrom("bbyg:bmarshlandsy");
		meadow.setIDFrom("byg:meadow");
		northernForest.setIDFrom("byg:bnorthernforest");
		orchard.setIDFrom("byg:borchard");
		outback.setIDFrom("byg:boutback");
		outlands.setIDFrom("byg:boutlands");
		pineLowlands.setIDFrom("byg:bpine_lowlands");
		pineMountains.setIDFrom("byg:bpinemountains");
		prairie.setIDFrom("byg:bprairie");
		quagmire.setIDFrom("byg:bquagmire");
		redwoodTropics.setIDFrom("byg:bredwoodtropics");
		redDesert.setIDFrom("byg:breddesert");
		redOakForest.setIDFrom("byg:bredoakforest");
		redOutlands.setIDFrom("byg:bred_outlands");
		savannaCanopy.setIDFrom("byg:bsavannacanopy");
		seasonalBirchForest.setIDFrom("byg:bseasonalbirchforest");
		seasonalDeciduous.setIDFrom("byg:bseasonaldeciduous");
		seasonalForest.setIDFrom("byg:bseasonalforest");
		seasonalTaiga.setIDFrom("byg:bseasonaltaiga");
		shrublands.setIDFrom("byg:bshrublands");
		skyrisHighlands.setIDFrom("byg:bskyrishighlands");
		snowyConiferousForest.setIDFrom("byg:bsnowyconiferousforest");
		snowyDeciduousForest.setIDFrom("byg:bsnowydeciduousforest");
		snowyEvergreenTaiga.setIDFrom("byg:bsnowyevergeentaiga");
		snowyPineMountains.setIDFrom("byg:bsnowypinemountains");
		sonoranDesert.setIDFrom("byg:bsonorandesert");
		stellataPasture.setIDFrom("byg:bstellatapasture");
		stoneBrushlands.setIDFrom("byg:bstone_brushlands");
		tropicalIslands.setIDFrom("byg:btropical_islands");
		tropicalMountains.setIDFrom("byg:btropicalmountains");
		tropicalRainforest.setIDFrom("byg:btropicalrainforest"); 
		weepingWitchForest.setIDFrom("byg:bweepingwitchforest");
		whisperingWoods.setIDFrom("byg:bwhisperingwoods");
		woodlands.setIDFrom("byg:woodlands");
		zelkovaForest.setIDFrom("byg:bzelkovaforest");
	}
	
    static final String biomesOnName = "BiomesYouGoOn";

    public final Mutable<Boolean> biomesFromConfig = climateControlCategory.booleanSetting(
                        biomesOnName, "", false);

    static final String configName = "BYG";
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
