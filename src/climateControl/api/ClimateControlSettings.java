
package climateControl.api;

//import climateControl.*;
import com.Zeno410Utils.Acceptor;
import com.Zeno410Utils.Mutable;
import com.Zeno410Utils.Settings;
import climateControl.biomeSettings.BoPSettings;
import climateControl.biomeSettings.ExternalBiomePackage;
import climateControl.biomeSettings.OceanBiomeSettings;
import climateControl.biomeSettings.VanillaBiomeSettings;
import climateControl.generator.MountainFormer;
import com.Zeno410Utils.Named;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import net.minecraft.world.WorldType;
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
    private static final String interveneInCustomizedName = "alterCustomWorlds";
    private static final String controlVillageBiomesName = "controlVillageBiomes";
    private static final String wideBeachesName = "wideBeaches";
    private static final String forceLargeStartContinentName = "forceLargeStartContinent";
    private static final String forceMediumStartContinentName = "forceMediumStartContinent";
    private static final String forceStartContinentName = "forceStartContinent";
    private static final String cacheSizeName = "cacheSize";
    private static final String externalBiomesListName = "externalBiomeNames";
    private static final String rescueLimitName = "rescueLimit";
    private static final String bandedClimateWidthName = "bandedClimateWidth";
    private static final String bandedClimateOffsetName = "bandedClimateOffset";
    private static final String xSpawnOffsetName = "xSpawnOffset";
    private static final String zSpawnOffsetName = "zSpawnOffset";
    private static final String mountainsChains = "Mountains in Mountain Chains";
    private static final String frozenIcecapName = "Frozen Icecaps";
    private static final String landExpansionRoundsName = "Land Expansion Rounds";
    private static final String forceIceMountainsName = "Ice Mountains in Mountain Chains";
    private static final String forceMoutainMesasName = "Mesas in Mountain Chains";
    private static final String mesaMesaBordersName = "Mesas for mesa borders";
    private static final String extremeClimateSeparationName = "ExtremeClimateSeparation";
    private static final String complexSubBiomesName = "MoreComplexSubBiomes";

    private WorldType worldType = WorldType.DEFAULT;
    public void setWorldType(WorldType newType) {worldType = newType;}
    
    public int maximumBiomes() {return BiomeSettings.highestBiomeID();}//hook to permit greater numbers in the future; sparsely implemented at present.
    
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

    public final Mutable<Boolean> mountainChains = climateZoneCategory.booleanSetting(mountainsChains, false,
                    "Place mountains in chains");

    public final Mutable<Boolean> forceIceMountains = climateZoneCategory.booleanSetting(forceIceMountainsName, true,
                    "Use Ice Mountains instead of Ice Plains in mountain chain areas");

    public final Mutable<Boolean> MesaMountains = climateZoneCategory.booleanSetting(forceMoutainMesasName, true,
                    "Use Mesas as mountains in mountain chain areas");

    public final Mutable<Integer> bandedClimateWidth = climateZoneCategory.intSetting(bandedClimateWidthName,
            -1, "Width of banded climates (climate depends on latitude). 0 or less for normal rules. Width is in terms of climate zones, whatever they are"+
            "  Widths below 3 will have mixed-up zones. ");

    public final Mutable<Integer> bandedClimateOffset = climateZoneCategory.intSetting(bandedClimateOffsetName,
            0, "Number of climate zones to shift the band from the default of the warm - to - cool transition at 0. Positive numbers shift the bands up.");

    public final Mutable<Integer> xSpawnOffset = climateZoneCategory.intSetting(
            xSpawnOffsetName, 0, "X offset for initial spawn search in blocks");

    public final Mutable<Integer> zSpawnOffset = climateZoneCategory.intSetting(
            zSpawnOffsetName, 0, "Z offset for initial spawn search in blocks");

    public final Mutable<Boolean> frozenIcecaps = climateZoneCategory.booleanSetting(
            frozenIcecapName, false, "True freezes oceans in snowy latitudes. Only used with latitudinal climates.");

    private final Category climateControlCategory = category("Assorted Parameters");

    public final Mutable<Boolean> allowDerpyIslands = climateControlCategory.booleanSetting(
            allowDerpyIslandsName, false, "place little islands near shore rather than in deep ocean");

    public final Mutable<Double> maxRiverChasm = climateControlCategory.doubleSetting(
            "maxChasm", 10.0, "max height value for allowing rivers; 10.0 allows everything; 0.75 is plains but no hills");

    public final Mutable<Boolean> testingMode = climateControlCategory.booleanSetting(
            "TestingMode", false, "add testing routines and crash in suspicious circumstances"); 

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
            "subBiomeRingsNotSaved", 3, "subbiomes not saved on edges, default 3, -1 deactivates saving sub-biomes");

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

    public final Mutable<Integer> rescueSearchLimit  = climateControlCategory.intSetting(
            rescueLimitName,-1,"Maximum Number of Rescue attempts. Negative numbers mean no limit");

    public final Mutable<Boolean> mesaMesaBorders  = climateControlCategory.booleanSetting(
            mesaMesaBordersName,true,"Use red sand mesa for mesa borders. False uses desert like vanilla");

    public final Mutable<Boolean> extremeClimateSeparation  = climateZoneCategory.booleanSetting(
            extremeClimateSeparationName,false,"Separate climates further. Makes illegitimate junctions rarer but also makes extreme climates rarer.");
    
    public boolean cachingOn() {return true;}// {return cacheSize.value()> 0;}
    public int cacheSize() {
        if (cachingOn()) {
            return cacheSize.value() >500 ? 500: cacheSize.value();
        }
        return 0;
    }

    private final Category climateIncidenceCategory = category("Climate Incidences", "" +
                    "Blocks of land are randomly assigned to each zone with a frequency proportional to the incidence. " +
                    "Smoothing eliminates some extreme climates on continents later, especially for quarter size zones. " +
                    "Default 3/2/2/3 is better for continents since Hot and Snowy are easier to find." +
                    "With lots of islands 1/1/1/1 may be better as not so many islands have extreme climates.");


    public final Mutable<Integer> hotIncidence = climateIncidenceCategory.intSetting(hotIncidenceName, 3,
                    "relative incidence of hot zones like savanna/desert/plains/mesa");
    public final Mutable<Integer> warmIncidence = climateIncidenceCategory.intSetting(warmIncidenceName, 2,
                    "relative incidence of warm zones like forest/plains/hills/jungle/swamp");
    public final Mutable<Integer> coolIncidence = climateIncidenceCategory.intSetting(coolIncidenceName, 2,
                    "relative incidence of cool zones like forest/plains/hills/taiga/roofed forest");
    public final Mutable<Integer> snowyIncidence =climateIncidenceCategory.intSetting(snowyIncidenceName, 3,
                    "relative incidence of snowy zones");

    private final Category oceanControlCategory = category("Ocean Control Parameters", "" +
                    "Frequencies are x per thousand but a little goes a very long way because seeds grow a lot. " +
                    "SeparateLandmasses = true makes an oceanic world with these settings and"+
                    "SeparateLandmasses = false makes a continental world");

    public final Mutable<Integer> largeContinentFrequency = oceanControlCategory.intSetting(
            largeContinentFrequencyName, 40,"frequency of large continent seeds, about 8000x16000");
    public final Mutable<Integer> mediumContinentFrequency = oceanControlCategory.intSetting(
            mediumContinentFrequencyName, 100, "frequency of medium continent seeds, about 4000x8000");
    public final Mutable<Integer> smallContinentFrequency = oceanControlCategory.intSetting(
            smallContinentFrequencyName, 60,"frequency of small continent seeds, about 2000x4000");
    public final Mutable<Integer> largeIslandFrequency = oceanControlCategory.intSetting(
            largeIslandFrequencyName, 30,"frequency of large island seeds, about 500x1000");
    public final Mutable<Integer> mediumIslandFrequency = oceanControlCategory.intSetting(
            mediumIslandFrequencyName, 15,"frequency of medium island seeds, about 250x500, but they tend to break up into archipelagos");
    public final Mutable<Boolean> separateLandmasses = oceanControlCategory.booleanSetting(
            separateLandmassesName, true, "True mostly stops landmasses merging." +
            "With default settings you will get an oceanic world if true and " +
            "a continental world if false");
    public final Mutable<Integer> landExpansionRounds = oceanControlCategory.intSetting(landExpansionRoundsName, 1,
            "Rounds of continent and large island expansion in oceanic worlds (with separateLandmasses off). "+
            "More makes continents larger and oceans narrower. Default is 1." +
            "Values above 2 will overwhelm the separate landmasses setting with an otherwise default config.");

    private OceanBiomeSettings oceanBiomeSettings = new OceanBiomeSettings();

    public final Mutable<Boolean> vanillaBiomesOn = climateControlCategory.booleanSetting(
            vanillaBiomesOnName, "Include vanilla biomes in world", true);
    private VanillaBiomeSettings vanillaBiomeSettings = new VanillaBiomeSettings();
    
    public final Mutable<Boolean> complexSubbiomes = climateControlCategory.booleanSetting(complexSubBiomesName, true,
            "More varied vanilla sub-biomes");
    
    public final Mutable<Boolean> highlandsBiomesOn = new Mutable.Concrete<Boolean>(false);
    Acceptor<Boolean> highlandsOnTracker = new Acceptor<Boolean>() {
        public void accept(Boolean accepted) {
            highlandsBiomesOn.set(accepted);
        }
    };
    public final Mutable<Boolean> interveneInHighlandsWorlds = climateControlCategory.booleanSetting(
            interveneInHighlandsName, true, "impose GeographiCraft generation on Highlands world types");

    public final Mutable<Boolean> interveneInBOPWorlds = climateControlCategory.booleanSetting(
            interveneInBOPName, true, "impose GeographiCraft generation on the Biomes o' Plenty world type");

    public final Mutable<Boolean> interveneInCustomizedWorlds = climateControlCategory.booleanSetting(
        interveneInCustomizedName, true, "impose GeographiCraft generation on the Customized world type");

    public final Mutable<Boolean> suppressInStandardWorlds = climateControlCategory.booleanSetting(
        suppressInStandardWorldsName, false, "suppress GeographiCraft generation in default, large biomes, and amplified worlds");    
    
    public final Mutable<Boolean> forceLargeStartContinent = climateControlCategory.booleanSetting(
                forceLargeStartContinentName,false,"force large continent near origin");
    
    public final Mutable<Boolean> forceMediumStartContinent = climateControlCategory.booleanSetting(
            forceMediumStartContinentName,false,"force medium continent near origin");
    
    public final Mutable<Boolean> forceStartContinent = climateControlCategory.booleanSetting(
            forceStartContinentName,true,"force small continent near origin");
    
    
    public final Mutable<Boolean> noBoPSubBiomes = climateControlCategory.booleanSetting(
            noBoPSubBiomesName, false, "suppress Bop sub-biome generation in vanilla biomes");

    public final boolean doBoPSubBiomes() {
        return noBoPSubBiomes.value()==false;
    }

    public final Mutable<String> externalBiomeNames = climateControlCategory.stringSetting(externalBiomesListName,
            "", "Comma-delimited list of externalBiome Names. No quotes or line returns. "+
            "You will have to reload Minecraft after changing this."+
            "Names not in the list aren't removed from the configs but they have no effect");
    private ExternalBiomePackage.ExternalBiomeSettings externalBiomeSettings;
   

    public ClimateControlSettings() {
        this.registeredBiomeSettings = BiomePackageRegistry.instance.freshBiomeSettings();
    }
    
    public ClimateControlSettings(WorldType worldtype) {
        this();
        this.setWorldType(worldtype);
    }
    
        public boolean doFull() {return !halfSize.value() && !quarterSize.value();}
        public boolean doHalf() {return halfSize.value() && !quarterSize.value();}

        private boolean vanillaMountainsForced;

    public ArrayList<BiomeSettings> biomeSettings() {
        // force in ice mountains as a biome for mountains chains
        // this is hacky but the best quick solution
        if (!vanillaMountainsForced) {
            vanillaMountainsForced = true;
            if (this.mountainChains.value()) {
                if (this.forceIceMountains.value()) {
                    vanillaBiomeSettings.forceIceMountains();
                } 
            } 
        }
        ArrayList<BiomeSettings> result = new ArrayList<BiomeSettings>();
        result.add(oceanBiomeSettings);
        if (this.vanillaBiomesOn.value()) result.add(vanillaBiomeSettings);
        if (this.externalBiomeSettings != null) result.add(externalBiomeSettings);
        for (Named<BiomeSettings> namedSettings: registeredBiomeSettings()) {
            //if (namedSettings.object.biomesAreActive()) {
                result.add(namedSettings.object);
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
        Collection<Named<BiomeSettings>> result = new ArrayList<Named<BiomeSettings>>();
        for (Named<BiomeSettings> namedSettings: registeredBiomeSettings) {
            if (namedSettings.object.activeIn(worldType)) result.add(namedSettings);
        }
        return result;
    }

    private ArrayList<DistributionPartitioner> partitioners = new ArrayList<DistributionPartitioner>();

    public ArrayList<DistributionPartitioner> partitioners() {
        ArrayList<DistributionPartitioner> result = new ArrayList<DistributionPartitioner>();
        // add the internal list
        for (DistributionPartitioner partitioner: partitioners) {
            result.add(partitioner);
        }
        // add the registry list
        for (DistributionPartitioner partitioner: DistributionPartitioner.registeredPartitioners()) {
            result.add(partitioner);
        }
        return result;
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
        this.doBiomeSettingsInteractions();
    }

    private boolean interactionsDone = false;
    public void doBiomeSettingsInteractions() {
        if (interactionsDone) return;
        for (BiomeSettings setting: biomeSettings()) {
            // skip BoP settings if off.
            if (setting instanceof BoPSettings) {
                if (!doBoPSubBiomes()) continue;
            }
            setting.arrangeInteractions(biomeSettings());
        }
        interactionsDone = true;
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
            setting.readFrom(source);
        }
        partitioners= new ArrayList<DistributionPartitioner>();
        if (mountainChains.value()) {
            this.partitioners.add(new MountainFormer(this.MesaMountains.value()));
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

