
package climateControl.customGenLayer;
import climateControl.ClimateControl;
import climateControl.genLayerPack.GenLayerPack;
import climateControl.utils.Numbered;
import climateControl.utils.PlaneLocation;
import climateControl.utils.Zeno410Logger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.Logger;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

/**
 * This is the new climate system.
 * @author Zeno410
 *
 * Algorithm: set all the climates according to the user parameters. Then for all the extreme
 * climates change nearby temps to be acceptable. changes are made in an order based on a random derived
 * from the second call to the seed.
 */

public class GenLayerSmoothClimate extends GenLayerPack {
    //public static Logger logger = new Zeno410Logger("GenLayerSmoothClimate").logger();


    public GenLayerSmoothClimate(long par1, GenLayer par3GenLayer) {
        super(par1);
        this.parent = par3GenLayer;
    }

    public int[] getInts(int x0, int z0, int xSize, int zSize){
        // note parent layer is increased by 2 due to the need
        ArrayList<ExtremeTemp> changes = new ArrayList<ExtremeTemp>();
        Prioritizer prioritizer = new Prioritizer();
        int parentX0 = x0 - 2;
        int parentZ0 = z0 - 2;
        int parentXSize = xSize + 4;
        int parentZSize = zSize + 4;
        int[] parentVals = this.parent.getInts(parentX0, parentZ0, parentXSize, parentZSize);
        int[] parentClimates = IntCache.getIntCache(parentXSize*parentZSize);
        for (int parentZ = 0; parentZ < parentZSize; parentZ++){
            for (int parentX = 0; parentX < parentXSize; ++parentX){
                int k2 = parentVals[parentX  + (parentZ) * parentXSize];
                int climate = 0;
                if (isOceanic(k2)) {
                    climate = k2;
                } else {
                    climate = k2%4;
                    if (climate ==0) climate = 4;
                }
                parentClimates[parentX  + (parentZ) * parentXSize] = climate;
            }
        }
        int[] vals = IntCache.getIntCache(xSize * zSize);

        // we cover the entire parental layer, as parental changes can feed back to up to 2 away
        for (int parentZ = 0; parentZ < parentZSize; parentZ++){
            for (int parentX = 0; parentX < parentXSize; ++parentX){


                int k2 = parentClimates[parentX  + (parentZ) * parentXSize];
                setFromParentCoords(k2,parentX,parentZ,xSize,zSize,vals);
                if (k2 > 4) {
                    if ((k2 != 24)&&(k2!=BiomeGenBase.mushroomIsland.biomeID)&&(k2!=BiomeGenBase.frozenOcean.biomeID)) {
                        if (ClimateControl.testing) {
                            ClimateControl.logger.info("GenLayerSmoothClimate "+k2);
                            throw new RuntimeException("GenLayerSmoothClimate "+k2);
                        }
                    }
                }
                this.initChunkSeed((long)(parentX + parentX0), (long)(parentZ + parentZ0));

                if (k2==1){
                    changes.add(new Hot(prioritizer,parentX,parentZ));
                } else if (k2 == 4) {
                    changes.add(new Cold(prioritizer,parentX,parentZ));
                }
            }
        }
        // sort the extreme temps by their priority
        // if priorities identical sort by location
        // a location only search would have some derpy effects
        // but 1 derpiness in a billion is tolerable
        java.util.Collections.sort(changes,Numbered.comparator(PlaneLocation.topLefttoBottomRight()));
        // and adjust climates incompatible with that extremity
        parentClimates = null;

        for (ExtremeTemp temp: changes) {
            temp.adjust(parentClimates,xSize, zSize, vals);
        }
        return vals;
    }

    abstract private class ExtremeTemp extends Numbered<PlaneLocation> {
        // the numbers are the priority
        ExtremeTemp(Prioritizer prioritizer, int x, int z) {
            super(prioritizer.next(),new PlaneLocation(x,z));
        }
        final int x() {return item().x();}
        final int z() {return item().z();}
        abstract void adjust(int [] parentVals, int xSize, int zSize, int [] vals);
    }

    private class Hot extends ExtremeTemp {
        Hot(Prioritizer prioritizer,int x, int z) {
            super(prioritizer,x,z);
        }
        void adjust(int [] parentVals, int xSize, int zSize, int [] vals) {

            // seems logical to skip if already eliminated but that sets up possible
            // side effects that can travel an infinite distance
            // instead we put it back. It can be erased again later.
            setFromParentCoords(1,x(),z(),xSize,zSize,vals);

            warmerThanCool(x()-1,z(),xSize,zSize,vals);
            warmerThanCool(x(),z()-1,xSize,zSize,vals);
            warmerThanCool(x()+1,z(),xSize,zSize,vals);
            warmerThanCool(x(),z()+1,xSize,zSize,vals);

            warmerThanSnowy(x()-2,z(),xSize,zSize,vals);
            warmerThanSnowy(x()-1,z()-1,xSize,zSize,vals);
            warmerThanSnowy(x(),z()-2,xSize,zSize,vals);
            warmerThanSnowy(x()+1,z()-1,xSize,zSize,vals);
            warmerThanSnowy(x()+2,z(),xSize,zSize,vals);
            warmerThanSnowy(x()+1,z()+1,xSize,zSize,vals);
            warmerThanSnowy(x(),z()+2,xSize,zSize,vals);
            warmerThanSnowy(x()-1,z()+1,xSize,zSize,vals);

        }
    }

    private class Cold extends ExtremeTemp {
        Cold(Prioritizer prioritizer,int x, int z) {
            super(prioritizer,x,z);
        }
        void adjust(int [] parentVals,int xSize, int zSize, int [] vals) {

            // seems logical to skip if already eliminated but that sets up possible
            // side effects that can travel an infinite distance
            // instead we put it back. It can be erased again later.
            setFromParentCoords(4,x(),z(),xSize,zSize,vals);

            coolerThanWarm(x()-1,z(),xSize,zSize,vals);
            coolerThanWarm(x(),z()-1,xSize,zSize,vals);
            coolerThanWarm(x()+1,z(),xSize,zSize,vals);
            coolerThanWarm(x(),z()+1,xSize,zSize,vals);

            coolerThanHot(x()-2,z(),xSize,zSize,vals);
            coolerThanHot(x()-1,z()-1,xSize,zSize,vals);
            coolerThanHot(x(),z()-2,xSize,zSize,vals);
            coolerThanHot(x()+1,z()-1,xSize,zSize,vals);
            coolerThanHot(x()+2,z(),xSize,zSize,vals);
            coolerThanHot(x()+1,z()+1,xSize,zSize,vals);
            coolerThanHot(x(),z()+2,xSize,zSize,vals);
            coolerThanHot(x()-1,z()+1,xSize,zSize,vals);
        }
    }

    private class Prioritizer {
        // I thought I needed unique numbers but realized I didn't
        // kept it for type safety
        HashSet<Integer> numbers = new HashSet<Integer>();
        boolean duplicate = false;
        int next() {
            int number = nextInt(1000000000);
            // skip one because the first is correlated to the climate
            number = nextInt(1000000000);
            if (!numbers.contains(number)) {
                numbers.add(number);
            } else duplicate =true;
            return number;
        }
    }


    // Note all the changing calls use *parent* coordinates and the *child* array size
    private final void setFromParentCoords(int value, int px, int pz, int xSize, int zSize, int[] vals) {
        if ((px<2)||(pz<2)||(px-1>xSize)||(pz-1)>zSize)return;
        vals[px-2+(pz-2)*xSize] = value;
    }

    private final void coolerThanHot(int px, int pz, int xSize, int zSize, int[] vals) {
        if ((px<2)||(pz<2)||(px-1>xSize)||(pz-1)>zSize)return;
        int value = vals[px-2+(pz-2)*xSize];
        if (value == 1) vals[px-2+(pz-2)*xSize] = 2;
    }

    private final void warmerThanSnowy(int px, int pz, int xSize, int zSize, int[] vals) {
        if ((px<2)||(pz<2)||(px-1>xSize)||(pz-1)>zSize)return;
        int value = vals[px-2+(pz-2)*xSize];
        if (value == 4) vals[px-2+(pz-2)*xSize] = 3;
    }

    private final void coolerThanWarm(int px, int pz, int xSize, int zSize, int[] vals) {
        if ((px<2)||(pz<2)||(px-1>xSize)||(pz-1)>zSize)return;
        int value = vals[px-2+(pz-2)*xSize];
        if (value == 1) vals[px-2+(pz-2)*xSize] = 3;
        if (value == 2) vals[px-2+(pz-2)*xSize] = 3;
    }

    private final void warmerThanCool(int px, int pz, int xSize, int zSize, int[] vals) {
        if ((px<2)||(pz<2)||(px-1>xSize)||(pz-1)>zSize)return;
        int value = vals[px-2+(pz-2)*xSize];
        if (value == 4) vals[px-2+(pz-2)*xSize] = 2;
        if (value == 3) vals[px-2+(pz-2)*xSize] = 2;
    }

}