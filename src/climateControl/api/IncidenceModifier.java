/*
 * Available under the Lesser GPL License 3.0
 */

package climateControl.api;

import com.Zeno410Utils.Numbered;
import net.minecraft.world.biome.Biome;

/**
 *
 * @author Zeno410
 */
public interface IncidenceModifier {
    public int modifiedIncidence(Numbered<Biome> biomeIncidence);
}
