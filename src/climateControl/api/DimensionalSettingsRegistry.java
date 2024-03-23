
package climateControl.api;

import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import java.util.HashMap;
import net.minecraftforge.event.world.WorldEvent;

/**
 *
 * @author Zeno410
 */
public class DimensionalSettingsRegistry {
    
    private HashMap<String,DimensionalSettingsModifier> modifiers  = 
            new HashMap<String,DimensionalSettingsModifier>();

    private HashMap<Integer,Long> dimensionalSeeds = new HashMap<Integer,Long>();

    public static DimensionalSettingsRegistry instance;

    public void add(String tag, DimensionalSettingsModifier modifier) {
        modifiers.put(tag, modifier);
    }

    public void remove(String tag) {
        modifiers.remove(tag);
    }

    public boolean useCCIn(int dimension) {
        for (DimensionalSettingsModifier modifier: modifiers.values()) {
            if (modifier.signalCCActive(dimension)) return true;
        }
        return false;
    }

    public void modify(int dimension, ClimateControlSettings settings){
        for (DimensionalSettingsModifier modifier: modifiers.values()) {
            modifier.modify(dimension, settings);
        }
    }

    /*public void clearSeeds() {
        dimensionalSeeds = new HashMap<Integer,Long>();
    }

    public void setSeed(int dimension, long seed) {
        dimensionalSeeds.put(dimension, seed);
    }

    public Maybe<Integer> seedFor(int dimension) {
        if (dimensionalSeeds.containsKey(dimension)) {
            return Maybe.definitely(dimension);
        }
        return Maybe.unknown();
    }*/
    
    public void onWorldLoad(WorldEvent.Load event) {
        for (DimensionalSettingsModifier modifier: modifiers.values()) {
            modifier.onWorldLoad(event);
        }
    }

    public void unloadWorld(WorldEvent.Unload event){
        for (DimensionalSettingsModifier modifier: modifiers.values()) {
            modifier.unloadWorld(event);
        }
    }

    public void serverStarted(FMLServerStartedEvent event) {
        for (DimensionalSettingsModifier modifier: modifiers.values()) {
            modifier.serverStarted(event);
        }
    }


    public void serverStopped(FMLServerStoppedEvent event){
        for (DimensionalSettingsModifier modifier: modifiers.values()) {
            modifier.serverStopped(event);
        }
    }
}
