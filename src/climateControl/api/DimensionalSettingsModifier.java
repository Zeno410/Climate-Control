
package climateControl.api;

import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.event.world.WorldEvent;

/**
 *
 * @author Zeno410
 */
abstract public class DimensionalSettingsModifier {

    abstract public boolean signalCCActive(int dimension);
    abstract public void modify(int dimension, ClimateControlSettings settings);
    abstract public void onWorldLoad(WorldEvent.Load event);
    abstract public void unloadWorld(WorldEvent.Unload event);
    abstract public void serverStarted(FMLServerStartedEvent event);
    abstract public void serverStopped(FMLServerStoppedEvent event);

}
