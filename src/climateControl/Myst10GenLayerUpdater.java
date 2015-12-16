
package climateControl;

import climateControl.customGenLayer.GenLayerRiverMixWrapper;
import climateControl.utils.Accessor;
import com.xcompwiz.mystcraft.api.world.logic.IBiomeController;
import com.xcompwiz.mystcraft.symbol.symbols.SymbolBiomeControllerNative;
import com.xcompwiz.mystcraft.world.AgeController;
import net.minecraft.world.WorldProvider;

import com.xcompwiz.mystcraft.world.WorldProviderMyst;
import java.util.List;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.gen.layer.GenLayer;

/**
 *
 * @author Zeno410
 */
public class Myst10GenLayerUpdater {

    private Accessor<WorldProviderMyst,AgeController> ageControllerAccess =
            new Accessor<WorldProviderMyst,AgeController>("controller");

    private Accessor<AgeController,IBiomeController> biomeControllerAccess =
            new Accessor<AgeController,IBiomeController>("biomeController");

    private Accessor<SymbolBiomeControllerNative,GenLayer> genLayerAccess=
            new Accessor<SymbolBiomeControllerNative,GenLayer>("genBiomes");

    private Accessor<SymbolBiomeControllerNative,GenLayer> biomeIndexAccess=
            new Accessor<SymbolBiomeControllerNative,GenLayer>("biomeIndexLayer");

    public void update(GenLayerRiverMixWrapper riverMix, WorldProvider provider) {
        DimensionManager.logger.info(" provider "+provider.toString());
        if (provider instanceof WorldProviderMyst) {
            WorldProviderMyst mystProvider = (WorldProviderMyst)provider;
            AgeController controller = ageControllerAccess.get(mystProvider);
            IBiomeController newController = new BiomeController(provider.worldChunkMgr);
            IBiomeController biomeController = biomeControllerAccess.get(controller);
            biomeControllerAccess.setField(controller,newController);
            if (biomeController instanceof SymbolBiomeControllerNative ){
               DimensionManager.logger.info(" SymbolbiomeController "+biomeController.toString());
                SymbolBiomeControllerNative standardController = (SymbolBiomeControllerNative)biomeController;
                genLayerAccess.setField(standardController, riverMix);
                biomeIndexAccess.setField(standardController, riverMix.voronoi());
            }
        }
    }
         private class BiomeController implements IBiomeController {
                 private WorldChunkManager manager;

                 protected BiomeController(WorldChunkManager manager) {
                   this.manager = manager;
                 }

                 public List<BiomeGenBase> getValidSpawnBiomes(){
                   return this.manager.getBiomesToSpawnIn();
                 }

                 public BiomeGenBase getBiomeAtCoords(int par1, int par2){
                   return this.manager.getBiomeGenAt(par1, par2);
                 }

                 public float[] getRainfallField(float[] afloat, int x, int z, int width, int length){
                   return this.manager.getRainfall(afloat, x, z, width, length);
                 }

                 public BiomeGenBase[] getBiomesFromGenerationField(BiomeGenBase[] par1ArrayOfBiomeGenBase, int par2, int par3, int par4, int par5){
                   return this.manager.getBiomesForGeneration(par1ArrayOfBiomeGenBase, par2, par3, par4, par5);
                 }

                 public BiomeGenBase[] getBiomesAtCoords(BiomeGenBase[] par1ArrayOfBiomeGenBase, int par2, int par3, int par4, int par5, boolean par6){
                   return this.manager.getBiomeGenAt(par1ArrayOfBiomeGenBase, par2, par3, par4, par5, par6);
                 }

                 public void cleanupCache(){
                   this.manager.cleanupCache();
                 }

                 public BiomeGenBase[] getBiomesForGeneration(BiomeGenBase[] par1ArrayOfBiomeGenBase, int par2, int par3, int par4, int par5) {
                   return this.manager.getBiomesForGeneration(par1ArrayOfBiomeGenBase, par2, par3, par4, par5);
                 }
               }
}
