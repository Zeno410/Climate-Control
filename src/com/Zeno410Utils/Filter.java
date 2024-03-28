/*
 * Available under the Lesser GPL License 3.0
 */

package com.Zeno410Utils;

import java.util.ArrayList;

/**
 *
 * @author Zeno410
 */
public abstract class Filter<Type> {
    public abstract boolean accepts(Type tested);
    public Iterable<Type> filtered(Iterable<Type> source) {
        ArrayList<Type> result = new ArrayList<Type>();
        for (Type toTest: source) {
            if (accepts(toTest)) result.add(toTest);
        }
        return result;
    }

}
