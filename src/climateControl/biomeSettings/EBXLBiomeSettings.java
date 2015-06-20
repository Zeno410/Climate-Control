
package climateControl.biomeSettings;
import climateControl.api.BiomeSettings;
import climateControl.api.ClimateControlRules;
import climateControl.utils.Mutable;
import com.google.common.base.Optional;
import extrabiomes.api.BiomeManager;
import java.io.File;

/**
 *
 * @author Zeno410
 */
public class EBXLBiomeSettings extends BiomeSettings {
    public static final String biomeCategory = "EXBLBiome";
    public static final String EBLXCategory = "EBLXSettings";
    public final Category EBLXSettings = new Category(EBLXCategory);

   public final Element  alpine = new Element("Alpine", 60);
   public final Element  autumnwoods = new Element("Autumn Woods", 61);
   public final Element  birchforest = new Element("Birch Forest", 62);
   public final Element  extremejungle = new Element("Extreme Jungle", 63);
   public final Element  forestedisland = new Element("Forested Island", 65);
   public final Element  forestedhills = new Element("Forested Hills", 64);
   public final Element  glacier = new Element("Glacier", 66);
   public final Element  greenhills = new Element("Highlands", 67);
   public final Element  icewasteland = new Element("Ice Wasteland", 41);
   public final Element  greenswamp = new Element("Green Swamp", 40);
   public final ID  marsh = new ID("Marsh",-1);
   public final Element  meadow = new Element("Meadow", 43);
   public final Element  minijungle = new Element("Minijungle", 44);
   public final Element  mountaindesert = new Element("Mountain Desert", 45);
   public final Element  mountainridge = new Element("Mountain Ridge", 46);
   public final Element  mountaintaiga = new Element("Mountain Taiga", 47);
   public final Element  pineforest = new Element("Pine Forest", 48);
   public final Element  rainforest = new Element("Raintforest", 49);
   public final Element  redwoodforest = new Element("Redwood Forest", 50);
   public final Element  redwoodlush = new Element("Lush Redwood Forest", 51);
   public final Element  savanna = new Element("Savanna", 52);
   public final Element  shrubland = new Element("Shrubland", 53);
   public final Element  snowyforest = new Element("Snowy Forest", 54);
   public final Element  snowyrainforest = new Element("Snowy Rainforest", 55);
   public final Element  temperaterainforest = new Element("Temperate Rainforest", 56);
   public final Element  tundra = new Element("Tundra", 57);
   public final Element  wasteland = new Element("Wasteland", 58);
   public final Element  woodlands = new Element("Woodlands", 59);

   public EBXLBiomeSettings() {
        super(biomeCategory);
    }

    @Override
    public void setRules(ClimateControlRules rules) {
        
    }

    @Override
    public void setNativeBiomeIDs(File configDirectory) {
        
        EBXLBiomes EBXLBiomes = new EBXLBiomes(configDirectory);
        this.alpine.biomeID().set(EBXLBiomes.alpine.biomeID());
        this.autumnwoods.biomeID().set(EBXLBiomes.autumnwoods.nativeBiomeID());
        this.birchforest.biomeID().set(EBXLBiomes.birchforest.nativeBiomeID());
        this.extremejungle.biomeID().set(EBXLBiomes.extremejungle.nativeBiomeID());
        this.forestedhills.biomeID().set(EBXLBiomes.forestedhills.nativeBiomeID());
        this.forestedisland.biomeID().set(EBXLBiomes.forestedisland.nativeBiomeID());
        this.glacier.biomeID().set(EBXLBiomes.glacier.nativeBiomeID());
        this.greenhills.biomeID().set(EBXLBiomes.greenhills.nativeBiomeID());
        this.greenswamp.biomeID().set(EBXLBiomes.greenswamp.nativeBiomeID());
        this.icewasteland.biomeID().set(EBXLBiomes.icewasteland.nativeBiomeID());
        this.marsh.biomeID().set(EBXLBiomes.marsh.nativeBiomeID());
        this.meadow.biomeID().set(EBXLBiomes.meadow.nativeBiomeID());
        this.minijungle.biomeID().set(EBXLBiomes.minijungle.nativeBiomeID());
        this.mountaindesert.biomeID().set(EBXLBiomes.mountaindesert.nativeBiomeID());
        this.mountainridge.biomeID().set(EBXLBiomes.mountainridge.nativeBiomeID());
        this.mountaintaiga.biomeID().set(EBXLBiomes.mountaintaiga.nativeBiomeID());
        this.pineforest.biomeID().set(EBXLBiomes.pineforest.nativeBiomeID());
        this.rainforest.biomeID().set(EBXLBiomes.rainforest.nativeBiomeID());
        this.redwoodforest.biomeID().set(EBXLBiomes.redwoodforest.nativeBiomeID());
        this.redwoodlush.biomeID().set(EBXLBiomes.redwoodlush.nativeBiomeID());
        this.savanna.biomeID().set(EBXLBiomes.savanna.nativeBiomeID());
        this.shrubland.biomeID().set(EBXLBiomes.shrubland.nativeBiomeID());
        this.snowyforest.biomeID().set(EBXLBiomes.snowforest.nativeBiomeID());
        this.snowyrainforest.biomeID().set(EBXLBiomes.snowyrainforest.nativeBiomeID());
        this.temperaterainforest.biomeID().set(EBXLBiomes.temperaterainforest.nativeBiomeID());
        this.tundra.biomeID().set(EBXLBiomes.tundra.nativeBiomeID());
        this.wasteland.biomeID().set(EBXLBiomes.wasteland.nativeBiomeID());
        this.woodlands.biomeID().set(EBXLBiomes.woodland.nativeBiomeID());
    }

    static final String biomesOnName = "EBXLBiomesOn";

    public final Mutable<Boolean> biomesFromConfig = climateControlCategory.booleanSetting(
                        biomesOnName, "", false);
    @Override
    public boolean biomesAreActive() {
        return this.biomesFromConfig.value();
    }
}
