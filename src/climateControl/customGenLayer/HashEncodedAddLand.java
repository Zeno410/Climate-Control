
package climateControl.customGenLayer;

import climateControl.genLayerPack.GenLayerPack;
import climateControl.generator.Decoder;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

/**
 * @author Zeno410
 */
public class HashEncodedAddLand extends EncodedAddLand {
    private static final String __OBFID = "CL_00000551";
    private final Decoder biomeEncoder;

    public HashEncodedAddLand(long par1, GenLayer par3GenLayer, Decoder biomeEncoder) {
        super(par1,par3GenLayer);
        this.biomeEncoder = biomeEncoder;
    }

    final int decoded(int code) {return this.biomeEncoder.decode(code);}
}