
package climateControl.customGenLayer;

import climateControl.api.IslandClimateMaker;
import climateControl.genLayerPack.GenLayerPack;
import climateControl.utils.IntRandomizer;
import net.minecraft.world.gen.layer.GenLayer;

/**
 *
 * @author Zeno410
 */
public class GenLayerForceStartLand extends GenLayerPack {

    private final IslandClimateMaker climateMaker;
    
    private final IntRandomizer randomizer = new IntRandomizer() {

        @Override
        public int nextInt(int range) {
            return GenLayerForceStartLand.this.nextInt(range);
        }

    };
    public GenLayerForceStartLand(GenLayer parent, IslandClimateMaker climateMaker) {
        super(0L);
        this.parent = parent;
        this.climateMaker = climateMaker;
    }



    @Override
    public int [] getInts(int x0, int z0, int xSize, int zSize) {
        //logger.info("location " + x0 + " " + z0 + " " + xSize + " " + zSize);
        // if region doesn't include 0,0 return parent
        if (x0>0||z0>0||(x0+xSize<0)||(z0+zSize<0)) {
            return parent.getInts(x0, z0, xSize, zSize);
        }
        int[] changed = parent.getInts(x0-1, z0-1, xSize+2, zSize+2);
        for (int x=1; x<xSize + 1;x++) {
            for (int z = 1; z<zSize + 1;z++) {
                if (x+x0-1 ==0&& z+z0-1 == 0) {
                    // test for adjacent land, skipping if any

                    if (changed[(z-1)*(xSize+2)+x-1] > 0) continue;
                    if (changed[(z-1)*(xSize+2)+x] > 0) continue;
                    if (changed[(z-1)*(xSize+2)+x+1] > 0) continue;
                    if (changed[(z)*(xSize+2)+x-1] > 0) continue;
                    if (changed[(z)*(xSize+2)+x] > 0) continue;
                    if (changed[(z)*(xSize+2)+x+1] > 0) continue;
                    if (changed[(z+1)*(xSize+2)+x-1] > 0) continue;
                    if (changed[(z+1)*(xSize+2)+x] > 0) continue;
                    if (changed[(z+1)*(xSize+2)+x+1] > 0) continue;
                    //force land

                    super.initChunkSeed(0,0);
                    changed[(z)*(xSize+2)+x] = climateMaker.climate(x+x0-1, z+z0-1, randomizer);
                }
            }
        }
        int [] result  = new int [xSize*zSize];
        for (int x=0; x<xSize;x++) {
            for (int z = 0; z<zSize;z++) {
                result[(z)*(xSize)+x] = changed[(z+1)*(xSize + 2)+x+1];
            }
        }
        return result;
    }

}
