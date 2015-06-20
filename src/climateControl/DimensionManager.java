
package climateControl;

import Zeno410Utils.Accessor;
import climateControl.api.BiomeSettings;
import climateControl.api.ClimateControlSettings;
import climateControl.customGenLayer.GenLayerLowlandRiverMix;
import climateControl.customGenLayer.GenLayerRiverMixWrapper;
import climateControl.genLayerPack.GenLayerPack;
import climateControl.generator.CorrectedContinentsGenerator;
import climateControl.generator.OneSixCompatibleGenerator;
import climateControl.generator.StackedContinentsGenerator;
import climateControl.generator.TestGeneratorPair;
import climateControl.generator.VanillaCompatibleGenerator;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerRiverMix;

/**
 *
 * @author Zeno410
 */
public class DimensionManager {
        private Accessor<GenLayerRiverMix,GenLayerPack> riverMixBiome =
            new Accessor<GenLayerRiverMix,GenLayerPack>("field_75910_b");

    private Accessor<WorldChunkManager,GenLayer> worldGenLayer =
            new Accessor<WorldChunkManager,GenLayer>("field_76944_d");

    private Accessor<WorldChunkManager,GenLayer> biomeIndexLayer =
            new Accessor<WorldChunkManager,GenLayer>("field_76945_e;");

    public void patchGenLayer(ClimateControlSettings newSettings, 
            WorldType worldType,
            GenLayerRiverMixWrapper riverLayerWrapper,
            long worldSeed) {

        for (BiomeSettings biomeSettings: newSettings.biomeSettings()) {
            //biomeSettings.report();
        }
        if (ignore(worldType)) return;
        if (newSettings.noGenerationChanges.value()) {
            if (newSettings.oneSixCompatibility.value()) {
                riverLayerWrapper.setRedirection(
                        ((GenLayerRiverMix)(new OneSixCompatibleGenerator(newSettings).fromSeed(worldSeed))));
            } else {
                riverLayerWrapper.useOriginal();
            }
            return;
        }
        GenLayerRiverMix newMix = null;
        //logger.info("world seed " + worldSeed);
        if (newSettings.vanillaLandAndClimate.value()) {
             newMix = new VanillaCompatibleGenerator(newSettings).fromSeed(worldSeed);
             riverLayerWrapper.setRedirection(newMix);
        } else {
            if (newSettings.zeroPointFive.value()) {
                newMix = new CorrectedContinentsGenerator(newSettings).fromSeed(worldSeed);
                riverLayerWrapper.setRedirection(newMix);
            } else {
                StackedContinentsGenerator generator = new StackedContinentsGenerator(newSettings);
                newMix = generator.fromSeed(worldSeed);
            riverLayerWrapper.setRedirection(newMix);
            }
        }
        GenLayer oldGen = null;//riverMixBiome.get(activeRiverMix);
        GenLayer newGen = riverMixBiome.get(newMix);
        TestGeneratorPair pair = new TestGeneratorPair(oldGen,newGen);
        while (true) {
            //logger.info(pair.description());
            if (!pair.hasNext()) break;
            pair = pair.next();
        }
        //logger.info("newMix "+newMix.toString());
        if (newMix instanceof GenLayerLowlandRiverMix) {
            ((GenLayerLowlandRiverMix)newMix).setMaxChasm(newSettings.maxRiverChasm.value().floatValue());
        }
        for (BiomeSettings biomeSettings: newSettings.biomeSettings()) {
            //biomeSettings.report();
        }
        BiomeRandomizer.instance.set(newSettings.biomeSettings());
    }

    private ClimateControlSettings newSettings() {
        return null;
    }

    public boolean ignore(WorldType considered) {
        if (considered == null) return true;
        if (considered.equals(WorldType.AMPLIFIED)) return false;
        if (considered.equals(WorldType.DEFAULT)) {
            return false;
        }
        if (considered.equals(WorldType.DEFAULT_1_1)) return false;
        if (considered.equals(WorldType.LARGE_BIOMES)) return false;
        if (considered.equals(WorldType.FLAT)) return true;
        if (this.newSettings().interveneInBOPWorlds.value()) {
            if (considered.getWorldTypeName().equalsIgnoreCase("BIOMESOP")) return false;
        }
        if (this.newSettings().interveneInHighlandsWorlds.value()) {

            if (considered.getWorldTypeName().equalsIgnoreCase("Highlands")) return false;
            if (considered.getWorldTypeName().equalsIgnoreCase("HighlandsLB")) return false;
        }
        return true;
    }
}
