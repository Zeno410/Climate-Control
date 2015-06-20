
package climateControl.biomeSettings;

import climateControl.utils.IntRandomizer;
import java.util.List;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeManager.BiomeEntry;

import org.apache.commons.lang3.tuple.Pair;

import biomesoplenty.common.biome.BOPSubBiome;
import biomesoplenty.common.world.BOPBiomeManager;
import biomesoplenty.common.world.noise.SimplexNoise;

/**
 *
 * @author Zeno410
 * Mostly reworked code from Biome o' Plenty
 */
public class BoPSubBiomeReplacer extends BiomeReplacer {
	private final int OFFSET_RANGE = 500000;

	private Pair<Integer, Integer>[] offsets = new Pair[BiomeGenBase.getBiomeGenArray().length];
    public BoPSubBiomeReplacer(IntRandomizer randomizer) {
        setOffsets(randomizer);
	}

    @Override
    public int replacement(int currentBiomeId, IntRandomizer randomizer, int x, int z) {

        List<BiomeEntry> currentSubBiomes = BOPBiomeManager.overworldSubBiomes[currentBiomeId];
        BOPSubBiome selectedSubBiome = currentSubBiomes != null ? (BOPSubBiome)currentSubBiomes.get(
                randomizer.nextInt(currentSubBiomes.size())).biome : null;

        if (selectedSubBiome != null){
            Pair<Integer, Integer> offset = getOffset(selectedSubBiome);

            if (SimplexNoise.noise((x + offset.getLeft()) * selectedSubBiome.zoom,
                    (z+ offset.getRight()) * selectedSubBiome.zoom)
                    > selectedSubBiome.threshold){

                try {
                    BiomeGenBase selected = (BiomeGenBase)selectedSubBiome;
                    return selected.biomeID;
                } catch (RuntimeException e) {
                    throw e;
                }
            }
        }
       return currentBiomeId;
    }

    private Pair<Integer, Integer> getOffset(BiomeGenBase biome) {
    	return offsets[biome.biomeID];
    }

    private void setOffsets(IntRandomizer randomizer) {
    	for (int i = 0; i< offsets.length; i++){
    		offsets[i] = Pair.of(
                    randomizer.nextInt(OFFSET_RANGE) - (OFFSET_RANGE / 2),
                    randomizer.nextInt(OFFSET_RANGE) - (OFFSET_RANGE / 2));
         }
    }
}
