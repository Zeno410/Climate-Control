
package climateControl.customGenLayer;

import climateControl.DimensionManager;
import climateControl.api.ClimateControlSettings;
import climateControl.LockGenLayers;
import com.Zeno410Utils.Accessor;
import com.Zeno410Utils.Maybe;
import com.Zeno410Utils.Zeno410Logger;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerRiverMix;
import net.minecraft.world.gen.layer.GenLayerVoronoiZoom;

/**
 *
 * @author Zeno410
 */
public class GenLayerRiverMixWrapper extends GenLayerRiverMix{

    public static Logger logger = new Zeno410Logger("GenLayerRiverMixWrapper").logger();
    private GenLayer redirect;
    private GenLayer voronoi;
    private GenLayer original;
    private Accessor<GenLayerRiverMix,GenLayer> riverMixBiome =
            new Accessor<GenLayerRiverMix,GenLayer>("field_75910_b");
    
    private LockGenLayers biomeLocker = new LockGenLayers();
    private DimensionManager dimensionManager;

    private static GenLayer dummyGenLayer() {
        return new GenLayer(0L) {

            @Override
            public int[] getInts(int var1, int var2, int var3, int var4) {
                return new int [var3*var4];
            }

        };
    }


    public GenLayerRiverMixWrapper(long baseSeed, GenLayer nonGCLayers, DimensionManager dimensionManager) {
        super(baseSeed,dummyGenLayer(),dummyGenLayer());
        voronoi = new GenLayerVoronoiZoom(baseSeed,this);
        this.original = nonGCLayers;
        this.dimensionManager = dimensionManager;
    }

    private boolean found = false;
    
    private void findSelf() {
        if (found) return;
        Integer [] dimensions = net.minecraftforge.common.DimensionManager.getIDs();
        for (Integer dimension: dimensions) {
            
            BiomeProvider provider = net.minecraftforge.common.DimensionManager.getProvider(dimension).getBiomeProvider();
            GenLayer topLayer = workingGenLayer(provider);
            if (topLayer == this) {
                logger.info(topLayer.toString() + " dimension " + dimension);
                found = true;
                WorldServer world = net.minecraftforge.common.DimensionManager.getWorld(dimension);
                Maybe<GenLayerRiverMix> gcLayers = dimensionManager.getGeographicraftGenlayers(world, dimension, original);
                if (gcLayers.isKnown()) {
                    redirect = gcLayers.iterator().next();
                } else {
                    redirect = original;
                }
                return;
            } else {
                
                logger.info(dimension + " not a match for " + original.toString());
            }
        }
        // fallback in case some mod has changed the genlayers somehow
        found = true;
        redirect = original;
    }
    
    private GenLayer workingGenLayer(BiomeProvider provider) {
        Accessor<BiomeProvider,GenLayer> worldGenLayer =
                   new Accessor<BiomeProvider,GenLayer>("field_76944_d");
            return worldGenLayer.get(provider);
    }

    @Override
    public int[] getInts(int arg0, int arg1, int arg2, int arg3) {
        //logger.info("generating ");
        findSelf();
        return redirect.getInts(arg0, arg1, arg2, arg3);
    }

    @Override
    public void initChunkSeed(long par1, long par3) {
        super.initChunkSeed(par1, par3);
        findSelf();
        redirect.initChunkSeed(par1, par3);
    }

    @Override
    public void initWorldGenSeed(long arg0) {
        super.initWorldGenSeed(arg0);
        findSelf();
        redirect.initWorldGenSeed(arg0);
        //voronoi.initWorldGenSeed(arg0);
    }
    public GenLayer voronoi() {return voronoi;}

    public GenLayer [] modifiedGenerators() {
        return new GenLayer [] {this, voronoi, this};
    }

}
