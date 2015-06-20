package climateControl ;

import climateControl.utils.IntRandomizer;
import climateControl.api.BiomeSettings;
import climateControl.customGenLayer.GenLayerRandomBiomes;
import java.util.Collection;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.layer.GenLayer;

/**
 *
 * @author Zeno410
 */
public class BiomeRandomizer {

    public static BiomeRandomizer instance = new BiomeRandomizer();

    private BiomeIncidences incidences;
    private BiomeGenBase[] biomes = new BiomeGenBase[0] ;
    private int nextIndex = 0;

    private BiomeRandomizer() {
    }

    public BiomeIncidences currentIncidences() {return incidences;}

    private BiomeRandomizer hot;
    private BiomeRandomizer warm;
    private BiomeRandomizer cool;
    private BiomeRandomizer snowy;
    private BiomeRandomizer ocean;
    private BiomeRandomizer deepOcean;

    public void set(BiomeIncidences incidences) {
        this.incidences = (BiomeIncidences)(incidences.clone());
        nextIndex = 0;
        biomes = new BiomeGenBase[incidences.totalIncidences()];
        add(incidences.birchForest,BiomeGenBase.birchForest);
        add(incidences.coldTaiga,BiomeGenBase.coldTaiga);
        add(incidences.desert,BiomeGenBase.desert);
        add(incidences.extremeHills,BiomeGenBase.extremeHills);
        add(incidences.forest,BiomeGenBase.forest);
        add(incidences.icePlains,BiomeGenBase.icePlains);
        add(incidences.jungle,BiomeGenBase.jungle);
        add(incidences.megaTaiga,BiomeGenBase.megaTaiga);
        add(incidences.mesaPlateau,BiomeGenBase.mesaPlateau);
        add(incidences.mesaPlateau_F,BiomeGenBase.mesaPlateau_F);
        add(incidences.plains,BiomeGenBase.plains);
        add(incidences.roofedForest,BiomeGenBase.roofedForest);
        add(incidences.savanna,BiomeGenBase.savanna);
        add(incidences.swampland,BiomeGenBase.swampland);
        add(incidences.taiga,BiomeGenBase.taiga);

        hot = new BiomeRandomizer();
        warm = new BiomeRandomizer();
        cool = new BiomeRandomizer();
        snowy = new BiomeRandomizer();

        warm.append(incidences.birchForest,BiomeGenBase.birchForest);
        snowy.append(incidences.coldTaiga,BiomeGenBase.coldTaiga);
        hot.append(incidences.desert,BiomeGenBase.desert);
        warm.append(incidences.extremeHills/2,BiomeGenBase.extremeHills);
        cool.append(incidences.extremeHills/2,BiomeGenBase.extremeHills);
        warm.append(incidences.forest/2,BiomeGenBase.forest);
        cool.append(incidences.forest/2,BiomeGenBase.forest);
        snowy.append(incidences.icePlains,BiomeGenBase.icePlains);
        warm.append(incidences.jungle,BiomeGenBase.jungle);
        cool.append(incidences.megaTaiga,BiomeGenBase.megaTaiga);
        hot.append(incidences.mesaPlateau,BiomeGenBase.mesaPlateau);
        hot.append(incidences.mesaPlateau_F,BiomeGenBase.mesaPlateau_F);
        hot.append(incidences.plains/3,BiomeGenBase.plains);
        warm.append(incidences.plains/3,BiomeGenBase.plains);
        cool.append(incidences.plains/3,BiomeGenBase.plains);
        cool.append(incidences.roofedForest,BiomeGenBase.roofedForest);
        hot.append(incidences.savanna,BiomeGenBase.savanna);
        warm.append(incidences.swampland,BiomeGenBase.swampland);
        cool.append(incidences.taiga,BiomeGenBase.taiga);
    }

    public void set(Collection<BiomeSettings> settings) {
        hot = new BiomeRandomizer();
        warm = new BiomeRandomizer();
        cool = new BiomeRandomizer();
        snowy = new BiomeRandomizer();
        ocean = new BiomeRandomizer();
        deepOcean = new BiomeRandomizer();
        nextIndex = 0;
        for (BiomeSettings setting: settings) {
            ClimateControl.logger.info(setting.toString());
            for (ClimateDistribution.Incidence incidence: setting.incidences()) {
                ClimateControl.logger.info("incidence "+incidence.biome + " rate "+incidence.incidence+" " +
                       incidence.climate.name);
                add(incidence);
            }
        }
    }

    private BiomeRandomizer randomizer(Climate climate) {
        if (climate == Climate.DEEP_OCEAN) return this.deepOcean;
        if (climate == Climate.SNOWY) return this.snowy;
        if (climate == Climate.COOL) return this.cool;
        if (climate == Climate.WARM) return this.warm;
        if (climate == Climate.HOT) return this.hot;
        if (climate == Climate.OCEAN) return this.ocean;
        throw new RuntimeException("No such climate");
    }


    private void add(ClimateDistribution.Incidence incidence) {
        append(incidence.incidence, BiomeGenBase.getBiomeGenArray()[incidence.biome]);
        if (ClimateControl.testing) {
            int checkBiome = BiomeGenBase.getBiomeGenArray()[incidence.biome].biomeID;
        }
        randomizer(incidence.climate).append(incidence.incidence, BiomeGenBase.getBiomeGenArray()[incidence.biome]);
    }

    private void add(int count, BiomeGenBase biome) {
        int lastIndex = nextIndex;
        for (nextIndex = lastIndex; nextIndex < lastIndex + count; nextIndex ++) {
            biomes[nextIndex] = biome;
        }
    }

    private void append(int count, BiomeGenBase biome) {
        int lastIndex = nextIndex;
        BiomeGenBase[] newArray = new BiomeGenBase[nextIndex + count];
        for (int i = 0; i < lastIndex ; i++) {
            newArray[i] = biomes[i];
        }
        biomes = newArray;
        for (nextIndex = lastIndex; nextIndex < lastIndex + count; nextIndex ++) {
            biomes[nextIndex] = biome;
        }
    }

    public BiomeGenBase randomBiome(GenLayerRandomBiomes asking) {
        return biomes[asking.nextInt(biomes.length)];
    }
    
    public BiomeGenBase choose(IntRandomizer source) {
        return biomes[source.nextInt(biomes.length)];
    }

    public PickByClimate pickByClimate() {return new PickByClimate();}

    public class PickByClimate {
        public int biome(int climate, IntRandomizer source) {
            if (climate == 0) return 0;
            if (climate == 1) return hot.choose(source).biomeID;
            if (climate == 2) return warm.choose(source).biomeID;
            if (climate == 3) return cool.choose(source).biomeID;
            if (climate == 4) return snowy.choose(source).biomeID;
            if (climate == BiomeGenBase.deepOcean.biomeID) return deepOcean.choose(source).biomeID;
            if (climate == BiomeGenBase.mushroomIsland.biomeID) return climate;
            if (ClimateControl.testing) {
                ClimateControl.logger.info("problem climate "+climate);
                throw new RuntimeException();
            }
            // ocean if failing and testing off
            return 0;
        }
    }
}
