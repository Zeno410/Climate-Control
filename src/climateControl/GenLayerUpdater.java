
package climateControl;

import climateControl.customGenLayer.GenLayerRiverMixWrapper;
import com.Zeno410Utils.Accessor;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.layer.GenLayer;

/**
 *
 * @author Zeno410
 */
public class GenLayerUpdater {
    public static final Accessor<BiomeProvider,GenLayer> accessGenLayer =
            new Accessor<BiomeProvider,GenLayer>("field_76944_d");

    public static final Accessor<BiomeProvider,GenLayer> accessBiomeIndex =
            new Accessor<BiomeProvider,GenLayer>("field_76945_e");

    public void update(GenLayerRiverMixWrapper riverMix, WorldProvider provider) {
       accessGenLayer.setField(provider.getBiomeProvider(), riverMix);
       accessBiomeIndex.setField(provider.getBiomeProvider(), riverMix.voronoi());
    }

}
