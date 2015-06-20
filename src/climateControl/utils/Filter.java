/*
 * Available under the Lesser GPL License 3.0
 */

package climateControl.utils;

/**
 *
 * @author Zeno410
 */
public interface Filter<Type> {
    public boolean accepts(Type tested);

}
