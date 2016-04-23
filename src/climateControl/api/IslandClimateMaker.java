/*
 * Available under the Lesser GPL License 3.0
 */

package climateControl.api;

import climateControl.utils.IntRandomizer;

/**
 *
 * @author Zeno410
 */
public interface IslandClimateMaker {
    public int climate(int x, int z, IntRandomizer randomizer);

}
