
package climateControl.api;

import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;

/**
 *
 * @author Zeno410
 */
public class Climate {
    public static boolean legitimate(int climate) {
            if (climate == 0) return true;
            if (climate == 1) return true;
            if (climate == 2) return true;
            if (climate == 3) return true;
            if (climate == 4) return true;
            if (climate == Biome.getIdForBiome(Biomes.DEEP_OCEAN)) return true;
            if (climate == Biome.getIdForBiome(Biomes.MUSHROOM_ISLAND)) return true;
            return false;
    }
    public static Climate SNOWY = new Climate("Snowy");
    public static Climate COOL = new Climate("Cool");
    public static Climate WARM = new Climate("Warm");
    public static Climate HOT = new Climate("Hot");
    public static Climate OCEAN = new Climate("Ocean");
    public static Climate DEEP_OCEAN = new Climate("Deep_Ocean");
    public final String name;
    public Climate(String name) {
        this.name = name;
    }
}
