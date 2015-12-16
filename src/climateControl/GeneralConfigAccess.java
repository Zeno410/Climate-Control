
package climateControl;

import java.io.File;

/**
 *
 * @author Zeno410
 */
public class GeneralConfigAccess extends MinecraftFilesAccess {
    private final File configDirectory;
    public GeneralConfigAccess(File configDirectory) {
        this.configDirectory = configDirectory;
    }

    @Override
    public File baseDirectory() {
        return configDirectory.getParentFile();
    }

    @Override
    public File configDirectory() {
        return configDirectory;
    }

}
