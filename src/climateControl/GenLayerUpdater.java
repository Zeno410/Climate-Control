
package climateControl;

import climateControl.customGenLayer.GenLayerRiverMixWrapper;
import climateControl.utils.Accessor;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.gen.layer.GenLayer;

/**
 *
 * @author Zeno410
 */
public class GenLayerUpdater {
    public static final Accessor<WorldChunkManager,GenLayer> accessGenLayer =
            new Accessor<WorldChunkManager,GenLayer>("field_76944_d");

    public static final Accessor<WorldChunkManager,GenLayer> accessBiomeIndex =
            new Accessor<WorldChunkManager,GenLayer>("field_76945_e");

    public void update(GenLayerRiverMixWrapper riverMix, WorldProvider provider) {
       accessGenLayer.setField(provider.worldChunkMgr, riverMix);
       accessBiomeIndex.setField(provider.worldChunkMgr, riverMix.voronoi());
       try {
           new Myst11GenLayerUpdater().update(riverMix, provider);
       } catch (java.lang.NoClassDefFoundError e) {
           DimensionManager.logger.info("no class for Myst update");
           //no Mystcraft; do nothing
       }
    }

}
