
package climateControl.customGenLayer;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

/**
 * returns a constant value
 * @author Zeno410
 */

public class GenLayerConstant extends GenLayer {
    private final int value;
    public GenLayerConstant(long seed, int value) {
        super(seed);
        this.value  = value;
    }
    public GenLayerConstant(long seed) {this(seed,0);}

    public int[] getInts(int par1, int par2, int par3, int par4){

        int[] aint2 = IntCache.getIntCache(par3 * par4);
        for (int i = 0; i < aint2.length;i++) {
            aint2[i] = value;
        }
        return aint2;
    }

}