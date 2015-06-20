
package climateControl.generator;

import highlands.worldgen.layer.GenLayerShoreHL;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerShore;

/**
 *
 * @author Zeno410
 */
public class HighlandsShoreGenGetter {

    public GenLayer shoreGen(GenLayer object) {
        try {
            return new GenLayerShoreHL(1000L,object);
        } catch (java.lang.NoClassDefFoundError e) {
            return new GenLayerShore(1000L, object);
        }

    }

}
