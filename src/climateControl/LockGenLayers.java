
package climateControl;

import climateControl.api.ClimateControlSettings;
import climateControl.customGenLayer.GenLayerBiomeByClimate;
import climateControl.customGenLayer.GenLayerBiomeByTaggedClimate;
import climateControl.customGenLayer.GenLayerRandomBiomes;
import climateControl.customGenLayer.GenLayerSmoothClimate;
import climateControl.genLayerPack.GenLayerHillsOneSix;
import climateControl.genLayerPack.GenLayerOneSixBiome;
import climateControl.genLayerPack.GenLayerPack;
import com.Zeno410Utils.Acceptor;
import com.Zeno410Utils.Accessor;
import com.Zeno410Utils.ChunkLister;
import com.Zeno410Utils.Filter;
import com.Zeno410Utils.PlaneLocation;
import java.util.ArrayList;
import java.util.HashSet;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerBiome;
import net.minecraft.world.gen.layer.IntCache;

/**
 *
 * @author Zeno410
 */
public class LockGenLayers {
    
    private HashSet<LockGenLayer> toGenerateFor = new HashSet<LockGenLayer>();
    public static final int LOCATIONS_BEFORE_RESET  = 1;
    LockGenLayer biomeLock= new LockGenLayer("Biomes",isBiomeLayer(),Acceptor.to(toGenerateFor));
    LockGenLayer subBiomeLock= new LockGenLayer("SubBiomes",isSubBiomeLayer(),Acceptor.to(toGenerateFor));
    private Accessor<GenLayer,GenLayer> genLayerParent = new Accessor<GenLayer,GenLayer>("field_75909_a");

    public LockGenLayers() {

    }

    public void showGenLayers(GenLayer top) {
        biomeLock.showGenLayers(top);
    }
    
    public void lock(GenLayer top, int dimension, World world, ClimateControlSettings settings) {
        ClimateControl.logger.info("locking generation on "+top.toString());
        //showGenLayers(top);
        if ((settings.climateRingsNotSaved.value() > -1)&&(settings.oneSixCompatibility.value()== false)) {

            // not putting climateLock into an instance var because the filter needs to be changed
            LockGenLayer climateLock= new LockGenLayer("Climate",smoothClimateParent(),Acceptor.to(toGenerateFor));
            boolean found = climateLock.lock(top, dimension, world, settings.climateRingsNotSaved.value(), false);
            if (!found) {
                // couldn't find a SmoothClimate layer; look for Mushroom Island layer
                climateLock= new LockGenLayer("Climate",mushroomIslandParent(),Acceptor.to(toGenerateFor));
                climateLock.lock(top, dimension, world, settings.climateRingsNotSaved.value(), false);
            }

        }
        if (settings.biomeRingsNotSaved.value()> -1) {
           biomeLock.lock(top, dimension, world, settings.biomeRingsNotSaved.value(), false);
        }

        if (settings.subBiomeRingsNotSaved.value()> -1) {
           subBiomeLock.lock(top, dimension, world, settings.subBiomeRingsNotSaved.value(), false);
        }


        // if anything is new, look at all chunks in the world to force caching
        if(!(world instanceof WorldServer)) return;
        if (toGenerateFor.size()>0) {
            ArrayList<PlaneLocation> existingChunks = new ChunkLister().savedChunks(levelSavePath((WorldServer)world));
            LockGenLayer.logger.info ("chunk count" + existingChunks.size());
            int fixed = 0;
            int sinceLastReset = 0;
            for (PlaneLocation chunkLocation: existingChunks) {
                if (++sinceLastReset>LOCATIONS_BEFORE_RESET) {
                    IntCache.resetIntCache();
                    sinceLastReset = 0;
                }
                //logger.info(" fixed "+fixed++);
                int [] stored= top.getInts(chunkLocation.x()<<2, chunkLocation.z()<<2, 4, 4);
            }
            IntCache.resetIntCache();
        }

    }

    private String levelSavePath(WorldServer world) {
        String result = "";
        result = world.getChunkSaveLocation().getAbsolutePath();
        return result;
    }

    private Filter<GenLayer> isBiomeLayer() {
        return new Filter<GenLayer>() {
            public boolean accepts(GenLayer tested) {
                if (tested == null) return false;
                //LockGenLayer.logger.info(tested.getClass().getCanonicalName());
                //if (tested.getClass().getCanonicalName().contains("GenLayerBiome")) return true; BiomeLayerBiomes
                if (tested instanceof GenLayerBiome) return true;
                if (tested instanceof GenLayerBiomeByClimate) return true;
                if (tested instanceof GenLayerBiomeByTaggedClimate) return true;
                if (tested instanceof GenLayerRandomBiomes) return true;
                if (tested instanceof GenLayerOneSixBiome) return true;
                if (tested.getClass().getCanonicalName().contains("BiomeLayerBiomes")) return true;
                if (tested.getClass().getCanonicalName().contains("GenLayerBiomeEdgeHL")) return true;
                if (tested.getClass().getCanonicalName().contains("GenLayerBiomeByTaggedClimate")) return true;
                return false;
            }

        };
    }

    private Filter<GenLayer> isSubBiomeLayer() {
        return new Filter<GenLayer>() {
            public boolean accepts(GenLayer tested) {
                if (tested == null) return false;
                // could be either the minecraft GenLayerRareBiome or a modded one
                // so do this by name
                //LockGenLayer.logger.info(tested.getClass().getCanonicalName());
                if (tested.getClass().getCanonicalName().contains("GenLayerRareBiome")) return true;
                if (tested.getClass().getCanonicalName().contains("BiomeLayerSub")) return true;
                if (tested instanceof GenLayerHillsOneSix) return true;
                return false;
            }

        };
    }
    
    private Filter<GenLayer> smoothClimateParent() {
        return new Filter<GenLayer>() {
            private GenLayer smoothClimateLayer = null;
            public boolean accepts(GenLayer tested) {
                if (tested == null) return false;
                //LockGenLayer.logger.info(tested.toString());
                if (tested instanceof GenLayerSmoothClimate||tested instanceof GenLayerBiomeByTaggedClimate) {
                    smoothClimateLayer = tested;
                    //LockGenLayer.logger.info("smooth climate is "+tested.toString());
                    //LockGenLayer.logger.info("parent is "+parent(tested).toString());
                    return false; // obviously not the parent
                }
                // not parent if we haven't hit the smoothclimate layer
                if (smoothClimateLayer == null) return false;
                if (tested.equals(parent(smoothClimateLayer))) {
                    //LockGenLayer.logger.info("smooth climate on "+tested.toString());
                    return true;
                }
                return false;
            }

        };
    }
    
    private Filter<GenLayer> mushroomIslandParent() {
        // this is for caching climates in the vanilla generator{

        return new Filter<GenLayer>() {
            private GenLayer mushroomIslandLayer = null;
            public boolean accepts(GenLayer tested) {
                if (tested == null) return false;
                if (tested.getClass().getName().contains("GenLayerAddMushroomIsland") ) {
                    mushroomIslandLayer = tested;
                    return false; // obviously not the parent
                }
                // not parent if we haven't hit the smoothclimate layer
                if (mushroomIslandLayer == null) return false;
                if (tested.equals(parent(mushroomIslandLayer))) return true;
                return false;
            }

        };
    }

    public GenLayer parent(GenLayer child) {
        if (child instanceof GenLayerPack) {
            return ((GenLayerPack)child).getParent();
        }
        return genLayerParent.get(child);
    }
}
