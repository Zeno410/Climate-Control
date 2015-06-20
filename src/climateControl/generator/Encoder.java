
package climateControl.generator;

import java.util.HashMap;

/**
 *
 * @author Zeno410
 */
public class Encoder implements Decoder {
    private HashMap<Integer,Integer> encodings;
    public Encoder(int [] matrix) {
        encodings = new HashMap<Integer,Integer>(matrix.length);
    }
    public Encoder(int size) {
        encodings = new HashMap<Integer,Integer>(size);
    }
    public int decode(Integer code) {return encodings.get(code);}
    public void encode(Integer code,Integer biome) {
        encodings.put(code,biome);
    }

}
