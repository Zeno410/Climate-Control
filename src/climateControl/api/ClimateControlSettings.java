
package climateControl.api;

import climateControl.*;
import climateControl.api.ClimateDistribution.Incidence;
import climateControl.utils.Acceptor;
import climateControl.utils.Mutable;
import climateControl.utils.Settings;
import climateControl.biomeSettings.BoPSettings;
import climateControl.biomeSettings.ExternalBiomePackage;
import climateControl.biomeSettings.ExternalBiomePackage.ExternalBiomeSettings;
import climateControl.biomeSettings.OceanBiomeSettings;
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
    private static final String halfSizeName = "Half Zone Size";
    private static final String quarterSizeName = "Quarter Zone Size";
    private static final String randomBiomesName = "Random Biomes";
    private static final String allowDerpyIslandsName = "Allow Derpy Islands";
    private static final String moreOceanName = "More Ocean";
    private static final String largeContinentFrequencyName = "Incidence of Continents,Large";
    private static final String mediumContinentFrequencyName = "Incidence of Continents,Medium";
    private static final String smallContinentFrequencyName = "Incidence of Continents,Small";
    private static final String largeIslandFrequencyName ="Incidence of Islands,Large";
    private static final String mediumIslandFrequencyName ="Incidence of Islands,Medium";
    private static final String hotIncidenceName = "Hot Zone Incidence";
    private static final String warmIncidenceName = "Warm Zone Incidence";
    private static final String coolIncidenceName = "Cool Zone Incidence";
    private static final String snowyIncidenceName = "Snowy Zone Incidence";
    private static final String vanillaBiomesOnName = "VanillaBiomesOn";
    private static final String highlandsBiomesOnName = "HighlandsBiomesOn";
    private static final String bopBiomesOnName = "BoPBiomesOn";
    private static final String thaumcraftBiomesOnName = "ThaumcraftBiomesOn";
    private static final String biomeSizeName = "Biome Size";
    private static final String vanillaStructure = "VanillaLandAndClimate";
    private static final String mushroomIslandIncidenceName = "Mushroom Island Incidence";
    private static final String percentageRiverReductionName = "PercentRiverReduction";
    private static final String widerRiversName = "WiderRivers";
    private static final String oneSixCompatibilityName = "1.6Compatibility";
    private static final String oneSixExpansionName = "1.6Expansions";
    private static final String noBoPSubBiomesName = "NoBoPSubBiomes";
    private static final String separateLandmassesName = "SeparateLandmasses";
    private static final String interveneInBOPName = "alterBoPWorlds";
    private static final String interveneInHighlandsName = "alterHighlandsWorlds";
    private static final String suppressInStandardWorldsName = "suppressInStandardWorlds";
    private static final String controlVillageBiomesName = "controlVillageBiomes";
    private static final String wideBeachesName = "wideBeaches";
    private static final String forceStartContinentName = "forceStartContinent";
    private static final String cacheSizeName = "cacheSize";
    private static final String externalBiomesListName = "externalBiomeNames";

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
            "subBiomeRingsNotSaved", -1, "subbiomes not saved on edges, default 3, -1 deactivates saving sub-biomes");

    public final Mutable<Integer> mushroomIslandIncidence  = climateControlCategory.intSetting(
            mushroomIslandIncidenceName, 10, "per thousand; vanilla is 10");

    public final Mutable<Integer> percentageRiverReduction  = climateControlCategory.intSetting(
            percentageRiverReductionName, 0, "Percentage of rivers prevented; changes cause chunk boundaries at some rivers");

    public final Mutable<Boolean> widerRivers = climateControlCategory.booleanSetting(
            widerRiversName, "True for triple-width rivers", false);

    public final Mutable<Boolean> wideBeaches = climateControlCategory.booleanSetting(
            wideBeachesName, "True for double-width beaches", false);

    public final Mutable<Boolean> controlVillageBiomes = climateControlCategory.booleanSetting(
            controlVillageBiomesName, "Have Climate Control set the biomes for village generation; may be incompatible with village mods", false);
    
    private final Mutable<Integer> cacheSize = climateControlCategory.intSetting(
            cacheSizeName, 0, "Number of Chunk Biome layouts cached. Above 500 is ignored. 0 or below shuts off chunk info caching");
    
    public boolean cachingOn() {return true;}// {return cacheSize.value()> 0;}
    public int cacheSize() {
        if (cachingOn()) {
            return cacheSize.value() >500 ? 500: cacheSize.value();
        }
        return 0;
    }

    private final Category climateIncidenceCategory = category("Climate Incidences", "" +
                    "Blocks of land are randomly assigned to each zone with a frequency proportional to the incidence. " +
                    "Processing eliminates some extreme climates later, especially for quarter size zones. " +
                    "Consider doubling hot and snowy incidences for quarter size zones.");


    public final Mutable<Integer> hotIncidence = climateIncidenceCategory.intSetting(hotIncidenceName, 2,
                    "relative incidence of hot zones like savanna/desert/plains/mesa");
    public final Mutable<Integer> warmIncidence = climateIncidenceCategory.intSetting(warmIncidenceName, 1,
                    "relative incidence of warm zones like forest/plains/hills/jungle/swamp");
    public final Mutable<Integer> coolIncidence = climateIncidenceCategory.intSetting(coolIncidenceName, 1,
                    "relative incidence of cool zones like forest/plains/hills/taiga/roofed forest");
    public final Mutable<Integer> snowyIncidence =climateIncidenceCategory.intSetting(snowyIncidenceName, 2,
                    "relative incidence of snowy zones");

    private final Category oceanControlCategory = category("Ocean Control Parameters", "" +
                    "Frequencies are x per thousand but a little goes a very long way because seeds grow a lot. " +
                    "SeparateLandmasses = true makes an oceanic world with these settings and"+
                    "SeparateLandmasses = false makes a continental world");

    public final Mutable<Integer> largeContinentFrequency = oceanControlCategory.intSetting(
            largeContinentFrequencyName, 0,"frequency of large continent seeds, about 8000x16000");
    public final Mutable<Integer> mediumContinentFrequency = oceanControlCategory.intSetting(
            mediumContinentFrequencyName, 30, "frequency of medium continent seeds, about 4000x8000");
    public final Mutable<Integer> smallContinentFrequency = oceanControlCategory.intSetting(
            smallContinentFrequencyName, 80,"frequency of small continent seeds, about 2000x4000");
    public final Mutable<Integer> largeIslandFrequency = oceanControlCategory.intSetting(
            largeIslandFrequencyName, 60,"frequency of large island seeds, about 500x1000");
    public final Mutable<Integer> mediumIslandFrequency = oceanControlCategory.intSetting(
            mediumIslandFrequencyName, 40,"frequency of medium island seeds, about 250x500, but they tend to break up into archipelagos");
    public final Mutable<Boolean> separateLandmasses = oceanControlCategory.booleanSetting(
            separateLandmassesName, true, "True mostly stops landmasses merging." +
            "With default settings you will get an oceanic world if true and " +
            "a continental world if false");

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
            interveneInHighlandsName, false, "impose Climate Control generation on Highlands world types");

   public final Mutable<Boolean> noBoPSubBiomes = climateControlCategory.booleanSetting(
            noBoPSubBiomesName, true, "suppress Bop sub-biome generation");

    public final Mutable<Boolean> interveneInBOPWorlds = climateControlCategory.booleanSetting(
            interveneInBOPName, false, "impose Climate Control generation on the Biomes o' Plenty world type");

    public final Mutable<Boolean> suppressInStandardWorlds = climateControlCategory.booleanSetting(
            suppressInStandardWorldsName, false, "suppress Climate Control generation in default, large biomes, and amplified worlds");

    public final Mutable<Boolean> forceStartContinent = climateControlCategory.booleanSetting(
            forceStartContinentName,false,"force small continent near origin");

    public final boolean doBoPSubBiomes() {
        return noBoPSubBiomes.value()==false;
    }

    public final Mutable<String> externalBiomeNames = climateControlCategory.stringSetting(externalBiomesListName,
            "", "Comma-delimited list of externalBiome Names. No quotes or line returns"+
            "You will have to reload Minecraft after changing this."+
            "Names not in the list aren't removed from the configs but they have no effect");
    private ExternalBiomePackage.ExternalBiomeSettings externalBiomeSettings;
   

    public ClimateControlSettings() {
        this.registeredBiomeSettings = BiomePackageRegistry.instance.freshBiomeSettings();
    }

        public boolean doFull() {return !halfSize.value() && !quarterSize.value();}
        public boolean doHalf() {return halfSize.value() && !quarterSize.value();}
        
    public ArrayList<BiomeSettings> biomeSettings() {
        ArrayList<BiomeSettings> result = new ArrayList<BiomeSettings>();
        result.add(oceanBiomeSettings);
        if (this.vanillaBiomesOn.value()) result.add(vanillaBiomeSettings);
        if (this.externalBiomeSettings != null) result.add(externalBiomeSettings);
        for (Named<BiomeSettings> namedSettings: registeredBiomeSettings()) {
            ClimateControl.logger.info("Addon Settings "+namedSettings.name);
            //if (namedSettings.object.biomesAreActive()) {
                result.add(namedSettings.object);
                ClimateControl.logger.info("added");
            //}
        }
        return result;
    }

    public ArrayList<BiomeSettings> generalBiomeSettings() {
        ArrayList<BiomeSettings> result = new ArrayList<BiomeSettings>();
        result.add(oceanBiomeSettings);
        if (this.vanillaBiomesOn.value()) result.add(vanillaBiomeSettings);
        return result;
    }

    private final Collection<Named<BiomeSettings>> registeredBiomeSettings;

    public Collection<Named<BiomeSettings>> registeredBiomeSettings() {
        return registeredBiomeSettings;

    }

    public void onNewWorld() {
        for  (BiomeSettings settings: this.biomeSettings()) {
            settings.onNewWorld();
        }
    }
    
    public void setDefaults(File configDirectory) {
        //this.vanillaBiomeSettings.nameDefaultClimates();
        for (Named<BiomeSettings> registered: registeredBiomeSettings()) {
            registered.object.setNativeBiomeIDs(configDirectory);
            //registered.object.nameDefaultClimates();
        }
    }

    public File subdirectory(File directory) {
        return new File (directory,this.subDirectoryName);
    }

    @Override
    public void readFrom(Configuration source) {
        super.readFrom(source);
        externalBiomeSettings = null;
        if (externalBiomeNames.value().length()>0) {
            externalBiomeSettings = new ExternalBiomePackage.ExternalBiomeSettings(externalBiomeNames.value());
            externalBiomeSettings.readFrom(source);
        }

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
            setting.copyTo(target);
        }
        if (externalBiomeSettings != null) {
            externalBiomeSettings.copyTo(target);
        }
    }

    @Override
    public void readFrom(DataInput input) throws IOException {
        super.readFrom(input);
        for (BiomeSettings setting: generalBiomeSettings()) {
            setting.readFrom(input);
        }
        externalBiomeSettings = null;
        if (externalBiomeNames.value().length()>0) {
            externalBiomeSettings = new ExternalBiomePackage.ExternalBiomeSettings(externalBiomeNames.value());
            externalBiomeSettings.readFrom(input);
        }
    }

    @Override
    public void writeTo(DataOutput output) throws IOException {
        super.writeTo(output);
        for (BiomeSettings setting: generalBiomeSettings()) {
            setting.writeTo(output);
        }
        if (externalBiomeSettings != null) {
            externalBiomeSettings.writeTo(output);
        }
    }

}

