
package climateControl.generator;

import climateControl.api.DistributionPartitioner;
import climateControl.api.IncidenceModifier;
import climateControl.customGenLayer.GenLayerConstant;
import climateControl.customGenLayer.GenLayerLandReport;
import climateControl.customGenLayer.GenLayerLimitedCache;
import climateControl.customGenLayer.GenLayerMountainChains;
import climateControl.genLayerPack.GenLayerRiverInit;
import climateControl.genLayerPack.GenLayerSmooth;
import climateControl.genLayerPack.GenLayerZoom;
import com.Zeno410Utils.Numbered;
import com.Zeno410Utils.StringWriter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraftforge.common.BiomeDictionary;

/**
 *
 * @author Zeno410
 */
public class MountainFormer extends DistributionPartitioner {

    private GenLayer mountainGens;
    private final boolean mesaMountains; // the variable is not used but recorded for debugging

    public MountainFormer(boolean mesaMountains) {
        super(incidenceModifiers(mesaMountains));
        this.mesaMountains = mesaMountains;
        mountainGens = mountainGenLayers();
    }

    public void initWorldGenSeed(long par1) {
        mountainGens.initWorldGenSeed(par1);
    }
    
    @Override
    protected IncidenceModifier modifier(int x, int z) {
        int index = mountainGens.getInts(x, z, 1, 1)[0];
        return super.modifiers.get(index);
    }

    private GenLayer mountainGenLayers() {
        GenLayer result = new GenLayerConstant(1,1);
        result = new GenLayerRiverInit(3001L,result);
        for (int i = 0 ; i < 3; i++) {
            result = new GenLayerZoom(3001L+i,result);
            result = new GenLayerSmooth(3001L+i,result);
        }
        result = new GenLayerMountainChains(3005L,result);
        result = new GenLayerLimitedCache(result,64);

        result = reportOn(result, "mountains.txt");
        return result;
    }

    GenLayer reportOn(GenLayer reportedOn, String fileName) {
        if (true) {
            try {
                StringWriter target = new StringWriter(new File(fileName));
                reportedOn = new GenLayerLandReport(reportedOn,40,target);
                return reportedOn;
            } catch (IOException iOException) {
                throw new RuntimeException(iOException);
            }
        }
        return reportedOn;
    }
    private static ArrayList<IncidenceModifier> incidenceModifiers(boolean mesaMountains) {
        ArrayList<IncidenceModifier> result = new ArrayList<IncidenceModifier>();
        result.add(new LowlandModifier(mesaMountains));
        result.add(new MountainModifier(mesaMountains));
        return result;
    }

    //This grim hack is required because forge changed a name
     public static Method getBiomeTypeMethod() {
        // hunts through the class object and all superclasses looking for the field name
            Method method = null;
            {try {
                method = BiomeDictionary.class.getMethod("isBiomeOfType", Biome.class, BiomeDictionary.Type.class);
            } catch (NoSuchMethodException noSuchMethodException) {
                try {
                    method = BiomeDictionary.class.getMethod("hasType", Biome.class, BiomeDictionary.Type.class);
                } catch (NoSuchMethodException ex) {
                    Logger.getLogger(MountainFormer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SecurityException ex) {
                    Logger.getLogger(MountainFormer.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (SecurityException securityException) {
            }
        }
            return method;
    }

    private static class MountainModifier implements IncidenceModifier {

        Method method;
        private final boolean mesaMountains;
        MountainModifier(boolean mesaMountains) {
            method = getBiomeTypeMethod();
            this.mesaMountains = mesaMountains;
        }

        public int modifiedIncidence(Numbered<Biome> biomeIncidence) {
            try {
                Biome biome = biomeIncidence.item();
                // increase mountains;
                Boolean isMountain = (Boolean) method.invoke(null, biome, BiomeDictionary.Type.MOUNTAIN);
                if (isMountain) {
                    //if (BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.MOUNTAIN)) {
                    // multiply by 4
                    return biomeIncidence.count() * 4;
                }
                // check that extreme hills are a mountain biome
                if (biomeIncidence.item() == Biomes.EXTREME_HILLS) {
                    return biomeIncidence.count() * 4;
                }
                if (mesaMountains) {
                    if (biomeIncidence.item() == Biomes.MESA_CLEAR_ROCK||biomeIncidence.item() == Biomes.MESA_ROCK) {
                        return biomeIncidence.count() * 4;
                    }
                }
                // Hills unaffected
                Boolean isHill = (Boolean) method.invoke(null, biome, BiomeDictionary.Type.HILLS);
                if (isHill) {
                    return biomeIncidence.count();
                }
                // Oceans unaffected
                Boolean isOcean = (Boolean) method.invoke(null, biome, BiomeDictionary.Type.OCEAN);
                if (isOcean) {
                    // multiply by 4
                    return biomeIncidence.count();
                }
                // everything else suppressed;
                return 0;
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            } catch (IllegalArgumentException ex) {
                throw new RuntimeException(ex);
            } catch (InvocationTargetException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private static class LowlandModifier implements IncidenceModifier {
        private final boolean mesaMountains;
        Method method;
        LowlandModifier(boolean mesaMountains) {
            method = getBiomeTypeMethod();
            this.mesaMountains = mesaMountains;
        }

        public int modifiedIncidence(Numbered<Biome> biomeIncidence) {
            try {
                Biome biome = biomeIncidence.item();
                // erase mountains;
                Boolean isMountain = (Boolean) method.invoke(null, biome, BiomeDictionary.Type.MOUNTAIN);
                if (isMountain) {
                    //if (BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.MOUNTAIN)) {
                    // multiply by 4
                    return 0;
                }
                // check that extreme hills are a mountain biome
                if (biomeIncidence.item() == Biomes.EXTREME_HILLS) {
                    return 0;
                }
                if (mesaMountains) {
                    if (biomeIncidence.item() == Biomes.MESA_CLEAR_ROCK||biomeIncidence.item() == Biomes.MESA_ROCK) {
                        return 0;
                    }
                }
                // Hills unaffected
                Boolean isHill = (Boolean) method.invoke(null, biome, BiomeDictionary.Type.HILLS);
                if (isHill) {
                    // multiply by 4
                    return biomeIncidence.count();
                }
                // Oceans unaffected
                Boolean isOcean = (Boolean) method.invoke(null, biome, BiomeDictionary.Type.OCEAN);
                if (isOcean) {
                    // multiply by 4
                    return biomeIncidence.count();
                }
                // everything else increased slightly;
                return (biomeIncidence.count()*4)/3;
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            } catch (IllegalArgumentException ex) {
                throw new RuntimeException(ex);
            } catch (InvocationTargetException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

}
