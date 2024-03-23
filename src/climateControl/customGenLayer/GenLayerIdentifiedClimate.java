package climateControl.customGenLayer;

import climateControl.ClimateControl;
import climateControl.api.ClimateControlSettings;
import climateControl.genLayerPack.GenLayerPack;
import com.Zeno410Utils.Numbered;
import com.Zeno410Utils.PlaneLocation;
import com.Zeno410Utils.Zeno410Logger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.Logger;
import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.layer.GenLayer;

/**
 * This class generates coded climates, combining the climate with 4* the previous ID
 * The point is to allow the separation systems to continue working
 * 
 *
 * @author Zeno410
 */
public class GenLayerIdentifiedClimate extends GenLayerPack {

    private final int hotLevel;
    private final int warmLevel;
    private final int coldLevel;
    private final int totalLevel;
    public static Logger logger = new Zeno410Logger("IdentifiedClimate").logger();

    private GenLayerIdentifiedClimate(long par1, GenLayer par3GenLayer,int hot, int warm, int cold, int snow)
    {
        super(par1);
        this.parent = par3GenLayer;
        this.hotLevel = hot;
        this.warmLevel = hot + warm;
        this.coldLevel = hot + warm + cold;
        this.totalLevel = hot + warm + cold + snow;
    }

    public GenLayerIdentifiedClimate(long seed, GenLayer genLayer,ClimateControlSettings settings){
        this(seed,
                genLayer,
                settings.hotIncidence.value(),
                settings.warmIncidence.value(),
                settings.coolIncidence.value(),
                settings.snowyIncidence.value()) ;
    }

    public int[] getInts(int x0, int z0, int xSize, int zSize){
        // note parent layer is increased by 2 due to the need
        //ArrayList<ExtremeTemp> changes = new ArrayList<ExtremeTemp>();
        Prioritizer prioritizer = new Prioritizer();
        int parentX0 = x0 - 2;
        int parentZ0 = z0 - 2;
        int parentXSize = xSize + 4;
        int parentZSize = zSize + 4;
        int[] parentVals ;
        int[] parentIds = this.parent.getInts(parentX0, parentZ0, parentXSize, parentZSize);
        parentVals = this.getRawClimates(parentIds, parentX0, parentZ0, parentXSize, parentZSize);
        int[] vals = new int[xSize * zSize];

        // we cover the entire parental layer, as parental changes can feed back to up to 2 away
        for (int parentZ = 0; parentZ < parentZSize; parentZ++){
            for (int parentX = 0; parentX < parentXSize; ++parentX){


                int k2 = parentVals[parentX  + (parentZ) * parentXSize];
                setFromParentCoords(k2,parentX,parentZ,xSize,zSize,vals);
                if (k2 > 4) {
                    if ((k2 != 24)&&(k2!=Biome.getIdForBiome(Biomes.MUSHROOM_ISLAND))) {
                        if (ClimateControl.testing) {
                            ClimateControl.logger.info("GenLayerSmoothClimate "+k2);
                            throw new RuntimeException("GenLayerSmoothClimate "+k2);
                        }
                    }
                }
                this.initChunkSeed((long)(parentX + parentX0), (long)(parentZ + parentZ0));

                if (k2==1){
                    //changes.add(new Hot(prioritizer,parentX,parentZ));
                } else if (k2 == 4) {
                    //changes.add(new Cold(prioritizer,parentX,parentZ));
                }
            }
        }

        parentVals = null;

        /* climate smoothing has been moved to the GenLayerSmoothClimate routine
        // sort the extreme temps by their priority
        // if priorities identical sort by location
        // a location only search would have some derpy effects
        // but 1 derpiness in a billion is tolerable
        // and adjust climates incompatible with that extremity

        //java.util.Collections.sort(changes,Numbered.comparator(PlaneLocation.topLefttoBottomRight()));


        for (ExtremeTemp temp: changes) {
            logger.info(""+temp.toString());
            temp.adjust(parentVals,xSize, zSize, vals);
        }
         */
        // now we add on 4*identifier for original values above 255. These are landmass IDs.
        for (int parentZ = 2; parentZ < parentZSize-2; parentZ++){
            for (int parentX = 2; parentX < parentXSize-2; parentX++){
                int k2 = parentIds[parentX  + (parentZ) * parentXSize];
                if (k2<256) continue;
                vals[parentX-2  + (parentZ-2) * xSize] += k2*4;
            }
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

        public String toString() {return "Hot  "+x()+","+z();}

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

        public String toString() {return "Cold "+x()+","+z();}

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
            } else {
                duplicate =true;
                throw new RuntimeException();
            }
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


    private int[] getRawClimates(int [] parent,int par1, int par2, int par3, int par4) {
        int[] aint1 = new int[(par3 * par4)];

        for (int i2 = 0; i2 < par4; ++i2)
        {
            for (int j2 = 0; j2 < par3; ++j2)
            {
                int k2 = parent[j2 + (i2) * par3];
                this.initChunkSeed((long)(j2 + par1), (long)(i2 + par2));

                if (k2 == 0)
                {
                    aint1[j2 + i2 * par3] = 0;
                }
                else
                {
                    int l2 = this.nextInt(totalLevel);
                    byte b0;

                    if (l2 < hotLevel)
                    {
                        b0 = 1;
                    }
                    else if (l2 < warmLevel)
                    {
                        b0 = 2;
                    }
                    else if (l2 < coldLevel) {
                        b0 = 3;
                    } else {
                        b0 = 4;
                    }

                    aint1[j2 + i2 * par3] = b0;
                }
            }
        }
        for (int i = 0; i < par3 * par4;i++) {
            if (aint1[i]>255) throw new RuntimeException();
        }
        return aint1;
    }
}