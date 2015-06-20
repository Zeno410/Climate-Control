
package climateControl.customGenLayer;

import climateControl.genLayerPack.GenLayerPack;
import java.util.HashMap;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

/**
 * this class creates random codes for each location and uses the codes to store the values of the parent map
 * the codes can then be retrieved by lookup after other genlayer alterations
 * @author Zeno410
 */
public class GenLayerEncode extends GenLayerPack {


    private HashMap<Integer,Integer> encoding;
    public GenLayerEncode(long p_i45480_1_, GenLayer p_i45480_3_){
        super(p_i45480_1_);
        this.parent = p_i45480_3_;
    }

    /**
     * Every pixel of "water" (value 0) has percentFilled chance of being made land
     * amounts, or biomeList[] indices based on the particular GenLayer subclass.
     */
    public int[] getInts(int par1, int par2, int par3, int par4) {
        int i1 = par1;
        int j1 = par2;
        int k1 = par3;
        int l1 = par4;
        int[] aint = this.parent.getInts(i1, j1, k1, l1);
        int[] aint1 = IntCache.getIntCache(par3 * par4);

        encoding = new HashMap<Integer,Integer>(par3 * par4);
        for (int i2 = 0; i2 < par4; ++i2){
            for (int j2 = 0; j2 < par3; ++j2){
                this.initChunkSeed((long)(j2 + par1), (long)(i2 + par2));
                int code = nextInt(Integer.MAX_VALUE);
                aint1[j2 + i2 * par3] = code;
                encoding.put(code,aint[j2 + i2 * par3]);
            }
        }

        return aint1;
    }

    public void decode(int[] coded) {
        // decodes in place
        for (int i = 0; i<coded.length;i++) {
            coded[i] = encoding.get(coded[i]);
        }
    }
}