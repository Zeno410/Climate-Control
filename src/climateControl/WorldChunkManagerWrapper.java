
package climateControl;

import climateControl.customGenLayer.GenLayerRiverMixWrapper;
import net.minecraft.world.biome.WorldChunkManager;

/**
 *
 * @author Zeno410
 */
public class WorldChunkManagerWrapper extends WorldChunkManager {

    public WorldChunkManagerWrapper(GenLayerRiverMixWrapper riverMix) {
         super();
         GenLayerUpdater.accessGenLayer.setField(this, riverMix);
         GenLayerUpdater.accessBiomeIndex.setField(this, riverMix.voronoi());
    }

}
