
package com.Zeno410Utils;

import net.minecraftforge.common.config.Configuration;

/**
 *
 * @author Zeno410
 */
public interface ConfigReader {
    public void readFrom(Configuration source);
    public void copyTo(Configuration target);
}
