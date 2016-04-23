
package climateControl.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * This class can partition an arbitrary biome distribution into sub-distributions
 * Uses are mountain/lowlands, wet/moist/arid, etc.
 * @author Zeno410
 */
public abstract class DistributionPartitioner {

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
