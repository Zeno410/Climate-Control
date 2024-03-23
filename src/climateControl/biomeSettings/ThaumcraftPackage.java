package climateControl.biomeSettings;

import biomesoplenty.api.biome.BOPBiomes;
import biomesoplenty.common.init.ModBiomes;
import climateControl.api.BiomePackage;
import climateControl.api.BiomeSettings;

/**
*
* @author Zeno410
*/
public class ThaumcraftPackage extends BiomePackage {

   public ThaumcraftPackage() {
       super("ThaumcraftinCC.cfg");
       // confirm Thaumcraft is there.
       Class biomesClass = ModBiomes.class;
       try {
           int throwaway = BOPBiomes.alps.hashCode();
       } catch (NullPointerException e) {
           //not yet set is fine, this is testing for the field
       }
   }

   @Override
   public BiomeSettings freshBiomeSetting() {
       return new ThaumcraftBiomeSettings();
   }

}
