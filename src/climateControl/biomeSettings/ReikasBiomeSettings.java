
package climateControl.biomeSettings;

import climateControl.api.BiomeSettings;
import climateControl.utils.Acceptor;
import climateControl.utils.Settings;
import java.io.File;
import net.minecraftforge.common.config.Configuration;
import climateControl.api.ClimateControlRules;
import climateControl.utils.Mutable;

/**
 *
 * @author Zeno410
 */
public class ReikasBiomeSettings extends BiomeSettings {
    public static final String biomeCategory = "ReikasBiome";
    public static final String reikasCategory = "ReikasSettings";
    public final Category reikasSettings = new Category(reikasCategory);

    public final Element  enderForest = new Element("Ender Forest", 47,"MEDIUM");
    public final Element  island = new Element("Island", 101,"OCEAN");
    public final Element  plains = new Element("Plains", 102,"PLAINS");
    public final Element  rainbowForest = new Element("Rainbow Forest", 48,"MEDIUM");
    public final Element  skyland = new Element("Skyland", 100,"LAND");


    public ReikasBiomeSettings() {
        super(biomeCategory);
        enderForest.biomeIncidences().set(3);
        island.biomeIncidences().set(3);
        plains.biomeIncidences().set(3);
        rainbowForest.biomeIncidences().set(3);
        skyland.biomeIncidences().set(4);
    }




    public File configFile(File source) {
        File directory = source.getParentFile();
            File highlands = new File(directory,"Highlands");
            File generalConfig = new File(highlands,"General.cfg");
            return generalConfig;
    }

    @Override
    public void setNativeBiomeIDs(File configDirectory) {
        try {
            NativeChromaticraftSettings nativeSettings = nativeIDs(configDirectory);
            this.enderForest.biomeID().set(nativeSettings.EnderForestID.value());
            island.biomeID().set(nativeSettings.IslandID.value());
            plains.biomeID().set(nativeSettings.PlainsID.value());
            skyland.biomeID().set(nativeSettings.SkylandID.value());
            rainbowForest.biomeID().set(nativeSettings.RainbowForestID.value());
        } catch (java.lang.NoClassDefFoundError e) {
            // no highlands
        }
    }

    @Override
    public void setRules(ClimateControlRules rules) {
    }

    static final String biomesOnName = "ReikasBiomesOn";

    public final Mutable<Boolean> biomesFromConfig = climateControlCategory.booleanSetting(
                        biomesOnName, "", false);
    @Override
    public boolean biomesAreActive() {
        return this.biomesFromConfig.value();
    }

    private NativeChromaticraftSettings nativeIDs(File configDirectory) {
        NativeChromaticraftSettings result = new NativeChromaticraftSettings();
        File reikaDirectory = new File(configDirectory,"Reika");
        File configFile = new File(reikaDirectory,"ChromatiCraft.cfg");
        result.readFrom(new Configuration(configFile));
        return result;
    }

}


class NativeChromaticraftSettings extends Settings {
    public static final String biomeIDName = "biome ids";
    public final Category biomeIDs = new Category(biomeIDName);

    Mutable<Integer> EnderForestID =biomeIDs.intSetting("Ender Forest Biome ID", 47);
    Mutable<Integer> IslandID =biomeIDs.intSetting("Island Biome ID", 101);
    Mutable<Integer> PlainsID =biomeIDs.intSetting("Plains Biome ID", 102);
    Mutable<Integer> RainbowForestID =biomeIDs.intSetting("Rainbow Forest Biome ID", 48);
    Mutable<Integer> SkylandID =biomeIDs.intSetting("Skyland Biome ID", 100);

}
