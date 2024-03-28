
package climateControl.generator;

import climateControl.api.BiomeSettings;
import java.util.ArrayList;

/**
 *
 * @author Zeno410
 */
public class BiomeSwapper {
    private int[] swaps = new int[BiomeSettings.highestBiomeID()+1];

    public BiomeSwapper() {
        for (int i = 0; i < swaps.length; i++) {
            swaps[i] = i;
        }
    }
    public void clear() {
        for (int i = 0 ; i < swaps.length; i++) swaps[i] = i;
    }

    public void set (int biome, int replacement) {
        swaps[biome] = replacement;
    }

    public void set(ArrayList<BiomeSettings> settings) {
        for (BiomeSettings setting: settings) {
            if (setting.biomesAreActive()) setting.updateMBiomes(this);
        }
    }

    public int replacement(int original) {return swaps[original];}
}
