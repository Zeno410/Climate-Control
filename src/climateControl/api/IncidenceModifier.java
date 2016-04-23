/*
 * Available under the Lesser GPL License 3.0
 */

package climateControl.api;

import climateControl.utils.Numbered;
import net.minecraft.world.biome.BiomeGenBase;

/**
 *
 * @author Zeno410
 */
public interface IncidenceModifier {
    public int modifiedIncidence(Numbered<BiomeGenBase> biomeIncidence);
}
