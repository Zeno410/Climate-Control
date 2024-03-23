
package climateControl.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.WeakHashMap;

/**
 * This class can partition an arbitrary biome distributions into sub-distributions
 * Uses are mountain/lowlands, wet/moist/arid, etc.
 * @author Zeno410
 */
public abstract class DistributionPartitioner {

    private static HashMap<String,DistributionPartitioner> registered =
            new HashMap<String,DistributionPartitioner>();
    public static void register(String name, DistributionPartitioner partitioner) {
        if (registered.containsKey(name)) {
            throw new RuntimeException("Partitioner "+name+ " already registered");
        }
        registered.put(name, partitioner);
    }

    public static Collection<DistributionPartitioner> registeredPartitioners() {
        return registered.values();
    }

    public static void unregister(String name) {
        registered.remove(name);
    }

    protected final ArrayList<IncidenceModifier> modifiers;

    // um, wow...
    private WeakHashMap<BiomeRandomizer.PickByClimate,
            HashMap<IncidenceModifier,BiomeRandomizer.PickByClimate>>
            calculatedClimates = new
            WeakHashMap<BiomeRandomizer.PickByClimate,
            HashMap<IncidenceModifier,BiomeRandomizer.PickByClimate>>();

    protected abstract IncidenceModifier modifier(int x, int z);

    public DistributionPartitioner(ArrayList<IncidenceModifier> modifiers) {
        this.modifiers = modifiers;
    }
    
    public abstract void initWorldGenSeed(long par1) ;

    public BiomeRandomizer.PickByClimate partitioned(BiomeRandomizer.PickByClimate global, int x, int z) {
        // get the stored grouping.
        HashMap<IncidenceModifier,BiomeRandomizer.PickByClimate> grouping =
                calculatedClimates.get(global);

        // if it's null create and store;
        if (grouping == null) {
            grouping = global.modifiedDistributions(modifiers);
            calculatedClimates.put(global, grouping);
        }
        return grouping.get(modifier(x,z));
    }



}
