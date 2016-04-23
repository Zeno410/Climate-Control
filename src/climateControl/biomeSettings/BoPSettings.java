
package climateControl.biomeSettings;

import climateControl.api.BiomeSettings;
import biomesoplenty.api.content.BOPCBiomes;
import climateControl.api.Climate;
import climateControl.api.ClimateControlRules;
import climateControl.api.ClimateControlSettings;
import climateControl.api.ClimateDistribution;
import climateControl.utils.Acceptor;
import climateControl.utils.Mutable;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import net.minecraft.world.biome.BiomeGenBase;

/**
 *
 * @author Zeno410
 */
public class BoPSettings extends BiomeSettings {


    //public final ID alpsForestSubBiome = new ID("Alps Forest (Sub-Biome)",63);
    public final Element alpsForest = new Element("Alps Forest",99,0,Climate.SNOWY.name);
    public final Element alps = new Element("Alps",177,5,Climate.SNOWY.name);
    //public final ID alpsMountainside = new ID("Alps Mountainside (Sub-Biome)",64);
    public final Element arctic = new Element("Arctic",178,10,true,Climate.SNOWY.name);
    //public final ID autumnHills = new ID("Autumn Hills",58);
    //public final ID badlands = new ID("Badlands",179);
    public final Element bambooForest = new Element("Bamboo Forest",180,5,true,"WARM");
    public final Element bayou = new Element("Bayou",181,10,true,"WARM");
    //public final ID birchForest = new ID("Birch Forest",182);
    //public final ID bloodyHeap = new ID("Bloody Heap (Nether)",84);
    public final Element bog = new Element("Bog",183,7,true,"COOL");
    //public final ID boneyardNether = new ID("Boneyard (Nether)",83);
    public final ID boneyard = new ID("Boneyard",111);
    public final Element borealForest = new Element("Boreal Forest",184,10,true,"COOL");
    public final ID brushland = new ID("Brushland",185,true);
    public final Element canyon = new Element("Canyon",186,7,"HOT");
    public final ID canyonRavineSubBiome = new ID("Canyon Ravine (Sub-Biome)",70);
    public final ID canyonRavine = new ID("Canyon Ravine",100);
    public final Element chaparral = new Element("Chaparral",187,10,true,"WARM");
    public final Element cherryBlossomGrove = new Element("Cherry Blossom Grove",188,3,true,"COOL");
    public final ID coniferousForestSnow = new ID("Coniferous Forest (Snow)",190);
    public final Element coniferousForest = new Element("Coniferous Forest",189,10,true,"WARM");
    //public final ID coralReefOcean = new ID("Coral Reef (Ocean)",73);
    public final Element coralReef = new Element("Coral Reef",94,0,"OCEAN");
    public final ID corruptedSandsNether = new ID("Corrupted Sands (Nether)",81);
    public final ID corruptedSands = new ID("Corrupted Sands",109);
    public final Element crag = new Element("Crag",191,3,"MEDIUM");
    //public final ID deadForestSnow = new ID("Dead Forest (Snow)",193);
    public final Element deadForest = new Element("Dead Forest",192,7,true,"COOL");
    public final Element deadSwamp = new Element("Dead Swamp",194,7,true,"WARM");
    //public final ID deadlands = new ID("Deadlands",195);
    public final Element deciduousForest = new Element("Deciduous Forest",196,10,true,"WARM");
    public final ID dryRiver = new ID("Dry River",115);
    //public final ID dunes = new ID("Dunes",197);
    public final Element fen = new Element("Fen",198,true,"WARM");
    public final Element flowerField = new Element("Flower Field",199,3,true,"WARM");
    public final Element frostForest = new Element("Frost Forest",200,7,true,"SNOWY");
    public final Element fungiForest = new Element("Fungi Forest",201,3);
    public final Element garden = new Element("Garden",202,3,ClimateDistribution.MEDIUM.name());
    public final ID glacier = new ID("Glacier",203);
    public final Element grassland = new Element("Grassland",204,true,"COOL");
    public final ID gravelBeach = new ID("Gravel Beach",75);
    public final Element grove = new Element("Grove",205,5,"COOL");
    public final Element heathland = new Element("Heathland",206,"COOL");
    public final Element highland = new Element("Highland",210,"COOL");
    //public final ID hotSprings = new ID("Hot Springs",211);
    //public final ID icyHills = new ID("Icy Hills",212);
    public final Element jadeCliffs = new Element("Jade Cliffs",213,5,"WARM");
    //public final ID kelpForestOcean = new ID("Kelp Forest (Ocean)",74);
    public final Element kelpForest = new Element("Kelp Forest",95,0,"OCEAN");
    public final Element lavenderFields = new Element("Lavender Fields",56,3,"WARM");
    public final Element lushDesert = new Element("Lush Desert",214,3,true,"HOT");
    public final ID lushRiver = new ID("Lush River",114);
    public final Element lushSwamp = new Element("Lush Swamp",215,true,"WARM");
    public final Element mangrove = new Element("Mangrove",216,0,"WARM");
    public final Element mapleWoods = new Element("Maple Woods",217,true,"COOL");
    public final Element marsh = new Element("Marsh",218,7,true,"COOL");
    //public final ID meadowForestSubBiome = new ID("Meadow Forest (Sub-Biome)",71);
    public final ID meadowForest = new ID("Meadow Forest",102);
    public final Element meadow = new Element("Meadow",219,true,"COOL");
    //public final ID mesa = new ID("Mesa",220);
    public final Element moor = new Element("Moor",221,true,"COOL");
    public final Element mountain = new Element("Mountain",222,"MEDIUM");
    public final Element mysticGrove = new Element("Mystic Grove",223,3,"MEDIUM");
    public final ID oasis = new ID("Oasis",224);
    //public final ID oceanicAbyss = new ID("Oceanic Abyss (Ocean)",72);
    public final Element ominousWoods = new Element("Ominous Woods",225,3,"COOL");
    public final Element orchard = new Element("Orchard",226,0,ClimateDistribution.MEDIUM.name());
    public final Element originVally = new Element("Origin Valley",227,1,"MEDIUM");
    public final Element outback = new Element("Outback",228,7,true,"HOT");
    //public final ID overgrownBeach = new ID("Overgrown Beach",76);
    //public final ID overgrownGreens = new ID("Overgrown Greens",59);
    //public final ID pasture = new ID("Pasture",229);
    //public final ID pastureMeadowSubBiome = new ID("Pasture Meadow (Sub-Biome)",67);
    //public final ID phantasmagoricInfernoNether = new ID("Phantasmagoric Inferno (Nether)",82);
    public final ID phantasmagoricInferno = new ID("Phantasmagoric Inferno",110);
    //public final ID polar = new ID("Polar",230);
    public final Element prairie = new Element("Prairie",231,true,"WARM");
    public final ID quagmire = new ID("Quagmire",232);
    public final Element rainforest = new Element("Rainforest",233,5,true,"WARM");
    public final Element redwoodForest = new Element("Redwood Forest",234,7,true,"COOL");
    public final Element sacredSprings = new Element("Sacred Springs",235,3,ClimateDistribution.MEDIUM.name());
    public final ID savanna = new ID("Savanna",236);
    public final ID savannaPlateau = new ID("Savanna Plateau (Sub-Biome)",61);
    public final ID scrubland = new ID("Scrubland",237);
    public final Element seasonalForest = new Element("Seasonal Forest",238,true,"COOL");
    public final ID seasonalSpruceForest = new ID("Seasonal Spruce Forest (Sub-Biome)",62);
    public final Element shield = new Element("Shield",239,7,true,"COOL");
    public final ID shore = new ID("Shore",240);
    public final Element shrubland = new Element("Shrubland",241,true,"COOL");
    public final Element silkglades = new Element("Silkglades",60,0,"COOL");
    public final Element sludgepit = new Element("Sludgepit",242,5,true,"WARM");
    public final Element snowyConiferousForest = new Element("Snowy Coniferous Forest",51,true,"SNOWY");
    public final ID spruceWoods = new ID("Spruce Woods",243,true);
    public final Element steppe = new Element("Steppe",244,7,true,Climate.COOL.name);
    public final Element temperateRainforest = new Element("Temperate Rainforest",245,true,"WARM");
    //public final ID thickOminousWoodsSubBiome = new ID("Thick Ominous Woods (Sub-Biome)",68);
    //public final ID thickShrublandSubBiome = new ID("Thick Shrubland (Sub-Biome)",69);
    public final Element thicket = new Element("Thicket",246,5,true,"COOL");
    //public final ID thinnedMysticGroveSubBiome = new ID("Thinned Mystic Grove (Sub-Biome)",55);
    //public final ID thinnedPastureSubBiome = new ID("Thinned Pasture (Sub-Biome)",66);
    //public final ID thinnedTimberSubBiome = new ID("Thinned Timber (Sub-Biome)",65);
    //public final ID timber = new ID("Timber",247);
    public final Element tropicalRainforest = new Element("Tropical Rainforest",248,5,true,"HOT");
    public final Element tropics = new Element("Tropics",249,0,true,"HOT");
    //public final ID tropicsMountainSubBiome = new ID("Tropics Mountain (Sub-Biome)",57);
    public final Element tundra = new Element("Tundra",250,7,true,"SNOWY");
    //public final ID undergardenNether= new ID("Undergarden (Nether)",80);
    public final ID undergarden = new ID("Undergarden",113);
    public final ID visceralHeap = new ID("Visceral Heap",112);
    public final ID volcano = new ID("Volcano",251);
    public final Element wasteland = new Element("Wasteland",252,3,"HOT");
    public final Element wetland = new Element("Wetland",253,7,true,"WARM");
    public final Element woodland = new Element("Woodland",254,true,"WARM");

    public final Element denseForest = new Element("Dense Forest",56,false,"COOL");
    public final Element eucalyptusForest = new Element("Eucalyptus Forest",57,false,"WARM");
    public final Element landOfLakes = new Element("Land of Lakes",68,false,"COOL");
    public final ID landOfLakesMarsh = new Element("Land of Lakes Marsh",101,false,"COOL");
    public final Element xericShrubland = new Element("Xeric Shrubland",98,false,"HOT");

    public BoPSettings() {
        super("BoP");
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

    @Override
    public void setNativeBiomeIDs(File configDirectory) {
        try{
            this.alps.setIDFrom(BOPCBiomes.alps);
            this.alpsForest.setIDFrom(BOPCBiomes.alpsForest);
            this.arctic.setIDFrom(BOPCBiomes.arctic);
            this.bambooForest.setIDFrom(BOPCBiomes.bambooForest);
            this.bayou.setIDFrom(BOPCBiomes.bayou);
            this.bog.setIDFrom(BOPCBiomes.bog);
            this.boneyard.setIDFrom(BOPCBiomes.boneyard);
            this.borealForest.setIDFrom(BOPCBiomes.borealForest);
            this.brushland.setIDFrom(BOPCBiomes.brushland);
            this.canyon.setIDFrom(BOPCBiomes.canyon);
            this.canyonRavine.setIDFrom(BOPCBiomes.canyonRavine);
            this.chaparral.setIDFrom(BOPCBiomes.chaparral);
            this.cherryBlossomGrove.setIDFrom(BOPCBiomes.cherryBlossomGrove);
            this.coniferousForest.setIDFrom(BOPCBiomes.coniferousForest);
            this.coralReef.setIDFrom(BOPCBiomes.coralReef);
            this.corruptedSands.setIDFrom(BOPCBiomes.corruptedSands);
            this.crag.setIDFrom(BOPCBiomes.crag);
            this.deadForest.setIDFrom(BOPCBiomes.deadForest);
            this.deadSwamp.setIDFrom(BOPCBiomes.deadSwamp);
            this.deciduousForest.setIDFrom(BOPCBiomes.deciduousForest);
            this.dryRiver.setIDFrom(BOPCBiomes.dryRiver);
            this.fen.setIDFrom(BOPCBiomes.fen);
            this.flowerField.setIDFrom(BOPCBiomes.flowerField);
            this.frostForest.setIDFrom(BOPCBiomes.frostForest);
            this.fungiForest.setIDFrom(BOPCBiomes.fungiForest);
            this.garden.setIDFrom(BOPCBiomes.garden);
            this.grassland.setIDFrom(BOPCBiomes.grassland);
            this.glacier.setIDFrom(BOPCBiomes.glacier);
            this.grove.setIDFrom(BOPCBiomes.grove);
            this.heathland.setIDFrom(BOPCBiomes.heathland);
            this.highland.setIDFrom(BOPCBiomes.highland);
            this.jadeCliffs.setIDFrom(BOPCBiomes.jadeCliffs);
            this.kelpForest.setIDFrom(BOPCBiomes.kelpForest);
            this.lavenderFields.setIDFrom(BOPCBiomes.lavenderFields);
            this.lushDesert.setIDFrom(BOPCBiomes.lushDesert);
            this.lushRiver.setIDFrom(BOPCBiomes.lushRiver);
            this.lushSwamp.setIDFrom(BOPCBiomes.lushSwamp);
            this.mangrove.setIDFrom(BOPCBiomes.mangrove);
            this.mapleWoods.setIDFrom(BOPCBiomes.mapleWoods);
            this.marsh.setIDFrom(BOPCBiomes.marsh);
            this.meadow.setIDFrom(BOPCBiomes.meadow);
            this.meadowForest.setIDFrom(BOPCBiomes.meadowForest);
            this.mysticGrove.setIDFrom(BOPCBiomes.mysticGrove);
            this.moor.setIDFrom(BOPCBiomes.moor);
            this.mountain.setIDFrom(BOPCBiomes.mountain);
            this.ominousWoods.setIDFrom(BOPCBiomes.ominousWoods);
            this.oasis.setIDFrom(BOPCBiomes.oasis);
            this.orchard.setIDFrom(BOPCBiomes.orchard);
            this.originVally.setIDFrom(BOPCBiomes.originValley);
            this.outback.setIDFrom(BOPCBiomes.outback);
            this.phantasmagoricInferno.setIDFrom(BOPCBiomes.phantasmagoricInferno);
            this.prairie.setIDFrom(BOPCBiomes.prairie);
            this.quagmire.setIDFrom(BOPCBiomes.quagmire);
            this.rainforest.setIDFrom(BOPCBiomes.rainforest);
            this.redwoodForest.setIDFrom(BOPCBiomes.redwoodForest);
            this.sacredSprings.setIDFrom(BOPCBiomes.sacredSprings);
            this.savanna.setIDFrom(BiomeGenBase.savanna);
            this.savannaPlateau.setIDFrom(BiomeGenBase.savannaPlateau);
            this.scrubland.setIDFrom(BOPCBiomes.scrubland);
            this.seasonalForest.setIDFrom(BOPCBiomes.seasonalForest);
            this.shield.setIDFrom(BOPCBiomes.shield);
            this.shrubland.setIDFrom(BOPCBiomes.shrubland);
            this.sludgepit.setIDFrom(BOPCBiomes.sludgepit);
            this.snowyConiferousForest.setIDFrom(BOPCBiomes.snowyConiferousForest);
            this.silkglades.setIDFrom(BOPCBiomes.silkglades);
            this.spruceWoods.setIDFrom(BOPCBiomes.spruceWoods);
            this.steppe.setIDFrom(BOPCBiomes.steppe);
            this.snowyConiferousForest.setIDFrom(BOPCBiomes.snowyConiferousForest);
            this.temperateRainforest.setIDFrom(BOPCBiomes.temperateRainforest);
            this.thicket.setIDFrom(BOPCBiomes.thicket);
            this.tropicalRainforest.setIDFrom(BOPCBiomes.tropicalRainforest);
            this.tropics.setIDFrom(BOPCBiomes.tropics);
            this.tundra.setIDFrom(BOPCBiomes.tundra);
            this.undergarden.setIDFrom(BOPCBiomes.undergarden);
            this.visceralHeap.setIDFrom(BOPCBiomes.visceralHeap);
            this.volcano.setIDFrom(BOPCBiomes.volcano);
            this.wasteland.setIDFrom(BOPCBiomes.wasteland);
            this.wetland.setIDFrom(BOPCBiomes.wetland);
            this.woodland.setIDFrom(BOPCBiomes.woodland);
            try {
                this.denseForest.setIDFrom(BOPCBiomes.denseForest);
            } catch (java.lang.NoSuchFieldError e) {
                // disable the "new" biomes in case of "old" BoP
                this.denseForest.biomeID().set(-1);
            }
            try {
                this.eucalyptusForest.setIDFrom(BOPCBiomes.eucalyptusForest);
            } catch (java.lang.NoSuchFieldError e) {
                // disable the "new" biomes in case of "old" BoP
                this.eucalyptusForest.biomeID().set(-1);
            }
            try {
                this.landOfLakes.setIDFrom(BOPCBiomes.landOfLakes);
            } catch (java.lang.NoSuchFieldError e) {
                // disable the "new" biomes in case of "old" BoP
                this.landOfLakes.biomeID().set(-1);
            }
            try {
                this.landOfLakesMarsh.setIDFrom(BOPCBiomes.landOfLakesMarsh);
            } catch (java.lang.NoSuchFieldError e) {
                // disable the "new" biomes in case of "old" BoP
                this.landOfLakesMarsh.biomeID().set(-1);
            }
            try {
                this.xericShrubland.setIDFrom(BOPCBiomes.xericShrubland);
            } catch (java.lang.NoSuchFieldError e) {
                // disable the "new" biomes in case of "old" BoP
                this.xericShrubland.biomeID().set(-1);
            }

        }
    catch (java.lang.NoClassDefFoundError e) {
        } 
    }

    private HashMap<ID,BiomeReplacer.Variable> subBiomeSets;

    private BiomeReplacer.Variable subBiomeSet(ID biome) {
        BiomeReplacer.Variable result = subBiomeSets.get(biome);
        if (result == null) {
            result = new BiomeReplacer.Variable();
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

    public void setSubBiomes(ClimateControlSettings settings) {
        subBiomeSets = new HashMap<ID,BiomeReplacer.Variable>();
        ArrayList<BiomeSettings> biomeSettings = settings.biomeSettings();
        for (BiomeSettings biomeSetting: biomeSettings) {
            if (biomeSetting instanceof OceanBiomeSettings) {
                OceanBiomeSettings oceanSettings = (OceanBiomeSettings)biomeSetting;
                addSubBiome(oceanSettings.coastalOcean,this.coralReef);
                addSubBiome(oceanSettings.coastalOcean,this.kelpForest);
            }
        }
    }

    @Override
    public void setRules(ClimateControlRules rules) {
        rules.noBeaches(this.coralReef.biomeID().value());
        rules.noBeaches(this.kelpForest.biomeID().value());
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
}
