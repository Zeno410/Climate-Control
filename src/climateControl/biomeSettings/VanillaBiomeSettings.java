
package climateControl.biomeSettings;

import climateControl.api.BiomeSettings;
import climateControl.api.ClimateControlRules;
import climateControl.ClimateDistribution;
import climateControl.utils.Mutable;
import java.io.File;

/**
 *
 * @author Zeno410
 */
public class VanillaBiomeSettings extends BiomeSettings {    // biome incidences. Numbers are defaults.
    public static int birchForestDefault =10;
    public static int coldTaigaDefault=10;
    public static int desertDefault=30;
    public static int extremeHillsDefault=20;
    public static int forestDefault=20;
    public static int icePlainsDefault=30;
    public static int jungleDefault=5;
    public static int megaTaigaDefault=5;
    public static int mesaPlateauDefault=1;
    public static int mesaPlateau_FDefault=4;
    public static int plainsDefault=30;
    public static int roofedForestDefault=10;
    public static int savannaDefault=10;
    public static int swamplandDefault=10;
    public static int taigaDefault=20;

    public static final String birchForestName ="Birch Forest";
    public static final String coldTaigaName="Cold Taiga";
    public static final String desertName="Desert";
    public static final String extremeHillsName="Extreme Hills";
    public static final String forestName="Forest";
    public static final String icePlainsName="Ice Plains";
    public static final String jungleName="Jungle";
    public static final String megaTaigaName="Mega Taiga";
    public static final String mesaPlateauName="Mesa Plateau";
    public static final String mesaPlateau_FName="Mesa Plateau F";
    public static final String plainsName="Plains";
    public static final String roofedForestName="Roofed Forest";
    public static final String savannaName="Savanna";
    public static final String swamplandName="Swampland";
    public static final String taigaName="Taiga (snowless)";

    public static final String biomeCategory = "VanillaBiome";

    Element birchForest = new Element(birchForestName, 27, 10);
    Element coldTaiga = new Element(coldTaigaName,30 , 10);
    Element desert = new Element(desertName, 2, 30, true);
    Element extremeHills = new Element(extremeHillsName,3 , 20);
    Element forest = new Element(forestName,4 , 20);
    Element icePlains = new Element(icePlainsName,12 , 30);
    Element jungle = new Element(jungleName,21, 5);
    Element megaTaiga = new Element(megaTaigaName,32, 5);
    Element mesaPlateau = new Element(mesaPlateauName,39, 1);
    Element mesaPlateau_F = new Element(mesaPlateau_FName,38, 4);
    Element plains = new Element(plainsName, 1,30,true);
    Element roofedForest = new Element(roofedForestName,29, 10);
    Element savanna = new Element(savannaName,35, 20,true);
    Element swampland = new Element(swamplandName,6, 10);
    Element taiga = new Element(taigaName,5, 10);
    ID iceMountains = new ID("Ice Mountains",13);
    ID mushroomIsland = new ID("Mushroom Island",14);
    ID desertHills = new ID("Desert Hills",17);
    ID forestHills = new ID("Forest Hills",18);
    ID taigaHills = new ID("Taiga Hills",19);
    ID jungleHills = new ID("Jungle Hills",21);
    ID birchForestHills = new ID("Birch Forest Hills",28);
    ID coldTaigaHills = new ID("Cold Taiga Hills",31);
    ID megaTaigaHills = new ID("Mega Taiga Hills",33);
    ID extremeHillsPlus = new ID("Extreme Hills+",34);
    ID savannaPlateau = new ID("Savanna Plateau",36);
    ID mesa = new ID("Mesa",37);
    ID sunflowerPlains = new ID("Sunflower Plains",129);
    ID desertM = M(desert);
    ID extremeHillsM = M(extremeHills);
    ID flowerForest = M(forest,"Flower Forest");
    ID taigaM = M(taiga);
    ID swamplandM = M(swampland);
    ID icePlainsSpikes = M(icePlains, "Ice Plains Spikes");
    ID jungleM = M(jungle);
    ID birchForestM = M(birchForest);
    ID birchForestHillsM = M(birchForestHills);
    ID roofedForestM = M(roofedForest);
    ID coldTaigaM = M(coldTaiga);
    ID megaSpruceTaiga = M(megaTaiga,"Mega Spruce Taiga");
    ID extremeHillsPlusM = M(extremeHillsPlus);
    ID savannaM = M(savanna);
    ID savannaPlateauM = M(savannaPlateau);
    ID bryceMesa = M(mesa,"Mesa (Bryce)");
    ID mesaPlateauM = M(mesaPlateau);
    ID mesaPlateau_FM = M(mesaPlateau_F);


    public VanillaBiomeSettings() {
        super(biomeCategory);
        
        // set non-default distributions
        jungle.setDistribution(ClimateDistribution.WARM);
        megaTaiga.setDistribution(ClimateDistribution.COOL);
        plains.setDistribution(ClimateDistribution.PLAINS);
        swampland.setDistribution(ClimateDistribution.WARM);
        roofedForest.setDistribution(ClimateDistribution.COOL);
        taiga.setDistribution(ClimateDistribution.COOL);

        // set subBiome distributions
        desert.setSubBiome(desertHills);
        forest.setSubBiome(forestHills);
        birchForest.setSubBiome(birchForestHills);
        roofedForest.setSubBiome(plains);
        taiga.setSubBiome(taigaHills);
        megaTaiga.setSubBiome(megaTaigaHills);
        coldTaiga.setSubBiome(coldTaigaHills);
        BiomeReplacer.Variable plainsSubBiomes = new BiomeReplacer.Variable();
        plainsSubBiomes.add(forestHills, 1);
        plainsSubBiomes.add(forest, 2);
        plains.setSubBiomeChooser(plainsSubBiomes);
        icePlains.setSubBiome(iceMountains);
        jungle.setSubBiome(jungleHills);
        extremeHills.setSubBiome(extremeHillsPlus);
        savanna.setSubBiome(savannaPlateau);
        mesaPlateau_F.setSubBiome(mesa);

    }

    @Override
    public void setNativeBiomeIDs(File configDirectory) {
        // never anything to do in vanilla
    }

    @Override
    public void setRules(ClimateControlRules rules) {
        setVillages(rules);
        // nothing yet
    }
    static final String biomesOnName = "VanillaBiomesOn";

    public final Mutable<Boolean> biomesFromConfig = climateControlCategory.booleanSetting(
                        biomesOnName, "", false);
    @Override
    public boolean biomesAreActive() {
        return this.biomesFromConfig.value();
    }
}
