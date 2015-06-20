
package climateControl.biomeSettings;
import climateControl.utils.Mutable;
import com.google.common.base.Optional;
import extrabiomes.lib.BiomeSettings;
import climateControl.utils.Settings;
import java.io.File;
import net.minecraftforge.common.config.Configuration;

/**
 *
 * @author Zeno410
 */
public class EBXLBiomes {

    NativeSettings settings = new NativeSettings();
    EBXLBiome alpine;
    EBXLBiome autumnwoods;
    EBXLBiome birchforest;
    EBXLBiome extremejungle;
    EBXLBiome forestedhills;
    EBXLBiome forestedisland;
    EBXLBiome glacier;
    EBXLBiome greenhills;
    EBXLBiome greenswamp;
    EBXLBiome icewasteland;
    EBXLBiome marsh;
    EBXLBiome meadow;
    EBXLBiome minijungle;
    EBXLBiome mountaindesert;
    EBXLBiome mountainridge;
    EBXLBiome mountaintaiga;
    EBXLBiome pineforest;
    EBXLBiome rainforest;
    EBXLBiome redwoodforest;
    EBXLBiome redwoodlush;
    EBXLBiome savanna;
    EBXLBiome shrubland;
    EBXLBiome snowforest;
    EBXLBiome snowyrainforest;
    EBXLBiome temperaterainforest;
    EBXLBiome tundra;
    EBXLBiome wasteland;
    EBXLBiome woodland;

    
    public EBXLBiomes(File configDirectory) {
        try {
        alpine = new EBXLBiome(BiomeSettings.ALPINE,"alpine");
        autumnwoods = new EBXLBiome(BiomeSettings.AUTUMNWOODS,"autumnwoods");
        birchforest = new EBXLBiome(BiomeSettings.BIRCHFOREST,"birchforest");
        this.extremejungle = new EBXLBiome(BiomeSettings.EXTREMEJUNGLE,"extremejungle");
        this.forestedhills = new EBXLBiome(BiomeSettings.FORESTEDHILLS,"forestedhills");
        this.forestedisland = new EBXLBiome(BiomeSettings.FORESTEDISLAND,"forestedisland");
        this.glacier = new EBXLBiome(BiomeSettings.GLACIER,"glacier");
        this.greenhills = new EBXLBiome(BiomeSettings.GREENHILLS,"greenhills");
        this.greenswamp = new EBXLBiome(BiomeSettings.GREENSWAMP,"greenswamp");
        this.icewasteland = new EBXLBiome(BiomeSettings.ICEWASTELAND,"icewasteland");
        this.marsh = new EBXLBiome(BiomeSettings.MARSH,"marsh");
        this.meadow = new EBXLBiome(BiomeSettings.MEADOW,"meadow");
        this.minijungle = new EBXLBiome(BiomeSettings.MINIJUNGLE,"minijungle");
        this.mountaindesert = new EBXLBiome(BiomeSettings.MOUNTAINDESERT,"mountaindesert");
        this.mountainridge = new EBXLBiome(BiomeSettings.MOUNTAINRIDGE,"mountainridge");
        this.mountaintaiga = new EBXLBiome(BiomeSettings.MOUNTAINTAIGA,"mountaintaiga");
        this.pineforest = new EBXLBiome(BiomeSettings.PINEFOREST,"pineforest");
        this.rainforest = new EBXLBiome(BiomeSettings.RAINFOREST,"rainforest");
        this.redwoodforest = new EBXLBiome(BiomeSettings.REDWOODFOREST,"redwoodforest");
        this.redwoodlush = new EBXLBiome(BiomeSettings.REDWOODLUSH,"redwoodlush");
        this.savanna = new EBXLBiome(BiomeSettings.SAVANNA,"savanna");
        this.shrubland = new EBXLBiome(BiomeSettings.SHRUBLAND,"shrubland");
        this.snowforest = new EBXLBiome(BiomeSettings.SNOWYFOREST,"snowyforest");
        this.snowyrainforest = new EBXLBiome(BiomeSettings.SNOWYRAINFOREST,"snowyrainforest");
        this.temperaterainforest = new EBXLBiome(BiomeSettings.TEMPORATERAINFOREST,"temporaterainforest");
        this.tundra = new EBXLBiome(BiomeSettings.TUNDRA,"tundra");
        this.wasteland = new EBXLBiome(BiomeSettings.WASTELAND,"wasteland");
        this.woodland = new EBXLBiome(BiomeSettings.WOODLANDS,"woodlands");
        settings.readFrom(new Configuration(new File(configDirectory,"extrabiomes.cfg")));
        } catch (java.lang.NoClassDefFoundError e){
            // EBXL isn't installed
        }
    }

    public class EBXLBiome {
        private final BiomeSettings source;
        private final String configName;
        private final Mutable<Integer> nativeID;
        private EBXLBiome(BiomeSettings source, String configName) {
            this.source = source;
            this.configName = configName;
            nativeID = settings.biomeCategory.intSetting(configName+".id", nativeBiomeID());
        }

        public int nativeBiomeID() {
            if (source.isEnabled()) return source.getID();
            return -1;
        }

        public int biomeID() {
            return nativeID.value();
        }
    }

    private class NativeSettings extends Settings {
        public final Category biomeCategory = new Category("biome");

    }

}
