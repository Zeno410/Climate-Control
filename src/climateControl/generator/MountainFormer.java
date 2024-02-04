
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
import climateControl.utils.Numbered;
import climateControl.utils.StringWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import net.minecraft.world.biome.BiomeGenBase;
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

        //result = reportOn(result, "mountains.txt");
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

    private static class MountainModifier implements IncidenceModifier {
        private final boolean mesaMountains;
        MountainModifier(boolean mesaMountains) {
            this.mesaMountains = mesaMountains;
        }

        public int modifiedIncidence(Numbered<BiomeGenBase> biomeIncidence) {
            BiomeGenBase biome = biomeIncidence.item();
            // increase mountains;
            if (BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.MOUNTAIN)) {
                // multiply by 4
                return biomeIncidence.count()*4;
            }
            // check that extreme hills are a mountain biome
            if (biomeIncidence.item().biomeID == BiomeGenBase.extremeHills.biomeID) {
                return biomeIncidence.count()*4;
            }
            if (mesaMountains) {
                if (biomeIncidence.item().equals(BiomeGenBase.mesaPlateau)||biomeIncidence.item().equals(BiomeGenBase.mesaPlateau_F)) {
                        return biomeIncidence.count() * 4;
                }
            }
            // Hills unaffected
            if (BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.HILLS)) {
                // multiply by 4
                return biomeIncidence.count();
            }
            // Oceans unaffected
            if (BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.OCEAN)) {
                // multiply by 4
                return biomeIncidence.count();
            }
            // everything else suppressed;
            return 0;
        }
    }

    private static class LowlandModifier implements IncidenceModifier {
        private final boolean mesaMountains;
        LowlandModifier(boolean mesaMountains) {
            this.mesaMountains = mesaMountains;
        }
        public int modifiedIncidence(Numbered<BiomeGenBase> biomeIncidence) {
            BiomeGenBase biome = biomeIncidence.item();
            // erase mountains;
            if (BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.MOUNTAIN)) {
                 return 0;
            }
            // check that extreme hills are a mountain biome
            if (biomeIncidence.item().biomeID == BiomeGenBase.extremeHills.biomeID) {
                return 0;
            }
            if (mesaMountains) {
                if (biomeIncidence.item().equals(BiomeGenBase.mesaPlateau)||biomeIncidence.item().equals(BiomeGenBase.mesaPlateau_F)) {
                    return 0;
                }
            }
            // Hills unaffected
            if (BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.HILLS)) {
                return biomeIncidence.count();
            }
            // Oceans unaffected
            if (BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.OCEAN)) {
                return biomeIncidence.count();
            }
            // everything else increased slightly;
            return (biomeIncidence.count()*4)/3;
        }
    }

}
