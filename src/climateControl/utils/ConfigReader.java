
package climateControl.utils;

import net.minecraftforge.common.config.Configuration;

/**
 *
 * @author Zeno410
 */
public interface ConfigReader {
    public void readFrom(Configuration source);
    public void copyTo(Configuration target);
}
