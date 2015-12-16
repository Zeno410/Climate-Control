
package climateControl.biomeSettings;

import climateControl.api.ClimateDistribution;
import climateControl.utils.Acceptor;
import climateControl.api.BiomeSettings;
import climateControl.api.Climate;
import climateControl.api.ClimateControlRules;
import climateControl.generator.SubBiomeChooser;
import climateControl.utils.Mutable;
import java.io.File;
import java.util.HashMap;
import net.minecraft.world.biome.BiomeGenBase;
import highlands.Config;

/**
 *
 * @author Zeno410
 */
public class HighlandsBiomeSettings extends BiomeSettings {
    public static final String biomeCategory = "HighlandsBiome";
    public static final String highlandsCategory = "HighlandsSettings";
    public final Category highlandsSettings = new Category(highlandsCategory);

   public final Element  alps = new Element("Alps", 200,"COOL");
   public final Element  autumnForest = new Element("Autumn Forest", 201,"COOL");
   public final Element  badlands = new Element("Badlands", 202,"WARM");
   public final Element  birchHills = new Element("Birch Hills", 203);
   public final Element  bog = new Element("Bog", 204,true,Climate.COOL.name);
   public final Element  cliffs = new Element("Cliffs", 205,ClimateDistribution.MEDIUM.name());
   public final Element  desertMountains = new Element("Desert Mountains", 206,"HOT");
   public final Element  dunes = new Element("Dunes", 207,Climate.HOT.name);
   public final Element  estuary = new Element("Estuary", 208,"WARM");
   public final Element  flyingMountains = new Element("Flying Mountains", 209,ClimateDistribution.MEDIUM.name());
   public final Element  glacier = new Element("Glacier", 210,Climate.SNOWY.name);
   public final Element  highlandsb = new Element("Highlands", 211,ClimateDistribution.MEDIUM.name());
   public final Element  lowlands = new Element("Lowlands", 212,ClimateDistribution.MEDIUM.name());
   public final Element  meadow = new Element("Meadow", 213,true,ClimateDistribution.MEDIUM.name());
   public final ID  ocean2 = new ID("Improved Oceans",229);
   public final Element  outback = new Element("Outback", 214,Climate.HOT.name);
   public final Element  pinelands = new Element("Pinelands", 215,true,"COOL");
   public final Element  rainforest = new Element("Rainforest", 216,"WARM");
   public final Element  redwoodForest = new Element("Redwood Forest", 217,"COOL");
   public final Element  rockMountains = new Element("Rock Mountains", 218,ClimateDistribution.PLAINS.name());
   public final Element  sahel = new Element("Sahel", 219,true,Climate.HOT.name);
   public final Element  savannah = new Element("Savannah", 220,true,Climate.HOT.name);
   public final Element  steppe = new Element("Steppe", 221,true,Climate.COOL.name);
   public final Element  snowMountains = new Element("Snow Mountains", 222,Climate.SNOWY.name);
   public final Element  tallPineForest = new Element("Tall Pine Forest", 223,Climate.SNOWY.name);
   public final Element  tropics = new Element("Tropics", 224,"HOT");
   public final Element  tropicalIslands = new Element("Tropical Islands", 225,1,true,Climate.DEEP_OCEAN.name);
   public final Element  tundra = new Element("Tundra", 226,Climate.SNOWY.name);
   public final Element  woodlands = new Element("Woodlands", 227,ClimateDistribution.MEDIUM.name());
   public final Element  woodlandMountains = new Element("Woodland Mountains", 228,ClimateDistribution.MEDIUM.name());

   public final ID baldHill = new ID("Bald Hill",238);
   public final ID canyon = new ID("Canyon",242);
   public final Element desertIsland = new Element("DesertIsland",230,1,"DEEP_OCEAN");
   public final Element forestIsland = new Element("Forest Island",231,1,"DEEP_OCEAN");
   public final Element jungleIsland = new Element("Jungle Island",232,1,"DEEP_OCEAN");
   public final ID lake = new ID("Lake",237);
   public final ID mesa = new ID("Mesa ID",239);
   public final ID oasis = new ID("Oasis",241);
   public final Element rockIsland = new Element("Rock Island",235,1,"DEEP_OCEAN");
   public final Element shrublands = new Element("Shrublands",243);
   public final ID snowIsland = new ID("Snow Island",234);
   public final ID valley = new ID("Valley",240);
   public final Element volcanoIsland = new Element("Volcano Island",233,1,"DEEP_OCEAN");
   public final Element windyIsland = new Element("Windy Island",236,1,true,"DEEP_OCEAN");


   public final ID desert = this.externalBiome("Desert", BiomeGenBase.desert.biomeID);
   public final ID plains = this.externalBiome("Plains", BiomeGenBase.plains.biomeID);
   public final ID deepOcean = this.externalBiome("Deep Ocean", BiomeGenBase.deepOcean.biomeID);
   public final ID frozenOcean = this.externalBiome("Frozen Ocean", BiomeGenBase.frozenOcean.biomeID);
   public final ID frozenRiver = this.externalBiome("Frozen River", BiomeGenBase.frozenRiver.biomeID);

    public HighlandsBiomeSettings() {
        super(biomeCategory);
        alps.setDistribution(ClimateDistribution.COOL);
        autumnForest.setDistribution(ClimateDistribution.COOL);
        badlands.setDistribution(ClimateDistribution.WARM);
        desertMountains.setDistribution(ClimateDistribution.HOT);
        estuary.setDistribution(ClimateDistribution.WARM);
        pinelands.setDistribution(ClimateDistribution.COOL);
        rainforest.setDistribution(ClimateDistribution.WARM);
        redwoodForest.setDistribution(ClimateDistribution.COOL);
        //ocean2.setDistribution(ClimateDistribution.DEEP_OCEAN);
        tropicalIslands.setDistribution(ClimateDistribution.DEEP_OCEAN);
        tropics.setDistribution(ClimateDistribution.HOT);
        desertIsland.setDistribution(ClimateDistribution.DEEP_OCEAN);
        forestIsland.setDistribution(ClimateDistribution.DEEP_OCEAN);
        jungleIsland.setDistribution(ClimateDistribution.DEEP_OCEAN);
        rockIsland.setDistribution(ClimateDistribution.DEEP_OCEAN);
        volcanoIsland.setDistribution(ClimateDistribution.DEEP_OCEAN);
        windyIsland.setDistribution(ClimateDistribution.DEEP_OCEAN);
        //this.biomesFromConfig.informOnChange(catchFalse);
    }
    
    public Acceptor<Boolean> catchFalse = catchFalse();

    public Acceptor<Boolean> catchFalse() {
        return new Acceptor<Boolean>() {

            @Override
            public void accept(Boolean arg0) {
                if (arg0 == false) throw new RuntimeException();
            }

        };
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

    @Override
    public void update(SubBiomeChooser subBiomeChooser) {
        setUpAllSubBiomes();
        super.update(subBiomeChooser);
    }

    public void setUpAllSubBiomes(){
        //imposeSubBiomes();
        subBiomeSets = new HashMap<ID,BiomeReplacer.Variable>();
		addSubBiome(alps, tallPineForest);
		addSubBiome(alps, glacier);
		addSubBiome(alps, alps);
		addSubBiome(alps, alps);
		addSubBiome(tropicalIslands, volcanoIsland);
		for(int i = 0; i < 3; i++){
			addSubBiome(tropicalIslands, tropicalIslands);
		}
		addSubBiome(autumnForest, baldHill);
		addSubBiome(autumnForest, lake);
		addSubBiome(autumnForest, autumnForest);
		addSubBiome(autumnForest, autumnForest);
		addSubBiome(birchHills, meadow);
		addSubBiome(birchHills, birchHills);
		addSubBiome(cliffs, valley);
		addSubBiome(dunes, oasis);
		addSubBiome(dunes, dunes);
		addSubBiome(estuary, lake);
		addSubBiome(meadow, lake);
		addSubBiome(meadow, birchHills);
		addSubBiome(meadow, meadow);
		addSubBiome(meadow, meadow);
		addSubBiome(woodlands, baldHill);
		addSubBiome(woodlands, lake);
		addSubBiome(woodlands, plains);
		addSubBiome(woodlands, woodlands);
		addSubBiome(woodlands, woodlands);
		addSubBiome(highlandsb, woodlands);
		addSubBiome(highlandsb, highlandsb);
		addSubBiome(lowlands, baldHill);
		addSubBiome(lowlands, lake);
		addSubBiome(lowlands, lowlands);
		addSubBiome(lowlands, lowlands);
		addSubBiome(outback, mesa);
		addSubBiome(outback, desert);
		addSubBiome(outback, outback);
		addSubBiome(outback, outback);
		addSubBiome(pinelands, autumnForest);
		addSubBiome(pinelands, pinelands);
		addSubBiome(redwoodForest, highlandsb);
		addSubBiome(redwoodForest, lake);
		addSubBiome(redwoodForest, redwoodForest);
		addSubBiome(redwoodForest, redwoodForest);
		addSubBiome(sahel, mesa);
		addSubBiome(sahel, desert);
		addSubBiome(sahel, savannah);
		addSubBiome(sahel, sahel);
		addSubBiome(sahel, sahel);
		addSubBiome(sahel, sahel);
		addSubBiome(savannah, mesa);
		addSubBiome(savannah, savannah);
		addSubBiome(steppe, canyon);
		addSubBiome(steppe, steppe);
		addSubBiome(tallPineForest, alps);
		addSubBiome(tallPineForest, tallPineForest);
		addSubBiome(rainforest, baldHill);
		addSubBiome(rainforest, lake);
		addSubBiome(rainforest, rainforest);
		addSubBiome(rainforest, rainforest);
		addSubBiome(tropics, lake);
		addSubBiome(tropics, tropics);
		addSubBiome(tundra, alps);
		addSubBiome(tundra, tallPineForest);
		addSubBiome(tundra, tundra);
		addSubBiome(tundra, tundra);

	}


    public File configFile(File source) {
        File directory = source.getParentFile();
            File highlands = new File(directory,"Highlands");
            File generalConfig = new File(highlands,"General.cfg");
            return generalConfig;
    }

    @Override
    public void setNativeBiomeIDs(File configDirectory) {
        try {
            this.alps.biomeID().set(Config.alpsID);
            this.autumnForest.biomeID().set(Config.autumnForestID);
            this.badlands.biomeID().set(Config.badlandsID);
            this.baldHill.biomeID().set(Config.baldHillID);
            this.birchHills.biomeID().set(Config.birchHillsID);
            this.bog.biomeID().set(Config.bogID);
            this.canyon.biomeID().set(Config.canyonID);
            this.cliffs.biomeID().set(Config.cliffsID);
            this.desertIsland.biomeID().set(Config.desertIslandID);
            this.desertMountains.biomeID().set(Config.desertMountainsID);
            this.dunes.biomeID().set(Config.dunesID);
            this.estuary.biomeID().set(Config.estuaryID);
            this.flyingMountains.biomeID().set(Config.flyingMountainsID);
            this.forestIsland.biomeID().set(Config.forestIslandID);
            this.glacier.biomeID().set(Config.glacierID);
            this.highlandsb.biomeID().set(Config.highlandsbID);
            this.lowlands.biomeID().set(Config.lowlandsID);
            this.jungleIsland.biomeID().set(Config.jungleIslandID);
            this.meadow.biomeID().set(Config.meadowID);
            this.mesa.biomeID().set(Config.mesaID);
            this.ocean2.biomeID().set(Config.ocean2ID);
            this.outback.biomeID().set(Config.outbackID);
            this.pinelands.biomeID().set(Config.pinelandsID);
            this.rainforest.biomeID().set(Config.rainforestID);
            this.redwoodForest.biomeID().set(Config.redwoodForestID);
            this.rockIsland.biomeID().set(Config.rockIslandID);
            this.rockMountains.biomeID().set(Config.rockMountainsID);
            this.sahel.biomeID().set(Config.sahelID);
            this.savannah.biomeID().set(Config.savannahID);
            this.shrublands.biomeID().set(Config.shrublandID);
            this.snowIsland.biomeID().set(Config.snowIslandID);
            this.snowMountains.biomeID().set(Config.snowMountainsID);
            this.tallPineForest.biomeID().set(Config.tallPineForestID);
            this.tropicalIslands.biomeID().set(Config.tropicalIslandsID);
            this.tropics.biomeID().set(Config.tropicsID);
            this.tundra.biomeID().set(Config.tundraID);
            this.valley.biomeID().set(Config.valleyID);
            this.volcanoIsland.biomeID().set(Config.volcanoIslandID);
            this.windyIsland.biomeID().set(Config.windyIslandID);
            this.woodlandMountains.biomeID().set(Config.woodlandMountainsID);
            this.woodlands.biomeID().set(Config.woodlandsID);
        } catch (java.lang.NoClassDefFoundError e) {
            // no highlands
        }
    }

    @Override
    public void setRules(ClimateControlRules rules) {
        rules.disallowRivers(this.desertMountains.biomeID().value());
        rules.disallowRivers(this.canyon.biomeID().value());
        rules.disallowRivers(this.rockMountains.biomeID().value());
        rules.disallowRivers(this.tropics.biomeID().value());
        rules.disallowRivers(this.valley.biomeID().value());
        rules.disallowStoneBeach(this.sahel.biomeID().value());
        rules.disallowStoneBeach(this.desertMountains.biomeID().value());
        rules.disallowStoneBeach(this.dunes.biomeID().value());
        rules.disallowStoneBeach(this.desertIsland.biomeID().value());
        setVillages(rules);
    }

    static final String biomesOnName = "HighlandsBiomesOn";

    public final Mutable<Boolean> biomesFromConfig = climateControlCategory.booleanSetting(
                        biomesOnName, "", false);
    @Override
    public boolean biomesAreActive() {
        return this.biomesFromConfig.value();
    }

}
