
package climateControl.customGenLayer;

import climateControl.generator.Decoder;
import net.minecraft.world.gen.layer.GenLayer;
/**
 *
 * @author Zeno410
 */
public class HashEncodedSmoothWithClimates extends EncodedSmoothWithClimates {
    private final Decoder biomeEncoder;

    public HashEncodedSmoothWithClimates(long par1, GenLayer par3GenLayer,Decoder biomeEncoder){
        super(par1, par3GenLayer);
        this.biomeEncoder = biomeEncoder;
        if (super.parent == null) throw new RuntimeException();
    }

    final int decoded(int code) {
        //try {
            return this.biomeEncoder.decode(code);
        /*} catch (NullPointerException e) {
            logger.info("missed on "+ code);
            throw e;
        }*/
    }
}