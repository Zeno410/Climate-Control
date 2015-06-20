
package climateControl.api;

import climateControl.*;
import climateControl.ClimateDistribution.Incidence;
import climateControl.utils.Acceptor;
import climateControl.utils.Mutable;
import climateControl.utils.Settings;
import climateControl.biomeSettings.BoPSettings;
import climateControl.biomeSettings.BopPackage;
import climateControl.biomeSettings.EBXLController;
import climateControl.biomeSettings.HighlandsPackage;
import climateControl.biomeSettings.OceanBiomeSettings;
import climateControl.biomeSettings.ThaumcraftPackage;
import climateControl.biomeSettings.VanillaBiomeSettings;
import climateControl.utils.Named;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import net.minecraftforge.common.config.Configuration;

/**
 *
 * @author Zeno410
 */
public class ClimateControlSettings extends Settings {
    static final String halfSizeName = "Half Zone Size";
    static final String quarterSizeName = "Quarter Zone Size";
    static final String randomBiomesName = "Random Biomes";
    static final String allowDerpyIslandsName = "Allow Derpy Islands";
    static final String moreOceanName = "More Ocean";
    static final String largeContinentFrequencyName = "Large Continent Frequency";
    static final String mediumContinentFrequencyName = "Medium Continent Frequency";
    static final String smallContinentFrequencyName = "Small Continent Frequency";
    static final String largeIslandFrequencyName ="Large Island Frequency";
    static final String mediumIslandFrequencyName ="Medium Island Frequency";
    static final String hotIncidenceName = "Hot Zone Incidence";
    static final String warmIncidenceName = "Warm Zone Incidence";
    static final String coolIncidenceName = "Cool Zone Incidence";
    static final String snowyIncidenceName = "Snowy Zone Incidence";
    static final String vanillaBiomesOnName = "VanillaBiomesOn";
    static final String highlandsBiomesOnName = "HighlandsBiomesOn";
    static final String bopBiomesOnName = "BoPBiomesOn";
    static final String thaumcraftBiomesOnName = "ThaumcraftBiomesOn";
    static final String biomeSizeName = "Biome Size";
    static final String vanillaStructure = "VanillaLandAndClimate";
    static final String mushroomIslandIncidenceName = "Mushroom Island Incidence";
    static final String percentageRiverReductionName = "PercentRiverReduction";
    static final String oneSixCompatibilityName = "1.6Compatibility";
    static final String oneSixExpansionName = "1.6Expansions";
    static final String noBoPSubBiomesName = "NoBoPSubBiomes";
    static final String separateLandmassesName = "SeparateLandmasses";
    static final String interveneInBOPName = "alterBoPWorlds";
    static final String interveneInHighlandsName = "alterHighlandsWorlds";
    static final String controlVillageBiomesName = "controlVillageBiomes";

    private final String subDirectoryName = "climateControl";

    private final Category climateZoneCategory = category("Climate Zone Parameters","" +
                    "Full-size is similar to 1.7 defaults. " +
                    "Half-size creates zones similar to pre-1.7 snowy zones. " +
                    "Quarter-size creates fairly small zones but the hot and snowy incidences are limited");

    public final Mutable<Boolean> halfSize = climateZoneCategory.booleanSetting(halfSizeName, true,
                    "half the climate zone size from vanilla, unless quartering");

    public final Mutable<Boolean> quarterSize = climateZoneCategory.booleanSetting(quarterSizeName, false,
                    "quarter the climate zone size from vanilla");

    public final Mutable<Boolean> randomBiomes = climateZoneCategory.booleanSetting(
            randomBiomesName, false,"ignore climate zones altogether");


    private final Category climateControlCategory = category("Assorted Parameters");

    public final Mutable<Boolean> allowDerpyIslands = climateControlCategory.booleanSetting(
            allowDerpyIslandsName, false, "place little islands near shore rather than in deep ocean");

    public final Mutable<Double> maxRiverChasm = climateControlCategory.doubleSetting(
            "maxChasm", 10.0, "max height value for allowing rivers; 10.0 allows everything; 0.75 is plains but no hills");

    public final Mutable<Boolean> testingMode = climateControlCategory.booleanSetting(
            "TestingMode", false, "add testing routines and crash in suspicious circumstances");

    public final Mutable<Boolean> zeroPointFive = climateControlCategory.booleanSetting(
            "0.5 generation", true, "Use more random generators, better behaved rivers, and larger rare climates");

    public final Mutable<Integer> biomeSize = climateControlCategory.intSetting(
            biomeSizeName, 4, "Biome size, exponential: 4 is regular and 6 is large biomes");
    
    public final Mutable<Boolean> noGenerationChanges = climateControlCategory.booleanSetting(
            "No Generation Changes",false,"generate as if CC weren't on; for loading pre-existing worlds or just preventing chunk boundaries");

    public final Mutable<Boolean> smootherCoasts = climateControlCategory.booleanSetting(
            "Smoother Coastlines",true,"increase smoothing steps; also shrinks unusual biomes some; changing produces occaisional chunk walls");

    public final Mutable<Boolean> vanillaLandAndClimate = climateControlCategory.booleanSetting(
            vanillaStructure, false, "Generate land masses and climate zones similar to vanilla 1.7");

    public final Mutable<Boolean> oneSixCompatibility = climateControlCategory.booleanSetting(
            oneSixCompatibilityName, false, "Use 1.6 compatibility mode");

    public final Mutable<Integer> climateRingsNotSaved = climateControlCategory.intSetting(
            "climateRingsNotSaved", 3, "climate not saved on edges; -1 deactivates saving climates");

    public final Mutable<Integer> biomeRingsNotSaved = climateControlCategory.intSetting(
            "biomeRingsNotSaved", 3, "biomes not saved on edges; more than 3 has no effect; -1 deactivates saving biomes");

    public final Mutable<Integer> subBiomeRingsNotSaved = climateControlCategory.intSetting(
            "subBiomeRingsNotSaved", 0, "subbiomes not saved on edges, default 3, -1 deactivates saving sub-biomes");

    public final Mutable<Integer> mushroomIslandIncidence  = climateControlCategory.intSetting(
            mushroomIslandIncidenceName, 10, "per thousand; vanilla is 10");

    public final Mutable<Integer> percentageRiverReduction  = climateControlCategory.intSetting(
            percentageRiverReductionName, 0, "Percentage of rivers prevented; changes cause chunk boundaries at some rivers");

    public final Mutable<Boolean> controlVillageBiomes = climateControlCategory.booleanSetting(
            controlVillageBiomesName, "Have Climate Control set the biomes for village generation; may be incompatible with village mods", false);

    private final Category climateIncidenceCategory = category("Climate Incidences", "" +
                    "Blocks of land are randomly assigned to each zone with a frequency proportional to the incidence. " +
                    "Processing eliminates some extreme climates later, especially for quarter size zones. " +
                    "Consider doubling hot and snowy incidences for quarter size zones.");

    public final Mutable<Integer> hotIncidence = climateIncidenceCategory.intSetting(hotIncidenceName, 1,
                    "relative incidence of hot zones like savanna/desert/plains/mesa");
    public final Mutable<Integer> warmIncidence = climateIncidenceCategory.intSetting(warmIncidenceName, 1,
                    "relative incidence of warm zones like forest/plains/hills/jungle/swamp");
    public final Mutable<Integer> coolIncidence = climateIncidenceCategory.intSetting(coolIncidenceName, 1,
                    "relative incidence of cool zones like forest/plains/hills/taiga/roofed forest");
    public final Mutable<Integer> snowyIncidence =climateIncidenceCategory.intSetting(snowyIncidenceName, 1,
                    "relative incidence of snowy zones");

    private final Category oceanControlCategory = category("Ocean Control Parameters", "" +
                    "Frequencies are x per thousand but a little goes a very long way because seeds grow a lot. " +
                    "About half the total continent frequencies is the percent land. " +
                    "For worlds with 1.7-like generation set large island seeds to about 300. " +
                    "That will largely fill the oceans after seed growth.");

    public final Mutable<Integer> largeContinentFrequency = oceanControlCategory.intSetting(
            largeContinentFrequencyName, 8,"frequency of large continent seeds, about 8000x16000");
    public final Mutable<Integer> mediumContinentFrequency = oceanControlCategory.intSetting(
            mediumContinentFrequencyName, 12, "frequency of medium continent seeds, about 4000x8000");
    public final Mutable<Integer> smallContinentFrequency = oceanControlCategory.intSetting(
            smallContinentFrequencyName, 18,"frequency of small continent seeds, about 2000x4000");
    public final Mutable<Integer> largeIslandFrequency = oceanControlCategory.intSetting(
            largeIslandFrequencyName, 35,"frequency of large island seeds, about 1000x2000");
    public final Mutable<Integer> mediumIslandFrequency = oceanControlCategory.intSetting(
            mediumIslandFrequencyName, 40,"frequency of medium island seeds, about 500x1000");
    public final Mutable<Boolean> separateLandmasses = oceanControlCategory.booleanSetting(
            separateLandmassesName, true, "True reduces the chance of landmasses merging");

    private OceanBiomeSettings oceanBiomeSettings = new OceanBiomeSettings();

    public final Mutable<Boolean> vanillaBiomesOn = climateControlCategory.booleanSetting(
            vanillaBiomesOnName, "Include vanilla biomes in world", true);
    private VanillaBiomeSettings vanillaBiomeSettings = new VanillaBiomeSettings();

    public final Mutable<Boolean> highlandsBiomesOn = new Mutable.Concrete<Boolean>(false);
    Acceptor<Boolean> highlandsOnTracker = new Acceptor<Boolean>() {
        public void accept(Boolean accepted) {
            highlandsBiomesOn.set(accepted);
        }
    };
    public final Mutable<Boolean> interveneInHighlandsWorlds = climateControlCategory.booleanSetting(
            interveneInHighlandsName, false, "impose Climate Control generation on Highlands world types. WARNING: WILL MAKE CHUNK WALLS ALL OVER");

   public final Mutable<Boolean> noBoPSubBiomes = climateControlCategory.booleanSetting(
            noBoPSubBiomesName, false, "suppress Bop sub-biome generation");

    public final Mutable<Boolean> interveneInBOPWorlds = climateControlCategory.booleanSetting(
            interveneInBOPName, false, "impose Climate Control generation on the Biomes o' Plenty world type");

    public final boolean doBoPSubBiomes() {
        return noBoPSubBiomes.value()==false;
    }
   

    public ClimateControlSettings() {
        try {
            // see if highlands is there
            BiomePackageRegistry.instance.register(new HighlandsPackage());
        } catch (java.lang.NoClassDefFoundError e){
            // Highlands isn't installed
        }
        try {
            // see if BoP is there
            BiomePackageRegistry.instance.register(new BopPackage());
        } catch (java.lang.NoClassDefFoundError e){
            // BoP isn't installed
        }
        try {
            // see if thaumcraft is there
            // attach a setting
            BiomePackageRegistry.instance.register(new ThaumcraftPackage());

        } catch (java.lang.NoClassDefFoundError e){
            // thaumcraft isn't installed
        }
        try {
            // see if EBXL is there
            BiomePackageRegistry.instance.register(new EBXLController());

        } catch (java.lang.NoClassDefFoundError e){
            // EBXL isn't installed
        }
    }
        public boolean doFull() {return !halfSize.value() && !quarterSize.value();}
        public boolean doHalf() {return halfSize.value() && !quarterSize.value();}
        
    public ArrayList<BiomeSettings> biomeSettings() {
        ArrayList<BiomeSettings> result = new ArrayList<BiomeSettings>();
        result.add(oceanBiomeSettings);
        if (this.vanillaBiomesOn.value()) result.add(vanillaBiomeSettings);
        for (Named<BiomeSettings> namedSettings: registeredBiomeSettings()) {
            ClimateControl.logger.info("Addon Settings "+namedSettings.name);
            if (namedSettings.object.biomesAreActive()) {
                result.add(namedSettings.object);
                ClimateControl.logger.info("added");
            }
        }
        return result;
    }

    public ArrayList<BiomeSettings> generalBiomeSettings() {
        ArrayList<BiomeSettings> result = new ArrayList<BiomeSettings>();
        result.add(oceanBiomeSettings);
        if (this.vanillaBiomesOn.value()) result.add(vanillaBiomeSettings);
        return result;
    }

    public Collection<Named<BiomeSettings>> registeredBiomeSettings() {
        return BiomePackageRegistry.instance.biomeSettings();

    }

    public void setDefaults(File configDirectory) {
        for (Named<BiomeSettings> registered: registeredBiomeSettings()) {
            registered.object.setNativeBiomeIDs(configDirectory);
        }
    }

    public File subdirectory(File directory) {
        return new File (directory,this.subDirectoryName);
    }

    @Override
    public void readFrom(Configuration source) {
        super.readFrom(source);
        for (BiomeSettings setting: generalBiomeSettings()) {
            //ClimateControl.logger.info(setting.toString());
            if (setting instanceof BoPSettings) {
                ArrayList<Incidence> incidences  = ((BoPSettings)setting).incidences();
                for (Incidence incidence: incidences) {
                    //ClimateControl.logger.info("Vanilla "+incidence.biome + " " + incidence.incidence);
                }
            }
            setting.readFrom(source);
            ClimateControl.logger.info(setting.toString());
            if (setting instanceof BoPSettings) {
                ArrayList<Incidence> incidences  = ((BoPSettings)setting).incidences();
                for (Incidence incidence: incidences) {
                    //ClimateControl.logger.info("Vanilla "+incidence.biome + " " + incidence.incidence);
                }
            }
        }
    }

    @Override
    public void copyTo(Configuration target) {
        super.copyTo(target);
        for (BiomeSettings setting: generalBiomeSettings()) {
            //setting.copyTo(target);
        }
    }

    @Override
    public void readFrom(DataInput input) throws IOException {
        super.readFrom(input);
        for (BiomeSettings setting: biomeSettings()) {
            setting.readFrom(input);
        }
    }

    @Override
    public void writeTo(DataOutput output) throws IOException {
        super.writeTo(output);
        for (BiomeSettings setting: generalBiomeSettings()) {
            //setting.writeTo(output);
        }
    }

}

