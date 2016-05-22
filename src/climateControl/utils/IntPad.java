
package climateControl.utils;

import java.util.WeakHashMap;

/**
 * Ant int array scratchpad
 * @author Zeno410
 */
public class IntPad {
    private WeakHashMap<Thread,int []> pads = new WeakHashMap<Thread,int []>();
    
    public synchronized int [] pad(int size) {
        int [] result = pads.get(Thread.currentThread());
        if ( result == null||size != result.length) {
            result = new int[size];
            pads.put(Thread.currentThread(), result);
            return result;
        }
        return result;
    }

}
