package climateControl.customGenLayer;
import climateControl.api.IslandClimateMaker;
import climateControl.utils.RandomIntUser;
import climateControl.utils.IntRandomizer;
import climateControl.genLayerPack.GenLayerPack;
import climateControl.utils.IntPad;
import climateControl.utils.Numbered;
import climateControl.utils.PlaneLocation;
import climateControl.utils.Zeno410Logger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.logging.Logger;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.layer.GenLayer;
/**
 *
 * @author Zeno410
 */
public class GenLayerOceanicIslands extends GenLayerPack {

    public static final Logger logger = new Zeno410Logger("OceanicIslands").logger();
    private final IntRandomizer passer = new IntRandomizer() {
        public int nextInt(int range) {
            return GenLayerOceanicIslands.this.nextInt(range);
        }
    };
    private final boolean suppressDiagonals;

    private final int milleFill;
    private final IslandClimateMaker island;
    private final String layerName;

    private final IntPad output = new IntPad();
    private final IntPad input = new IntPad();

    private GenLayerOceanicIslands(long p_i45480_1_, GenLayer p_i45480_3_, int milleFill, final int islandValue,
            boolean suppressDiagonals,String layerName){
        super(p_i45480_1_);
        this.parent = p_i45480_3_;
        this.milleFill = milleFill;
        // using a constant island value so create a RandomIntUser that ignores the random and returns the value
        this.island = new IslandClimateMaker() {
            public int climate(int x, int z, IntRandomizer randomizer) {
                return islandValue;
            }
        };
        this.suppressDiagonals = suppressDiagonals;
        this.layerName = layerName;
    }

    public GenLayerOceanicIslands(long p_i45480_1_, GenLayer p_i45480_3_, int milleFill,
                final RandomIntUser island,boolean suppressDiagonals, String layerName){
        super(p_i45480_1_);
        this.parent = p_i45480_3_;
        this.milleFill = milleFill;
        this.island = new IslandClimateMaker() {
            public int climate(int x, int z, IntRandomizer randomizer) {
                return island.value(randomizer);
            };
        };
        this.suppressDiagonals = suppressDiagonals;
        this.layerName = layerName;
    }

    public GenLayerOceanicIslands(long p_i45480_1_, GenLayer p_i45480_3_, int milleFill,
                IslandClimateMaker island,boolean suppressDiagonals, String layerName){
        super(p_i45480_1_);
        this.parent = p_i45480_3_;
        this.milleFill = milleFill;
        this.island = island;
        this.suppressDiagonals = suppressDiagonals;
        this.layerName = layerName;
    }

    /**
     * Every pixel of "water" (value 0) has percentFilled chance of being made land
     * amounts, or biomeList[] indices based on the particular GenLayer subclass.
     */
    public synchronized int[] getInts(int par1, int par2, int par3, int par4) {
        if (suppressDiagonals) {
            return this.getSeparatedIslands(par1, par2, par3, par4);
        }
        int i1 = par1 - 1;
        int j1 = par2 - 1;
        int k1 = par3 + 2;
        int l1 = par4 + 2;
        int[] aint = this.parent.getInts(i1, j1, k1, l1);
        int[] aint1 = output.pad(par3 * par4);

        for (int i2 = 0; i2 < par4; ++i2){
            for (int j2 = 0; j2 < par3; ++j2){
                int k2 = aint[j2 + 1 + (i2 + 1 - 1) * (par3 + 2)];
                int l2 = aint[j2 + 1 + 1 + (i2 + 1) * (par3 + 2)];
                int i3 = aint[j2 + 1 - 1 + (i2 + 1) * (par3 + 2)];
                int j3 = aint[j2 + 1 + (i2 + 1 + 1) * (par3 + 2)];
                int k3 = aint[j2 + 1 + (i2 + 1) * k1];
                // set default value
                aint1[j2 + i2 * par3] = k3;
                // apply test
                if (isOceanic(k3) && isOceanic(k2) && isOceanic(l2) && isOceanic(i3) && isOceanic(j3) ){
                    if (suppressDiagonals) {
                        int upperLeft = aint[j2 + (i2) * (par3 + 2)];
                        int upperRight = aint[j2 + 2 + (i2) * (par3 + 2)];
                        int lowerLeft = aint[j2 + (i2+2) * (par3 + 2)];
                        int lowerRight = aint[j2+2 + (i2+2) * (par3 + 2)];
                        if ( !isOceanic(upperLeft) || !isOceanic(upperRight) || !isOceanic(lowerLeft) || !isOceanic(lowerRight)) {
                            continue;
                        }
                    } 
                    this.initChunkSeed((long)(j2 + par1), (long)(i2 + par2));
                    if (this.nextInt(1000) < milleFill) {
                        aint1[j2 + i2 * par3] = island.climate(j2 + par1,i2 + par2,passer);
                    }
                }
            }
        }
        return aint1;
    }

    public synchronized int[] getSeparatedIslands(int par1, int par2, int par3, int par4) {
        ArrayList<Numbered<LocatedInt>> newIslands = new ArrayList<Numbered<LocatedInt>>();
        int i1 = par1 - 2;
        int j1 = par2 - 2;
        int k1 = par3 + 4;
        int l1 = par4 + 4;
        int[] aint = this.parent.getInts(i1, j1, k1, l1);
        int [] parent = input.pad(k1*l1);
        for (int i = 0; i < parent.length; i ++) {
            parent [i] = aint [i];
        }
        taste(parent,k1*l1);

        for (int i2 = 1; i2 < par4+3; ++i2){
            for (int j2 = 1; j2 < par3+3; ++j2){
                int k2 = parent[j2 + (i2  - 1) * k1];
                int l2 = parent[j2 + 1 + (i2 ) * k1];
                int i3 = parent[j2  - 1 + (i2 ) * k1];
                int j3 = parent[j2  + (i2  + 1) * k1];
                int k3 = parent[j2  + (i2 ) * k1];
                // apply test
                if (isOceanic(k3) && isOceanic(k2) && isOceanic(l2) && isOceanic(i3) && isOceanic(j3) ){
                    if (suppressDiagonals) {
                        int upperLeft = parent[j2 -1 + (i2-1) * (par3 + 4)];
                        int upperRight = parent[j2 +1 + (i2-1) * (par3 + 4)];
                        int lowerLeft = parent[j2 -1+ (i2+1) * (par3 + 4)];
                        int lowerRight = parent[j2+1 + (i2+1) * (par3 + 4)];
                        if (!isOceanic(upperLeft) || !isOceanic(upperRight) || !isOceanic(lowerLeft) || !isOceanic(lowerRight)) {
                            continue;
                        }
                    }
                    this.initChunkSeed((long)(j2 + i1), (long)(i2 + j1));
                    if (this.nextInt(1000) < milleFill) {
                        LocatedInt toAdd = new LocatedInt(new PlaneLocation(j2,i2),
                                island.climate(j2 + i1,i2 + j1,passer));
                        newIslands.add(new Numbered<LocatedInt>(passer.nextInt(Integer.MAX_VALUE),toAdd));
                    }
                }
            }
        }

        // sort and make changes, zeroing out adjacent land.
        // parent land suppresses adjacent islands so it don't get lost
        Collections.sort(newIslands, Numbered.comparator(locatedIntComparator()));
        for (Numbered<LocatedInt> attempt : newIslands) {
            int j2 = attempt.item().location.x();
            int i2 = attempt.item().location.z();
            int target = parent[j2  + (i2 ) * k1];
            if (!isOceanic(target)) throw new RuntimeException("unexpected land area during generation");
            forceOcean(parent,j2-1,i2,k1,target);
            forceOcean(parent,j2,i2-1,k1,target);
            forceOcean(parent,j2+1,i2,k1,target);
            forceOcean(parent,j2,i2+1,k1,target);
            parent[j2 + (i2) * k1] = attempt.item().climate;
            if (suppressDiagonals) {
                forceOcean(parent,j2-1,i2-1,k1,target);
                forceOcean(parent,j2+1,i2-1,k1,target);
                forceOcean(parent,j2+1,i2+1,k1,target);
                forceOcean(parent,j2-1,i2+1,k1,target);
            }
        }
        // now copy the values
        int[] aint1 = output.pad(par3 * par4);
        poison(aint1,par3*par4);
        for (int i2 = 2; i2 < par4+2; i2++){
            for (int j2 = 2; j2 < par3+2; j2++){
                int k3 = parent[j2  + (i2) * k1];
                // copy
                aint1[j2-2  + (i2-2) * par3] = k3;
                if (k3>0){
                    //logger.info(layerName+" " + (j2-2+par1)+ ","+(i2-2+par2));
                }

            }
        }
        taste(aint1,par3*par4);
        return aint1;
    }

    private void forceOcean(int[] array, int x, int z, int xSize, int ocean) {
        if (isOceanic(array[x + z*xSize])) return;
        // convert deep ocean to ocean;
        if (ocean == BiomeGenBase.deepOcean.biomeID) ocean = 0;
        array[x + z*xSize] = ocean;
    }

    private static Comparator<LocatedInt> locatedIntComparator() {
        return new Comparator<LocatedInt>() {

            public int compare(LocatedInt arg0, LocatedInt arg1) {
                if (arg0.location.x() != arg1.location.x()) {
                    return arg0.location.x() - arg1.location.x();
                }
                return arg0.location.z() - arg1.location.z();
            }

        };
    }

    private class LocatedInt {
        final PlaneLocation location;
        final int climate;
        LocatedInt(PlaneLocation location,int climate){
            this.location = location;
            this.climate = climate;
        }
    }
}