
package climateControl.customGenLayer;

import climateControl.api.ClimateControlSettings;
import climateControl.LockGenLayers;
import climateControl.utils.Accessor;
import climateControl.utils.Zeno410Logger;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.world.World;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerRiverMix;
import net.minecraft.world.gen.layer.GenLayerVoronoiZoom;

/**
 *
 * @author Zeno410
 */
public class GenLayerRiverMixWrapper extends GenLayerRiverMix{

    public static Logger logger = new Zeno410Logger("GenLayerRiverMixWrapper").logger();
    private LockableRiverMix redirect;
    private GenLayer voronoi;
    private LockableRiverMix original;
    private Accessor<GenLayerRiverMix,GenLayer> riverMixBiome =
            new Accessor<GenLayerRiverMix,GenLayer>("field_75910_b");
    
    private LockGenLayers biomeLocker = new LockGenLayers();


    public GenLayerRiverMixWrapper(long baseSeed) {
        super(baseSeed,null,null);
        voronoi = new GenLayerVoronoiZoom(baseSeed,this);
    }

    public void setOriginal(GenLayer target) {
        if (original == null) {
            original = new LockableRiverMix(target);
        }
    }

    public void useOriginal() {
        if (redirect != original) {
            redirect = original;
            logger.info("using original "+original.toString());
            return;
        }
        logger.info("already using original ");
    }

    public void setRedirection(GenLayerRiverMix target) {
        if (locked()) {
            logger.info("already locked prior to "+target.toString());
            return;
        }
        logger.info("redirecting to "+target.toString());
        redirect = new LockableRiverMix(target);
    }

    @Override
    public int[] getInts(int arg0, int arg1, int arg2, int arg3) {
        //logger.info("generating ");
        return redirect.generator.getInts(arg0, arg1, arg2, arg3);
    }

    @Override
    public void initChunkSeed(long par1, long par3) {
        super.initChunkSeed(par1, par3);
        redirect.generator.initChunkSeed(par1, par3);
    }

    @Override
    public void initWorldGenSeed(long arg0) {
        super.initWorldGenSeed(arg0);
        redirect.generator.initWorldGenSeed(arg0);
    }
    public void replaceWorldSeed(long newSeed) {
        voronoi.initWorldGenSeed(newSeed);
    }

    public void clearRedirection() {
        redirect = null;
    }

    public void lock(int dimension, World world, ClimateControlSettings newSettings) {
        redirect.lock(dimension, world, newSettings);
    }

    public boolean locked() {
        if (redirect == null) return false;
        return this.redirect.locked;
    }
    
    public GenLayer voronoi() {return voronoi;}

    public GenLayer [] modifiedGenerators() {
        return new GenLayer [] {this, voronoi, this};
    }

    private class Unlockable extends Exception {}

    private class LockableRiverMix {
        boolean locked = false;
        private GenLayer generator;
        LockableRiverMix(GenLayer generator) {
            this.generator = generator;
        }

        void lock(int dimension, World world, ClimateControlSettings newSettings) {
            if (locked) return;
            try {
                biomeLocker.lock(forLocking(), dimension, world, newSettings);
                locked = true;
            } catch (Unlockable ex) {
                //Logger.getLogger(GenLayerRiverMixWrapper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        public GenLayer forLocking() throws Unlockable {
            if (generator instanceof GenLayerLowlandRiverMix) {
                return ((GenLayerLowlandRiverMix)generator).forLocking();
            }
            if (generator instanceof GenLayerRiverMix) {
               return riverMixBiome.get((GenLayerRiverMix)generator);
            }
            try {

            } catch (java.lang.NoClassDefFoundError e){
            // EBXL isn't installed
            }
            throw new Unlockable();
        }
    }

}
