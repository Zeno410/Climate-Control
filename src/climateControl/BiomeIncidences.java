package climateControl;

import net.minecraftforge.common.config.Configuration;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldSavedData;

/**
 *
 * @author Zeno410
 */
public class BiomeIncidences extends WorldSavedData implements Cloneable {

    // biome incidences. Numbers are defaults.
    public int birchForest =10;
    public int coldTaiga=10;
    public int desert=30;
    public int extremeHills=20;
    public int forest=20;
    public int icePlains=30;
    public int jungle=5;
    public int megaTaiga=5;
    public int mesaPlateau=1;
    public int mesaPlateau_F=4;
    public int plains=30;
    public int roofedForest=10;
    public int savanna=10;
    public int swampland=10;
    public int taiga=20;

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

    public static final String biomeFrequencyCategory = "BiomeFrequencies";

    public BiomeIncidences() {
        super("RandomBiomeIncidences");
    }

    public BiomeIncidences(Configuration config) {
        super("RandomBiomeIncidences");
        birchForest = config.get(biomeFrequencyCategory, birchForestName, 10).getInt();
        coldTaiga = config.get(biomeFrequencyCategory, coldTaigaName, 10).getInt();
        desert = config.get(biomeFrequencyCategory, desertName, 30).getInt();
        extremeHills = config.get(biomeFrequencyCategory, extremeHillsName, 20,"rounded down to an even number if climate zones are on").getInt();
        forest = config.get(biomeFrequencyCategory, forestName, 20,"rounded down to an even number if climate zones are on").getInt();
        icePlains = config.get(biomeFrequencyCategory, icePlainsName, 30).getInt();
        jungle = config.get(biomeFrequencyCategory, jungleName, 5).getInt();
        megaTaiga = config.get(biomeFrequencyCategory, megaTaigaName, 5).getInt();
        mesaPlateau = config.get(biomeFrequencyCategory, mesaPlateauName, 1).getInt();
        mesaPlateau_F = config.get(biomeFrequencyCategory, mesaPlateau_FName, 4).getInt();
        plains = config.get(biomeFrequencyCategory, plainsName, 30,"rounded down to an number divisible by 3 if climate zones are on").getInt();
        roofedForest = config.get(biomeFrequencyCategory, roofedForestName, 10).getInt();
        savanna = config.get(biomeFrequencyCategory, savannaName, 20).getInt();
        swampland = config.get(biomeFrequencyCategory, swamplandName, 10).getInt();
        taiga = config.get(biomeFrequencyCategory, taigaName, 10).getInt();
    }

    public int totalIncidences() {
        return birchForest +coldTaiga+desert+extremeHills+forest+icePlains+jungle+megaTaiga+
                mesaPlateau+mesaPlateau_F+plains+roofedForest+savanna+swampland+taiga;
    }
    public void readFromNBT(NBTTagCompound tag) {
        birchForest = tag.getInteger(birchForestName);
        coldTaiga=tag.getInteger(coldTaigaName);
        desert=tag.getInteger(desertName);
        extremeHills=tag.getInteger(extremeHillsName);
        forest=tag.getInteger(forestName);
        icePlains=tag.getInteger(icePlainsName);
        jungle=tag.getInteger(jungleName);
        megaTaiga=tag.getInteger(megaTaigaName);
        mesaPlateau=tag.getInteger(mesaPlateauName);
        mesaPlateau_F=tag.getInteger(mesaPlateau_FName);
        plains=tag.getInteger(plainsName);
        roofedForest=tag.getInteger(roofedForestName);
        savanna=tag.getInteger(savannaName);
        swampland=tag.getInteger(swamplandName);
        taiga=tag.getInteger(taigaName);
    }

    public void writeToNBT(NBTTagCompound tag) {
        tag.setInteger(birchForestName, birchForest);
        tag.setInteger(coldTaigaName, coldTaiga);
        tag.setInteger(desertName, desert);
        tag.setInteger(extremeHillsName, extremeHills);
        tag.setInteger(forestName, forest);
        tag.setInteger(icePlainsName, icePlains);
        tag.setInteger(jungleName, jungle);
        tag.setInteger(megaTaigaName, megaTaiga);
        tag.setInteger(mesaPlateauName, mesaPlateau);
        tag.setInteger(mesaPlateau_FName, mesaPlateau_F);
        tag.setInteger(plainsName, plains);
        tag.setInteger(roofedForestName, roofedForest);
        tag.setInteger(savannaName, savanna);
        tag.setInteger(swamplandName, swampland);
        tag.setInteger(taigaName, taiga);
    }

    public BiomeIncidences clone() {
        try {
            return (BiomeIncidences)(super.clone());
        } catch (CloneNotSupportedException ex) {
            return this;
        }
    }

/* this.randomBiomeList = new BiomeGenBase[] {BiomeGenBase.desert, BiomeGenBase.desert, BiomeGenBase.savanna,
BiomeGenBase.plains, BiomeGenBase.plains, BiomeGenBase.forest, BiomeGenBase.forest,
BiomeGenBase.roofedForest, BiomeGenBase.extremeHills, BiomeGenBase.extremeHills,
BiomeGenBase.birchForest, BiomeGenBase.swampland, BiomeGenBase.swampland, BiomeGenBase.taiga,
BiomeGenBase.icePlains, BiomeGenBase.coldTaiga, BiomeGenBase.mesaPlateau, BiomeGenBase.mesaPlateau_F,
BiomeGenBase.megaTaiga, BiomeGenBase.jungle, BiomeGenBase.jungle};*/

}
