package climateControl.biomeSettings;

import climateControl.api.BiomeSettings;
import climateControl.api.Climate;
import climateControl.api.ClimateControlRules;
import climateControl.api.ClimateDistribution.Incidence;
import climateControl.generator.SubBiomeChooser;
import climateControl.utils.Mutable;
import com.google.common.base.Optional;
import enhancedbiomes.EnhancedBiomesMod;
import enhancedbiomes.world.biome.EnhancedBiomesBiome;
//import enhancedbiomes.api.EBAPI;
import climateControl.utils.Settings;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import net.minecraftforge.common.config.Configuration;


public class EBBiomeSettings extends BiomeSettings {
    public static final String biomeCategory = "EBBiomes";
    public static final String EBCategory = "EBSettings";
    public final Category EBSettings = new Category(EBCategory);

    NativeSettings settings = new NativeSettings();
    public final Element AlpineMountains = new Element("Alpine Mountains",52,10,Climate.SNOWY.name);
	public final Element AlpineMountainsEdge = new Element("Alpine Mountains Edge",86,0,Climate.SNOWY.name);
	public final Element AlpineMountainsM = new Element("Alpine Mountains M",180,0,Climate.SNOWY.name);
	public final Element AlpineTundra = new Element("Alpine Tundra",103,10,Climate.SNOWY.name);
	public final Element AspenForest = new Element("Aspen Forest",97,10,Climate.COOL.name);
	public final Element AspenHills = new Element("Aspen Hills",98,0,Climate.COOL.name);
	public final Element Badlands = new Element("Badlands",71,10,Climate.WARM.name);
	public final Element Basin = new Element("Basin",99,0,Climate.COOL.name);
	public final Element BlossomHills = new Element("Blossom Hills",76,0,Climate.WARM.name);
	public final Element BlossomWoods = new Element("Blossom Woods",75,10,Climate.WARM.name);
	public final Element BorealArchipelago = new Element("Boreal Archipelago",169,1,Climate.DEEP_OCEAN.name);
	public final Element BorealForest = new Element("Boreal Forest",46,10,Climate.COOL.name);
	public final Element BorealPlateau = new Element("Boreal Plateau",60,10,Climate.COOL.name);
	public final Element BorealPlateauM = new Element("Boreal Plateau M",188,0,Climate.COOL.name);
	public final Element Carr = new Element("Carr",93,10,Climate.WARM.name);
	public final Element ClayHills = new Element("Clay Hills",90,10,Climate.WARM.name);
	public final Element Clearing = new Element("Clearing",101,0,Climate.WARM.name);
	public final Element ColdBorealForest = new Element("Cold Boreal Forest",174,0,Climate.SNOWY.name);
	public final Element ColdCypressForest = new Element("Cold Cypress Forest",173,0,Climate.SNOWY.name);
	public final Element ColdFirForest = new Element("Cold Fir Forest",182,0,Climate.SNOWY.name);
	public final Element ColdPineForest = new Element("Cold Pine Forest",189,0,Climate.SNOWY.name);
	public final Element CreekBed = new Element("Creek Bed",69,0,Climate.SNOWY.name);
	public final Element CypressForest = new Element("Cypress Forest",45,10,Climate.WARM.name);
	public final Element DesertArchipelago = new Element("Desert Archipelago",63,10,Climate.OCEAN.name);
	public final Element EphemeralLake = new Element("Ephemeral Lake",58,10,Climate.WARM.name);
	public final Element EphemeralLakeEdge = new Element("Ephemeral Lake Edge",59,0,Climate.WARM.name);
	public final Element Fens = new Element("Fens",92,10,Climate.COOL.name);
	public final Element FirForest = new Element("Fir Forest",54,10,Climate.COOL.name);
	public final Element FloweryArchipelago = new Element("Flowery Archipelago",168,1,Climate.DEEP_OCEAN.name);
	public final Element ForestedArchipelago = new Element("Forested Archipelago",40,1,Climate.DEEP_OCEAN.name);
	public final Element ForestedMountains = new Element("Forested Mountains",171,0,Climate.WARM.name);
	public final Element ForestedValley = new Element("Forested Valley",172,0,Climate.WARM.name);
	public final Element FrozenArchipelago = new Element("Frozen Archipelago",65,1,Climate.DEEP_OCEAN.name);
	public final Element Glacier = new Element("Glacier",87,0,Climate.SNOWY.name);
	public final Element GrassyArchipelago = new Element("Grassy Archipelago",66,1,Climate.DEEP_OCEAN.name);
	public final Element IceSheet = new Element("Ice Sheet",170,0,Climate.SNOWY.name);
	public final Element Kakadu = new Element("Kakadu",78,10,Climate.WARM.name);
	public final Element Lake = new Element("Lake",100,0,Climate.WARM.name);
	public final Element LowHills = new Element("Low Hills",85,0,Climate.WARM.name);
	public final Element Mangroves = new Element("Mangroves",91,5,Climate.OCEAN.name);
	public final Element Marsh = new Element("Marsh",102,10,Climate.WARM.name);
	public final Element Meadow = new Element("Meadow",51,10,Climate.COOL.name);
	public final Element MeadowM = new Element("Meadow M",179,0,Climate.COOL.name);
	public final Element MountainousArchipelago = new Element("Mountainous Archipelago",62,1,Climate.DEEP_OCEAN.name);
	public final Element Mountains = new Element("Mountains",79,10,Climate.COOL.name);
	public final Element MountainsEdge = new Element("Mountains Edge",80,0,Climate.COOL.name);
	public final Element OakForest = new Element("Oak Forest",77,10,Climate.WARM.name);
	public final Element Oasis = new Element("Oasis",72,0,Climate.HOT.name);
	public final Element PineForest = new Element("Pine Forest",61,10,Climate.COOL.name);
	public final Element PineForestArchipelago = new Element("Pine Forest Archipelago",41,1,Climate.DEEP_OCEAN.name);
	public final Element Plateau = new Element("Plateau",48,10,Climate.WARM.name);
	public final Element PolarDesert = new Element("Polar Desert",42,10,Climate.SNOWY.name);
	public final Element Prairie = new Element("Prairie",84,10,Climate.COOL.name);
	public final Element Rainforest = new Element("Rainforest",73,10,Climate.HOT.name);
	public final Element RainforestValley = new Element("Rainforest Valley",74,0,Climate.HOT.name);
	public final Element RedDesert = new Element("Red Desert",105,10,Climate.HOT.name);
	public final Element RiparianZone = new Element("Riparian Zone",135,0,Climate.HOT.name);
	public final Element RockyDesert = new Element("Rocky Desert",67,10,Climate.HOT.name);
	public final Element RockyHills = new Element("Rocky Hills",88,10,Climate.HOT.name);
	public final Element RoofedShrublands = new Element("Roofed Shrublands",178,0,Climate.WARM.name);
	public final Element Sahara = new Element("Sahara",70,10,Climate.HOT.name);
	public final Element SandstoneCanyon = new Element("Sandstone Canyon",56,10,Climate.HOT.name);
	public final Element SandstoneCanyons = new Element("Sandstone Canyon 2",57,10,Climate.HOT.name);
	public final Element SandstoneRanges = new Element("Sandstone Ranges",49,10,Climate.HOT.name);
	public final Element SandstoneRangesM = new Element("Sandstone Ranges M",177,0,Climate.HOT.name);
	public final Element Scree = new Element("Scree",81,0,Climate.COOL.name);
	public final Element Scrub = new Element("Scrub",68,10,Climate.WARM.name);
	public final Element Shield = new Element("Shield",89,10,Climate.COOL.name);
	public final Element Shrublands = new Element("Shrublands",50,10,Climate.COOL.name);
	public final Element SilverPineForest = new Element("Silver Pine Forest",94,10,Climate.COOL.name);
	public final Element SilverPineHills = new Element("Silver Pine Hills",95,0,Climate.COOL.name);
	public final Element SnowyDesert = new Element("Snowy Desert",181,0,Climate.SNOWY.name);
	public final Element SnowyPlateau = new Element("Snowy Plateau",176,10,Climate.SNOWY.name);
	public final Element SnowyRanges = new Element("Snowy Ranges",104,10,Climate.SNOWY.name);
	public final Element SnowyWastelands = new Element("Snowy Wastelands",183,0,Climate.SNOWY.name);
	public final Element Steppe = new Element("Steppe",83,10,Climate.COOL.name);
	public final Element StoneCanyon = new Element("Stone Canyon",184,0,Climate.COOL.name);
	public final Element StoneCanyons = new Element("Stone Canyons",185,0,Climate.COOL.name);
	public final Element TropicalArchipelago = new Element("Tropical Archipelago",64,1,Climate.DEEP_OCEAN.name);
	public final Element Tundra = new Element("Tundra",53,10,Climate.SNOWY.name);
	public final Element Volcano = new Element("Volcano",47,0,Climate.WARM.name);
	public final Element VolcanoM = new Element("Volcano M",175,0,Climate.WARM.name);
	public final Element Wastelands = new Element("Wastelands",55,10,Climate.COOL.name);
	public final Element WoodlandField = new Element("Woodland Field",96,10,Climate.WARM.name);
	public final Element WoodlandHills = new Element("Woodland Hills",44,0,Climate.WARM.name);
	public final Element WoodlandLake = new Element("Woodland Lake",186,0,Climate.WARM.name);
	public final Element WoodlandLakeEdge = new Element("Woodland Lake Edge",187,0,Climate.WARM.name);
	public final Element Woodlands = new Element("Woodlands",43,10,Climate.WARM.name);
	public final Element XericSavannah = new Element("Xeric Savannah",82,10,Climate.HOT.name);
	public final Element XericShrubland = new Element("Xeric Shrubland",106,10,Climate.HOT.name);
    
    public EBBiomeSettings() {
        super(biomeCategory);
        // started to do it manually, then looked at the config list and:
        for (Element element: elements()) {
            for (Element possibleM : elements()) {
                if (element.biomeID().value()+128 == possibleM.biomeID().value()) {
                    element.setMBiome(possibleM);
                }
            }
        }
        // note this gets run on the defaults before anything gets read so biome ID changes don't cause trouble
        /*AlpineMountains.setMBiome(this.AlpineMountainsM);
        BorealPlateau.setMBiome(BorealPlateauM);
        Meadow.setMBiome(Meadow);
        SandstoneRanges.setMBiome(SandstoneRangesM);
        Volcano.setMBiome(VolcanoM);*/
    }


    @Override
    public ArrayList<Incidence> incidences() {
        return super.incidences();
    }

    @Override
    public void setNativeBiomeIDs(File configDirectory) {
        File ebDirectory = new File(configDirectory,"Enhanced Biomes");
        if (!ebDirectory.exists()) {return;}// EB seems not to be running
        File biomeFile = new File(ebDirectory,"Biomes.cfg");
        if (!biomeFile.exists()) {return;}// EB seems not to be running
        NativeSettings nativeSettings = new NativeSettings();// we're going to throw it away for space, etc.
        nativeSettings.setIDs(new Configuration(biomeFile));
    }
        @Override
    public void update(SubBiomeChooser subBiomeChooser) {
        setUpAllSubBiomes();
        super.update(subBiomeChooser);
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

    public void setUpAllSubBiomes(){
        //imposeSubBiomes();
        subBiomeSets = new HashMap<ID,BiomeReplacer.Variable>();
        addSubBiome(AspenForest,AspenHills);
        addSubBiome(BlossomWoods,BlossomHills);
        addSubBiome(Rainforest,RainforestValley);
        addSubBiome(Shrublands,RoofedShrublands);
        addSubBiome(SilverPineForest,SilverPineHills);
        addSubBiome(Woodlands,WoodlandField);
        addSubBiome(Woodlands,WoodlandHills);
        addSubBiome(Woodlands,WoodlandLake);
        addSubBiome(StoneCanyon,StoneCanyons);
        addSubBiome(ForestedMountains,ForestedValley);
        addSubBiome(Sahara,Sahara);
        addSubBiome(Sahara,Sahara);
        addSubBiome(Sahara,Sahara);
        addSubBiome(Sahara,Oasis);
        addSubBiome(RedDesert,RedDesert);
        addSubBiome(RedDesert,RedDesert);
        addSubBiome(RedDesert,RedDesert);
        addSubBiome(RedDesert,Oasis);
    }

    @Override
    public void setRules(ClimateControlRules rules) {
    }

    static final String biomesOnName = "EBBiomesOn";

    public final Mutable<Boolean> biomesFromConfig = climateControlCategory.booleanSetting(
                        biomesOnName, "", false);

    static final String configName = "EB";
    public final Mutable<Boolean> biomesInNewWorlds = climateControlCategory.booleanSetting(
                        this.startBiomesName(configName),
                        "Use biomes in new worlds and dimensions", true);
    @Override
    public void onNewWorld() {
        biomesFromConfig.set(biomesInNewWorlds);
    }

    @Override
    public boolean biomesAreActive() {
        return this.biomesFromConfig.value();
    }

    private class NativeSettings extends Settings {
        // tired of all this errorprone megacode, so time for an auto system
        public final Category biomeIDCategory = new Category("general");
        ArrayList<LinkedSetting> linkedSettings = new ArrayList<LinkedSetting>();
        
        NativeSettings() {
            // make native config settings for each biome setting
            for (Element element: EBBiomeSettings.this.elements()){
                linkedSettings.add(new LinkedSetting(element));
            }
        }

        void setIDs(Configuration settingFrom) {
            this.readFrom(settingFrom);
            for (LinkedSetting linkage: linkedSettings) {
                linkage.settingInCC.biomeID().set(linkage.settingInEB);
            }
        }

        private class LinkedSetting {
            final Mutable<Integer> settingInEB;
            final Element settingInCC;
            LinkedSetting(Element element) {
                settingInCC = element;
                settingInEB = biomeIDCategory.intSetting("Biome ID of "+element.name, element.biomeID().value());
            }
        }

    }

}
