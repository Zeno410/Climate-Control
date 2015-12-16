
package climateControl;

import java.io.File;
import net.minecraft.world.WorldServer;

/**
 *
 * @author Zeno410
 */
public class DimensionAccess extends MinecraftFilesAccess{
    public final int dimension;
    private final File dimensionDirectory;
    public DimensionAccess(int dimension, WorldServer world) {
        this.dimension = dimension;
        this.dimensionDirectory = world.getChunkSaveLocation();
    }

    public DimensionAccess(int dimension, File file) {
        this.dimension = dimension;
        this.dimensionDirectory = file;
    }

    public File configDirectory() {
        return new File(dimensionDirectory,"worldSpecificConfig");
    }

    @Override
    public File baseDirectory() {
        return dimensionDirectory;
    }
}
