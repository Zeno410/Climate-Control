
package climateControl;

import com.google.common.collect.ImmutableSetMultimap;
import fwg.world.WorldTypeFWG;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager.Ticket;

/**
 *
 * @author Zeno410
 */
public class FunWorldBridge {
    // fixes up FunWorld worlds

    public void fixWorld(World world) {
        if (world.getWorldTime()>10) return;
        if (world.getChunkProvider().getLoadedChunkCount()>100) return;
        ImmutableSetMultimap<ChunkCoordIntPair, Ticket>  persistentChunks = world.getPersistentChunks();

    }

}
