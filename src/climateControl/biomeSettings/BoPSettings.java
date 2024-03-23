
package climateControl.biomeSettings;

import climateControl.api.BiomeSettings;
import biomesoplenty.api.biome.BOPBiomes;
import biomesoplenty.common.init.ModBiomes;
import climateControl.api.Climate;
import climateControl.api.ClimateControlRules;
import climateControl.api.ClimateDistribution;
import climateControl.customGenLayer.GenLayerBopBiomeEdge;
import climateControl.generator.SubBiomeChooser;
import com.Zeno410Utils.Acceptor;
import com.Zeno410Utils.Mutable;
import com.google.common.base.Optional;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.minecraft.init.Biomes;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.layer.GenLayer;

/**
 *
 * @author Zeno410
 */
public class BoPSettings extends BiomeSettings {

    //public final ID alpsForestSubBiome = new ID("Alps Forest (Sub-Biome)",63);
    public final Element alps = new Element("Alps",177,5,Climate.SNOWY.name);
    public final Element alpsFoothills = new Element("Alps Foothills",178,0,Climate.SNOWY.name);
    //public final ID alpsMountainside = new ID("Alps Mountainside (Sub-Biome)",64);
    //public final Element arctic = new Element("Arctic",178,10,true,Climate.SNOWY.name);
    //public final ID autumnHills = new ID("Autumn Hills",58);
    //public final ID badlands = new ID("Badlands",179);
    public final Element bambooForest = new Element("Bamboo Forest",180,5,true,"WARM");
    public final Element bayou = new Element("Bayou",181,10,true,"WARM");
    public final Element bog = new Element("Bog",183,7,true,"COOL");
    public final Element borealForest = new Element("Boreal Forest",184,10,true,"COOL");
    public final Element brushland = new Element("Brushland",185,7,true,"WARM");
    public final Element chaparral = new Element("Chaparral",187,10,true,"WARM");
    public final Element cherryBlossomGrove = new Element("Cherry Blossom Grove",188,3,true,"COOL");
    public final Element coldDesert = new Element("Cold Desert",255,10,true,"SNOWY");
    public final Element coniferousForest = new Element("Coniferous Forest",189,10,true,"WARM");
    //public final ID coralReefOcean = new ID("Coral Reef (Ocean)",73);
    public final Element coralReef = new Element("Coral Reef",94,3,"OCEAN");
    public final Element crag = new Element("Crag",191,3,"MEDIUM");
    public final Element deadForest = new Element("Dead Forest",192,7,true,"COOL");
    public final Element deadSwamp = new Element("Dead Swamp",194,7,true,"WARM");
    public final Element fen = new Element("Fen",198,true,"WARM");
    public final Element flowerField = new Element("Flower Field",199,3,true,"WARM");
    public final Element flowerIsland = new Element("Flower Island",255,1,true,"DEEP_OCEAN");
    public final Element glacier = new Element("Glacier",203,0,"SNOWY");
    public final Element grassland = new Element("Grassland",204,true,"COOL");
    public final ID gravelBeach = new ID("Gravel Beach",75);
    public final Element grove = new Element("Grove",205,5,"COOL");
    public final Element heathland = new Element("Heathland",206,"COOL");
    public final Element highland = new Element("Highland",210,"COOL");
    public final Element kelpForest = new Element("Kelp Forest",95,0,"OCEAN");
    public final Element lavenderFields = new Element("Lavender Fields",56,3,"WARM");
    public final Element lushDesert = new Element("Lush Desert",214,3,true,"HOT");
    public final Element lushSwamp = new Element("Lush Swamp",215,5,true,"WARM");
    public final Element mangrove = new Element("Mangrove",216,3,"WARM");
    public final Element mapleWoods = new Element("Maple Woods",217,5,"COOL");
    public final Element marsh = new Element("Marsh",218,7,true,"COOL");
    public final Element meadow = new Element("Meadow",219,true,"COOL");
    public final Element moor = new Element("Moor",221,true,"COOL");
    public final Element mountain = new Element("Mountain",222,8,"WARM");
    public final Element mountainFoothills = new Element("Mountain Foothills",93,0,"WARM");
    public final Element mysticGrove = new Element("Mystic Grove",223,3,"MEDIUM");
    public final ID oasis = new ID("Oasis",224);
    //public final ID oceanicAbyss = new ID("Oceanic Abyss (Ocean)",72);
    public final Element ominousWoods = new Element("Ominous Woods",225,3,"COOL");
    public final Element orchard = new Element("Orchard",226,5,ClimateDistribution.MEDIUM.name());
    public final Element originVally = new Element("Origin Valley",227,1,"MEDIUM");
    public final Element outback = new Element("Outback",228,7,true,"HOT");
    public final Element overgrownCliffs = new Element("Overgrown Cliffs",255,7,true,ClimateDistribution.MEDIUM.name());
    public final Element pasture = new Element("Pasture",54,7,true,"WARM");
    public final Element prairie = new Element("Prairie",231,7,true,"WARM");
    public final Element quagmire = new Element("Quagmire",232,3,"COOL");
    public final Element rainforest = new Element("Rainforest",233,5,true,"WARM");
    public final Element redwoodForest = new Element("Redwood Forest",234,7,true,"COOL");
    public final Element redwoodForestEdge = new Element("Redwood Forest Edge",60,0,true,"COOL");
    public final Element sacredSprings = new Element("Sacred Springs",235,3,ClimateDistribution.MEDIUM.name());
    public final ID savanna = new ID("Savanna",236);
    public final ID savannaPlateau = new ID("Savanna Plateau (Sub-Biome)",61);
    public final Element seasonalForest = new Element("Seasonal Forest",238,true,"COOL");
    public final Element shield = new Element("Shield",239,7,true,"COOL");
    public final Element shrubland = new Element("Shrubland",241,true,"COOL");
    public final Element snowyConiferousForest = new Element("Snowy Coniferous Forest",51,true,"SNOWY");
    public final Element snowyForest = new Element("Snowy Forest",51,7,true,"SNOWY");
    public final Element snowyTundra = new Element("Snowy Tundra",52,7,true,"SNOWY");
    public final Element steppe = new Element("Steppe",244,7,true,Climate.COOL.name);
    public final Element temperateRainforest = new Element("Temperate Rainforest",245,5,true,"WARM");
    public final Element tropicalRainforest = new Element("Tropical Rainforest",248,5,true,"HOT");
    public final Element tropicalIslands = new Element("Tropical Islands",249,1,true,"DEEP_OCEAN");
    public final Element tundra = new Element("Tundra",250,7,true,"SNOWY");
    public final Element volcano = new Element("Volcanic Island",251,1,true,"DEEP_OCEAN");
    public final Element wasteland = new Element("Wasteland",252,3,"HOT");
    public final Element wetland = new Element("Wetland",253,7,true,"WARM");
    public final Element woodland = new Element("Woodland",254,true,"WARM");
    public final Element eucalyptusForest = new Element("Eucalyptus Forest",57,false,"WARM");
    public final Element landOfLakes = new Element("Land of Lakes",68,7,false,"COOL");
    public final Element xericShrubland = new Element("Xeric Shrubland",98,false,"HOT");

    public BoPSettings() {
        super("BoP");
    }
    
    @Override
    public boolean activeIn(WorldType worldType) {
        // currently used for BoP to exclude from non-BoP non-RTG worldTypes
        if (worldType.getName().equalsIgnoreCase("BIOMESOP")) return true;
        if (worldType.getName().equalsIgnoreCase("RTG")) return true;
        return false;
    }
    
    Acceptor<Integer> crashOnZero = new Acceptor<Integer>() {

        @Override
        public void accept(Integer accepted) {
            throw new RuntimeException("Thicket incidence "+accepted);
        }
    };
    
    Acceptor<Integer> crashOnNegative = new Acceptor<Integer>() {

        @Override
        public void accept(Integer accepted) {
            throw new RuntimeException("Thicket ID "+accepted);
        }
    };

    //private setBoPID()

    private int bopID(Optional<Biome> bopBiome) {
        try {
            return Biome.getIdForBiome(bopBiome.get());
        } catch (IllegalStateException e) {
            return -1;
        }
    }
    
    @Override
    public void setNativeBiomeIDs(File configDirectory) {
        try{
            this.alps.biomeID().set(bopID(BOPBiomes.alps));//biomesoplenty:alps
            this.alpsFoothills.biomeID().set(BiomeSettings.extractBiomeID("biomesoplenty:alps_foothills"));
            this.bambooForest.biomeID().set(bopID(BOPBiomes.bamboo_forest));//biomesoplenty:bamboo_forest
            this.bayou.biomeID().set(bopID(BOPBiomes.bayou));//biomesoplenty:bayou
            this.bog.biomeID().set(bopID(BOPBiomes.bog));//biomesoplenty:bog
            this.borealForest.biomeID().set(bopID(BOPBiomes.boreal_forest));//biomesoplenty:boreal_forest
            this.brushland.biomeID().set(bopID(BOPBiomes.brushland));//biomesoplenty:brushland
            this.chaparral.biomeID().set(bopID(BOPBiomes.chaparral));//biomesoplenty:chaparral
            this.cherryBlossomGrove.biomeID().set(bopID(BOPBiomes.cherry_blossom_grove));//biomesoplenty:cherry_blossom_grove
            this.coniferousForest.biomeID().set(bopID(BOPBiomes.coniferous_forest)); //biomesoplenty:coniferous_forest
            this.coldDesert.biomeID().set(bopID(BOPBiomes.cold_desert));//biomesoplenty:cold_desert
            this.coralReef.biomeID().set(bopID(BOPBiomes.coral_reef));//biomesoplenty:coral_reef
            this.crag.biomeID().set(bopID(BOPBiomes.crag));//biomesoplenty:crag
            this.deadForest.biomeID().set(bopID(BOPBiomes.dead_forest));//biomesoplenty:dead_forest
            this.deadSwamp.biomeID().set(bopID(BOPBiomes.dead_swamp));//biomesoplenty:dead_swamp
            this.eucalyptusForest.biomeID().set(bopID(BOPBiomes.eucalyptus_forest));//biomesoplenty:eucalyptus_forest
            this.fen.biomeID().set(bopID(BOPBiomes.fen));//biomesoplenty:fen
            this.flowerField.biomeID().set(bopID(BOPBiomes.flower_field));//biomesoplenty:flower_field
            this.flowerIsland.biomeID().set(bopID(BOPBiomes.flower_island));//biomesoplenty:flower_island
            this.grassland.biomeID().set(bopID(BOPBiomes.grassland));//biomesoplenty:grassland
            this.glacier.biomeID().set(bopID(BOPBiomes.glacier));//biomesoplenty:glacier
            this.gravelBeach.biomeID().set(bopID(BOPBiomes.gravel_beach));//biomesoplenty:gravel_beach
            this.grove.biomeID().set(bopID(BOPBiomes.grove));//biomesoplenty:grove
            this.highland.biomeID().set(bopID(BOPBiomes.highland));//biomesoplenty:highland
            this.kelpForest.biomeID().set(bopID(BOPBiomes.kelp_forest));//biomesoplenty:kelp_forest
            this.landOfLakes.biomeID().set(bopID(BOPBiomes.land_of_lakes));//biomesoplenty:land_of_lakes
            this.lavenderFields.biomeID().set(bopID(BOPBiomes.lavender_fields));//iomesoplenty:lavender_fields
            this.lushDesert.biomeID().set(bopID(BOPBiomes.lush_desert));//biomesoplenty:lush_desert
            this.lushSwamp.biomeID().set(bopID(BOPBiomes.lush_swamp));//biomesoplenty:lush_swamp
            this.mangrove.biomeID().set(bopID(BOPBiomes.mangrove));//biomesoplenty:mangrove
            this.mapleWoods.biomeID().set(bopID(BOPBiomes.maple_woods));//biomesoplenty:maple_woods
            this.marsh.biomeID().set(bopID(BOPBiomes.marsh));//biomesoplenty:marsh
            this.meadow.biomeID().set(bopID(BOPBiomes.meadow));//biomesoplenty:meadow
            this.mysticGrove.biomeID().set(bopID(BOPBiomes.mystic_grove));//biomesoplenty:mystic_grove
            this.moor.biomeID().set(bopID(BOPBiomes.moor));//biomesoplenty:moor
            this.mountain.biomeID().set(bopID(BOPBiomes.mountain));//biomesoplenty:mountain
            this.mountainFoothills.biomeID().set(bopID(BOPBiomes.mountain_foothills));//biomesoplenty:mountain_foothills
            this.ominousWoods.biomeID().set(bopID(BOPBiomes.ominous_woods));//biomesoplenty:ominous_woods
            this.oasis.biomeID().set(bopID(BOPBiomes.oasis));//biomesoplenty:oasis
            this.orchard.biomeID().set(bopID(BOPBiomes.orchard));//biomesoplenty:orchard
            this.originVally.biomeID().set(bopID(BOPBiomes.origin_island));//biomesoplenty:origin_island
            this.outback.biomeID().set(bopID(BOPBiomes.outback));//biomesoplenty:outback
            this.overgrownCliffs.biomeID().set(bopID(BOPBiomes.overgrown_cliffs));//biomesoplenty:overgrown_cliffs
            this.pasture.biomeID().set(BiomeSettings.extractBiomeID("biomesoplenty:pasture"));
            this.prairie.biomeID().set(bopID(BOPBiomes.prairie));//biomesoplenty:prairie
            this.quagmire.biomeID().set(bopID(BOPBiomes.quagmire));//biomesoplenty:quagmire
            this.rainforest.biomeID().set(bopID(BOPBiomes.rainforest));//biomesoplenty:rainforest
            this.redwoodForest.biomeID().set(bopID(BOPBiomes.redwood_forest));//biomesoplenty:redwood_forest
            this.redwoodForestEdge.biomeID().set(BiomeSettings.extractBiomeID("biomesoplenty:redwood_forest_edge"));
            this.sacredSprings.biomeID().set(bopID(BOPBiomes.sacred_springs));//biomesoplenty:sacred_springs
            this.savanna.setIDFrom(Biomes.SAVANNA);
            this.savannaPlateau.setIDFrom(Biomes.SAVANNA_PLATEAU);
            this.seasonalForest.biomeID().set(bopID(BOPBiomes.seasonal_forest));//biomesoplenty:seasonal_forest
            this.shield.biomeID().set(bopID(BOPBiomes.shield));//biomesoplenty:shield
            this.shrubland.biomeID().set(bopID(BOPBiomes.shrubland));//biomesoplenty:shrubland
            this.snowyConiferousForest.biomeID().set(bopID(BOPBiomes.snowy_coniferous_forest));//biomesoplenty:snowy_coniferous_forest
            this.snowyForest.biomeID().set(bopID(BOPBiomes.snowy_forest));//biomesoplenty:snowy_forest
            this.snowyTundra.biomeID().set(BiomeSettings.extractBiomeID("biomesoplenty:snowy_tundra"));
            this.steppe.biomeID().set(bopID(BOPBiomes.steppe));//biomesoplenty:steppe
            this.temperateRainforest.biomeID().set(bopID(BOPBiomes.temperate_rainforest));//biomesoplenty:temperate_rainforest
            this.tropicalRainforest.biomeID().set(bopID(BOPBiomes.tropical_rainforest));//biomesoplenty:tropical_rainforest
            this.tropicalIslands.biomeID().set(bopID(BOPBiomes.tropical_island));//biomesoplenty:tropical_island
            this.tundra.biomeID().set(bopID(BOPBiomes.tundra));//biomesoplenty:tundra
            this.volcano.biomeID().set(bopID(BOPBiomes.volcanic_island));//biomesoplenty:volcanic_island
            this.wasteland.biomeID().set(bopID(BOPBiomes.wasteland));//biomesoplenty:wasteland
            this.wetland.biomeID().set(bopID(BOPBiomes.wetland));//biomesoplenty:wetland
            this.woodland.biomeID().set(bopID(BOPBiomes.woodland));//biomesoplenty:woodland
            this.xericShrubland.biomeID().set(bopID(BOPBiomes.xeric_shrubland));//biomesoplenty:xeric_shrubland
            //
            //
            //biomesoplenty:origin_beach
            //biomesoplenty:white_beach
            
        }
    catch (java.lang.NoClassDefFoundError e) {
        } 
    }
    // Given a biomeId, return a replacer with similar results to the BoP routines
    public BiomeReplacer.Variable getCommonSubBiome(ID biome)
    {

        List<Integer> subBiomeIds = ModBiomes.subBiomesMap.get(biome.biomeID());
        BiomeReplacer.Variable result = new BiomeReplacer.Variable();
        if ((subBiomeIds == null)||(subBiomeIds.size() == 0)) {
            // no sub-biomes. Put in one copy of itself in case something wants to fiddle
            result.add(biome, 1);
        } else {
            int n = subBiomeIds.size();
            for (int i = 0; i < n ; i++ ) {
                // put in all the alternatives as equiprobable
                // because I don't supress sub-biomes on edges add in copies of the biomes itself
                result.add(biome, 1);
                result.addByNumber(subBiomeIds.get(i), 1);
            }
        }
        return result;
    }

    // if this generates a NPE arrangeInteractions hasn't been run
    private HashMap<ID,BiomeReplacer.Variable> subBiomeSets = new HashMap<ID,BiomeReplacer.Variable>();

    private BiomeReplacer.Variable subBiomeSet(ID biome) {
        BiomeReplacer.Variable result = subBiomeSets.get(biome);
        if (result == null) {
            result = getCommonSubBiome(biome);
            subBiomeSets.put(biome, result);
        }
        return result;
    }

    private void addSubBiome(ID biome, ID subBiome){
        if (subBiome.active()) {
            //BiomeSettings.logger.info("adding "+subBiome + " to "+ biome);

            subBiomeSet(biome).add(subBiome, 1);
            biome.setSubBiomeChooser(subBiomeSet(biome));
        }
    }

    @Override
    public void arrangeInteractions(ArrayList<BiomeSettings> biomeSettings) {
        //if (1>0) throw new RuntimeException();
        subBiomeSets = new HashMap<ID,BiomeReplacer.Variable>();
        for (BiomeSettings biomeSetting: biomeSettings) {
            if (biomeSetting instanceof OceanBiomeSettings) {
                OceanBiomeSettings oceanSettings = (OceanBiomeSettings)biomeSetting;
                BiomeReplacer oldReplacer = oceanSettings.coastalOcean.subBiomeChooser();
                BiomeReplacer.Variable newReplacer = new BiomeReplacer.Variable();
                if (coralReef.active()) {
                    newReplacer.add(this.coralReef, 1);
                    newReplacer.add(oceanSettings.coastalOcean, 1);
                }
                if (kelpForest.active()) {
                    newReplacer.add(this.kelpForest, 1);
                    newReplacer.add(oceanSettings.coastalOcean, 1);
                }
                if (coralReef.active()||kelpForest.active()) oceanSettings.coastalOcean.setSubBiomeChooser(newReplacer);
            }
            if (biomeSetting instanceof VanillaBiomeSettings) {
                VanillaBiomeSettings vanillaSettings = (VanillaBiomeSettings)biomeSetting;
                BiomeReplacer icePlainsReplacer = vanillaSettings.icePlains.subBiomeChooser();
                if (icePlainsReplacer instanceof BiomeReplacer.Fixed) {
                    BiomeReplacer.Variable newReplacer = new BiomeReplacer.Variable();
                    newReplacer.add(this.glacier, 1);
                    newReplacer.add(vanillaSettings.iceMountains, 1);
                    vanillaSettings.icePlains.setSubBiomeChooser(newReplacer);
                }
                BiomeReplacer desertReplacer = vanillaSettings.desert.subBiomeChooser();
                if (desertReplacer instanceof BiomeReplacer.Fixed) {
                    BiomeReplacer.Variable newReplacer = new BiomeReplacer.Variable();
                    newReplacer.add(this.oasis, 1);
                    newReplacer.add(vanillaSettings.desertHills, 3);
                    vanillaSettings.desert.setSubBiomeChooser(newReplacer);
                }
            }
        }
    }


    @Override
    public void update(SubBiomeChooser subBiomeChooser) {
        super.update(subBiomeChooser);
        for (Element element: this.elements()) {
            subBiomeChooser.set(element.biomeID().value(), subBiomeSet(element));
        }
    }

    @Override
    public void setRules(ClimateControlRules rules) {
        rules.noBeaches(this.coralReef.biomeID().value());
        rules.noBeaches(this.kelpForest.biomeID().value());
        rules.disallowRivers(this.alps.biomeID().value());
        rules.disallowRivers(this.alpsFoothills.biomeID().value());
        rules.disallowRivers(this.mountain.biomeID().value());
        rules.disallowRivers(this.mountainFoothills.biomeID().value());
        rules.disallowRivers(this.overgrownCliffs.biomeID().value());
        setVillages(rules);
        // nothing yet
    }
    static final String biomesOnName = "BoPBiomesOn";

    public final Mutable<Boolean> biomesFromConfig = climateControlCategory.booleanSetting(
                        biomesOnName, "", false);

    static final String configName = "BoP";
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
    
    @Override  
    public GenLayer subBiomeEdges(long seed, GenLayer parent, int biomeCount) {
    	return new GenLayerBopBiomeEdge(seed, parent, biomeCount);
    }
}
