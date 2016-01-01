package climateControl ;

import climateControl.api.Climate;
import climateControl.api.ClimateDistribution;
import climateControl.utils.IntRandomizer;
import climateControl.api.BiomeSettings;
import climateControl.customGenLayer.GenLayerRandomBiomes;
import java.util.Collection;
import net.minecraft.world.biome.BiomeGenBase;

/**
 *
 * @author Zeno410
 */
public class BiomeRandomizer {


    private BiomeGenBase[] biomes = new BiomeGenBase[0] ;
    private int nextIndex = 0;

    private BiomeRandomizer(){}

    public BiomeRandomizer(Collection<BiomeSettings> settings) {
        hot = new BiomeRandomizer();
        warm = new BiomeRandomizer();
        cool = new BiomeRandomizer();
        snowy = new BiomeRandomizer();
        ocean = new BiomeRandomizer();
        deepOcean = new BiomeRandomizer();
        nextIndex = 0;
        for (BiomeSettings setting: settings) {
            // skip inactive settings
            if (setting.biomesAreActive()==false) continue;
            ClimateControl.logger.info(setting.toString());
            for (ClimateDistribution.Incidence incidence: setting.incidences()) {
                ClimateControl.logger.info("incidence "+incidence.biome + " rate "+incidence.incidence+" " +
                       incidence.climate.name);
                add(incidence);
            }
        }
    }

    private BiomeRandomizer hot;
    private BiomeRandomizer warm;
    private BiomeRandomizer cool;
    private BiomeRandomizer snowy;
    private BiomeRandomizer ocean;
    private BiomeRandomizer deepOcean;

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
            if (climate == 0) return ocean.choose(source).biomeID;
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

        public boolean hasBiomes(int climate) {
            if (climate == 0) return ocean.biomes.length>0;
            if (climate == 1) return hot.biomes.length>0;
            if (climate == 2) return warm.biomes.length>0;
            if (climate == 3) return cool.biomes.length>0;
            if (climate == 4) return snowy.biomes.length>0;
            if (climate == BiomeGenBase.deepOcean.biomeID) return deepOcean.biomes.length>0;
            return false;
        }
    }
}
