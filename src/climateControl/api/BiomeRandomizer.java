package climateControl.api ;

import climateControl.utils.IntRandomizer;
import climateControl.utils.Numbered;
import climateControl.utils.Zeno410Logger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Logger;
import net.minecraft.world.biome.BiomeGenBase;

/**
 *
 * @author Zeno410
 */
public class BiomeRandomizer {

    public static Logger logger = new Zeno410Logger("BiomeRandomizer").logger();

    private BiomeRandomizer(){}

    public BiomeRandomizer(Collection<BiomeSettings> settings) {
        global = new BiomeList();
        hot = new BiomeList();
        warm = new BiomeList();
        cool = new BiomeList();
        snowy = new BiomeList();
        ocean = new BiomeList();
        deepOcean = new BiomeList();
        for (BiomeSettings setting: settings) {
            // skip inactive settings
            if (setting.biomesAreActive()==false) continue;
            //ClimateControl.logger.info(setting.toString());
            for (ClimateDistribution.Incidence incidence: setting.incidences()) {
                //ClimateControl.logger.info("incidence "+incidence.biome + " rate "+incidence.incidence+" " +incidence.climate.name);
                add(incidence);
            }
        }
    }

    public BiomeList global;
    private BiomeList hot;
    private BiomeList warm;
    private BiomeList cool;
    private BiomeList snowy;
    private BiomeList ocean;
    private BiomeList deepOcean;

    private BiomeList randomizer(Climate climate) {
        if (climate == Climate.DEEP_OCEAN) return this.deepOcean;
        if (climate == Climate.SNOWY) return this.snowy;
        if (climate == Climate.COOL) return this.cool;
        if (climate == Climate.WARM) return this.warm;
        if (climate == Climate.HOT) return this.hot;
        if (climate == Climate.OCEAN) return this.ocean;
        throw new RuntimeException("No such climate");
    }

    public int size() {
        return hot.biomes.length + ocean.biomes.length + deepOcean.biomes.length + snowy.biomes.length
                +cool.biomes.length + warm.biomes.length;
    }

    private void add(ClimateDistribution.Incidence incidence) {
        global.append(incidence.incidence, BiomeGenBase.getBiomeGenArray()[incidence.biome]);
        //if (ClimateControl.testing) {
            //int checkBiome = BiomeGenBase.getBiomeGenArray()[incidence.biome].biomeID;
        //}
        randomizer(incidence.climate).append(incidence.incidence, BiomeGenBase.getBiomeGenArray()[incidence.biome]);
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
            //if (ClimateControl.testing) {
                //ClimateControl.logger.info("problem climate "+climate);
                //throw new RuntimeException();
            //}
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

         public HashMap<IncidenceModifier,PickByClimate> modifiedDistributions(
                 ArrayList<IncidenceModifier> comprehensiveModifiers) {

             HashMap<IncidenceModifier,PickByClimate> result =
                     new HashMap<IncidenceModifier,PickByClimate>();

             // if we produce a 0-item list we have to forgo the modification for that climate
             // we'll keep a list to check
             ArrayList<BiomeRandomizer> randomizers = new ArrayList<BiomeRandomizer>();

             for (IncidenceModifier modifier: comprehensiveModifiers) {
                 logger.info(modifier.toString());
                 BiomeRandomizer randomizer = modifiedBy(modifier);
                 result.put(modifier, randomizer.pickByClimate());
                 randomizers.add(randomizer);
             }

             // now check for problems and revert if necessary
             // changing the randomizers also changes the PickByClimates we're returning;
             for (BiomeRandomizer randomizer: randomizers) {
                 if (randomizer.ocean.biomes.length == 0) {
                     for (BiomeRandomizer changed: randomizers) {
                         changed.ocean = BiomeRandomizer.this.ocean;
                     }
                 }
                 if (randomizer.cool.biomes.length == 0) {
                     for (BiomeRandomizer changed: randomizers) {
                         changed.cool = BiomeRandomizer.this.cool;
                     }
                 }
                 if (randomizer.deepOcean.biomes.length == 0) {
                     for (BiomeRandomizer changed: randomizers) {
                         changed.deepOcean = BiomeRandomizer.this.deepOcean;
                     }
                 }
                 if (randomizer.hot.biomes.length == 0) {
                     for (BiomeRandomizer changed: randomizers) {
                         changed.hot = BiomeRandomizer.this.hot;
                     }
                 }
                 if (randomizer.snowy.biomes.length == 0) {
                     for (BiomeRandomizer changed: randomizers) {
                         changed.snowy = BiomeRandomizer.this.snowy;
                     }
                 }
                 if (randomizer.warm.biomes.length == 0) {
                     for (BiomeRandomizer changed: randomizers) {
                         changed.warm = BiomeRandomizer.this.warm;
                     }
                 }
             }

             return result;
         }
    }





    private BiomeRandomizer modifiedBy(IncidenceModifier modifier) {

        if (ocean.biomes.length <1) throw new RuntimeException();
        BiomeRandomizer result = new BiomeRandomizer();
        result.cool = cool.modifiedSubRandomizer(modifier);
        result.deepOcean = deepOcean.modifiedSubRandomizer(modifier);
        result.hot = hot.modifiedSubRandomizer(modifier);
        result.ocean = ocean.modifiedSubRandomizer(modifier);
        result.snowy = snowy.modifiedSubRandomizer(modifier);
        result.warm = warm.modifiedSubRandomizer(modifier);
        return result;
    }


    public class BiomeList {
        private BiomeGenBase[] biomes = new BiomeGenBase[0] ;
        private int nextIndex = 0;

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

        public BiomeGenBase choose(IntRandomizer source) {
            return biomes[source.nextInt(biomes.length)];
        }

        private BiomeList modifiedSubRandomizer(IncidenceModifier modifier) {
            BiomeList result = new BiomeList();
            for (Numbered<BiomeGenBase> oldIncidence: incidences()) {
                int newIncidence = modifier.modifiedIncidence(oldIncidence);
                logger.info(oldIncidence.item().biomeName + " "+ oldIncidence.count() + " to " + newIncidence);
                if (newIncidence>0) {
                    result.append(newIncidence, oldIncidence.item());
                }
            }
            return result;
        }

        private ArrayList<Numbered<BiomeGenBase>> incidences() {
            ArrayList<Numbered<BiomeGenBase>> result= new ArrayList<Numbered<BiomeGenBase>>();
            BiomeGenBase current = null;
            int occurances = 0;
            for (BiomeGenBase biome: this.biomes) {
                if (biome == current) {
                    occurances ++;
                } else {
                    if (occurances >0) {
                        result.add(Numbered.from(occurances, current));
                        current = biome;
                        occurances = 1;
                    }
                    current = biome;
                }
            }
            result.add(Numbered.from(occurances, current));
            logger.info(""+biomes.length + " biomes " + result.size() + " incidencese ");
            return result;
        }
    }
}
