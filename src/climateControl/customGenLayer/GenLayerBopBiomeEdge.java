package climateControl.customGenLayer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;

import biomesoplenty.api.biome.BOPBiomes;
import climateControl.genLayerPack.GenLayerPack;
import climateControl.generator.MountainFormer;
import net.minecraft.init.Biomes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.registries.GameData;
public class GenLayerBopBiomeEdge extends GenLayerPack
{
    private static final String __OBFID = "CL_00000554";
    private boolean[] bopMountain;
    private boolean[] mountain;
    private int[] bopMountainEdges;
	private int redwoodForest = -1;
	private int redwoodForestEdge = -1;

    public GenLayerBopBiomeEdge(long p_i45475_1_, GenLayer p_i45475_3_, int maxBiome){
        super(p_i45475_1_);
        this.parent = p_i45475_3_;
    	bopMountain = new boolean[maxBiome+1];
    	mountain = new boolean[maxBiome+1];
    	bopMountainEdges = new int[maxBiome+1];
    	Method method = MountainFormer.getBiomeTypeMethod();
    	
        Iterator<ResourceLocation> registries = Biome.REGISTRY.getKeys().iterator();
        // set flags for all biomes tagged "mountain"
        while (registries.hasNext()) {
        	ResourceLocation location = registries.next();
        	Biome biome = Biome.REGISTRY.getObject(location);
            try {
				Boolean isMountain = (Boolean) method.invoke(null, biome, BiomeDictionary.Type.MOUNTAIN);
				if (isMountain) mountain[biome.getIdForBiome(biome)] = true;
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			} catch (IllegalArgumentException e) {
				throw new RuntimeException(e);
			} catch (InvocationTargetException e) {
				throw new RuntimeException(e);
			}
        }
        BOPBiomes.alps.asSet().forEach(mountainBiome -> {
        	Biome edge = BOPBiomes.alps_foothills.get();
        	int mountainId = Biome.getIdForBiome(mountainBiome);
            bopMountain[mountainId] = true;
            bopMountainEdges[mountainId] = Biome.getIdForBiome(edge);
        });
        BOPBiomes.mountain.asSet().forEach(mountainBiome -> {
        	Biome edge = BOPBiomes.mountain_foothills.get();
        	int mountainId = Biome.getIdForBiome(mountainBiome);
            bopMountain[mountainId] = true;
            bopMountainEdges[mountainId] = Biome.getIdForBiome(edge);
        });

        BOPBiomes.redwood_forest.asSet().forEach(redwoodForestBiome -> {
        	redwoodForest  = Biome.getIdForBiome(redwoodForestBiome);
        });
        
        BOPBiomes.redwood_forest_edge.asSet().forEach(redwoodForestBiomeEdge -> {
        	redwoodForestEdge  = Biome.getIdForBiome(redwoodForestBiomeEdge);
        });
    }

    /**
     * Returns a list of integer values generated by this layer. These may be interpreted as temperatures, rainfall
     * amounts, or biomeList[] indices based on the particular GenLayer subclass.
     */
    public int[] getInts(int par1, int par2, int par3, int par4)
    {
        int[] aint = this.parent.getInts(par1 - 1, par2 - 1, par3 + 2, par4 + 2);
        int[] aint1 = IntCache.getIntCache(par3 * par4);

        for (int i1 = 0; i1 < par4; ++i1)
        {
            for (int j1 = 0; j1 < par3; ++j1)
            {
                this.initChunkSeed((long)(j1 + par1), (long)(i1 + par2));
                int k1 = aint[j1 + 1 + (i1 + 1) * (par3 + 2)];
                aint1[j1 + (i1) * (par3)] = k1;
                if (bopMountain[k1]) {
                	int k2 = aint[j1 + 2 + (i1 + 1) * (par3 + 2)];
                	if (!mountain[k2]) {
                		aint1[j1 + (i1) * (par3)] = bopMountainEdges[k1];
                		continue;
                	}
                	k2 = aint[j1 + 1 + (i1 + 2) * (par3 + 2)];
                	if (!mountain[k2]) {
                		aint1[j1 + (i1) * (par3)] = bopMountainEdges[k1];
                		continue;
                	}
                	k2 = aint[j1 + (i1 + 1) * (par3 + 2)];
                	if (!mountain[k2]) {
                		aint1[j1 + (i1) * (par3)] = bopMountainEdges[k1];
                		continue;
                	}
                	k2 = aint[j1 + 1 + (i1) * (par3 + 2)];
                	if (!mountain[k2]) {
                		aint1[j1 + (i1) * (par3)] = bopMountainEdges[k1];
                		continue;
                	}
                	// corners needed too
                	k2 = aint[j1 + 2 + (i1 + 2) * (par3 + 2)];
                	if (!mountain[k2]) {
                		aint1[j1 + (i1) * (par3)] = bopMountainEdges[k1];
                		continue;
                	}
                	k2 = aint[j1 + (i1 + 2) * (par3 + 2)];
                	if (!mountain[k2]) {
                		aint1[j1 + (i1) * (par3)] = bopMountainEdges[k1];
                		continue;
                	}
                	k2 = aint[j1 + (i1) * (par3 + 2)];
                	if (!mountain[k2]) {
                		aint1[j1 + (i1) * (par3)] = bopMountainEdges[k1];
                		continue;
                	}
                	k2 = aint[j1 + 2 + (i1) * (par3 + 2)];
                	if (!mountain[k2]) {
                		aint1[j1 + (i1) * (par3)] = bopMountainEdges[k1];
                		continue;
                	}
                	
                }
                if ((redwoodForest < 0)||(redwoodForestEdge< 0)) continue;// either redwood forest or its edge are absent so no edging.
                
                if (k1 == redwoodForest) {
                	int k2 = aint[j1 + 2 + (i1 + 1) * (par3 + 2)];
                	if (k2!=redwoodForest&&k2!=redwoodForestEdge) {
                		aint1[j1 + (i1) * (par3)] = redwoodForestEdge;
                		continue;
                	}
                	k2 = aint[j1 + 1 + (i1 + 2) * (par3 + 2)];
                	if (k2!=redwoodForest&&k2!=redwoodForestEdge) {
                		aint1[j1 + (i1) * (par3)] = redwoodForestEdge;
                		continue;
                	}
                	k2 = aint[j1 + (i1 + 1) * (par3 + 2)];
                	if (k2!=redwoodForest&&k2!=redwoodForestEdge) {
                		aint1[j1 + (i1) * (par3)] = redwoodForestEdge;
                		continue;
                	}
                	k2 = aint[j1 + 1 + (i1) * (par3 + 2)];
                	if (k2!=redwoodForest&&k2!=redwoodForestEdge) {
                		aint1[j1 + (i1) * (par3)] = redwoodForestEdge;
                		continue;
                	}
                	// corners needed too
                	k2 = aint[j1 + 2 + (i1 + 2) * (par3 + 2)];
                	if (k2!=redwoodForest&&k2!=redwoodForestEdge) {
                		aint1[j1 + (i1) * (par3)] = redwoodForestEdge;
                		continue;
                	}
                	k2 = aint[j1 + (i1 + 2) * (par3 + 2)];
                	if (k2!=redwoodForest&&k2!=redwoodForestEdge) {
                		aint1[j1 + (i1) * (par3)] = redwoodForestEdge;
                		continue;
                	}
                	k2 = aint[j1 + (i1) * (par3 + 2)];
                	if (k2!=redwoodForest&&k2!=redwoodForestEdge) {
                		aint1[j1 + (i1) * (par3)] = redwoodForestEdge;
                		continue;
                	}
                	k2 = aint[j1 + 2 + (i1) * (par3 + 2)];
                	if (k2!=redwoodForest&&k2!=redwoodForestEdge) {
                		aint1[j1 + (i1) * (par3)] = redwoodForestEdge;
                		continue;
                	}
                	
                }
            }
        }

        return aint1;
    }
}

