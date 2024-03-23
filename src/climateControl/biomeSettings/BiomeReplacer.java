
package climateControl.biomeSettings;

import climateControl.api.BiomeSettings;
import climateControl.generator.SubBiomeChooser;
import com.Zeno410Utils.IntRandomizer;
import com.Zeno410Utils.RandomIntUser;

/**
 *
 * @author Zeno410
 */
abstract public class BiomeReplacer {
    public abstract int replacement(int biome, IntRandomizer randomizer, int x, int y);

    public static class Variable extends BiomeReplacer {
        private int totalPossibilities;
        private int [] alternatives = new int[0];

        public void add(BiomeSettings.ID biome,int count) {
            totalPossibilities += count;
            int [] newAlternatives = new int[alternatives.length+count];
            for (int i = 0; i < alternatives.length;i++) newAlternatives[i]= alternatives[i];
            for (int i = alternatives.length; i < newAlternatives.length; i ++ ) {
                newAlternatives[i] = biome.biomeID().value();
                //BiomeSettings.logger.info("subBiome added" + biome.biomeID().value());
            }
            alternatives = newAlternatives;
        }
        
        public void addByNumber(int biome,int count) {
            totalPossibilities += count;
            int [] newAlternatives = new int[alternatives.length+count];
            for (int i = 0; i < alternatives.length;i++) newAlternatives[i]= alternatives[i];
            for (int i = alternatives.length; i < newAlternatives.length; i ++ ) {
                newAlternatives[i] = biome;
                //BiomeSettings.logger.info("subBiome added" + biome.biomeID().value());
            }
            alternatives = newAlternatives;
        }

        public int replacement(int biome,IntRandomizer randomizer, int x, int y) {
            if (totalPossibilities == 0) return biome;
            int choice = randomizer.nextInt(totalPossibilities);
            if (choice >= alternatives.length) return biome;
            return alternatives[choice];
        }
    }

    public static class Fixed extends BiomeReplacer {
        private final int replacement;
        public Fixed(int replacement) {this.replacement = replacement;}
        public int replacement(int biome,IntRandomizer randomizer,int x, int y) {
            return replacement;
        }
    }

    public static final BiomeReplacer noChange = new NoChange();

    private static class NoChange extends BiomeReplacer {
        public int replacement(int biome,IntRandomizer randomizer,int x, int y) {
            return biome;
        }
    }

    public static class Multiple extends BiomeReplacer {
        private final BiomeReplacer first;
        private final BiomeReplacer second;
        public Multiple (BiomeReplacer first, BiomeReplacer second) {
            this.first = first;
            this.second = second;
        }

        @Override
        public int replacement(int biome, IntRandomizer randomizer, int x, int y) {
            int result = first.replacement(biome,randomizer,x,y);
            if (biome != result) return result;
            return second.replacement(biome, randomizer, x, y);
        }
    }
}
