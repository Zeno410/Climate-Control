
package climateControl.generator;

import com.Zeno410Utils.ListWrapper;
import com.Zeno410Utils.Zeno410Logger;
import java.util.List;
import java.util.logging.Logger;
import net.minecraft.world.biome.Biome;

/**
 *
 * @author Zeno410
 */
public class VillageBiomes extends ListWrapper<Biome>{
    //public static Logger logger = new Zeno410Logger("VillageBiomes").logger();
    public VillageBiomes(List<Biome> biomes) {
        super(biomes);
    }

    @Override
    public boolean contains(Object arg0) {
        if (!(arg0 instanceof Biome)) return false;
        Biome biome = (Biome)arg0;
        boolean result =  super.contains(arg0);
        //logger.info(""+ result +" " +biome.biomeID+ " " + biome.toString());
        return result;
    }
    public void reportMembers() {
        for (Biome biome: this) {
            //logger.info(""+biome.biomeID+ " " + biome.toString());
        }
        //logger.info("done with membership");
    }
}
