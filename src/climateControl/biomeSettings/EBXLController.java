
package climateControl.biomeSettings;

import climateControl.api.BiomePackage;
import climateControl.api.BiomeSettings;
import climateControl.utils.Acceptor;
import climateControl.utils.Mutable;
import climateControl.utils.Settings;


/**
 *
 * @author Zeno410
 */
public class EBXLController extends BiomePackage {

    public final static String EBXLBiomesOnName = "EBXLBiomesOn";
    public Mutable<Boolean> biomesFromConfig;
    public BiomeSettings biomeSettings = new EBXLBiomeSettings();
    public Mutable<Boolean> biomesOn = new Mutable.Concrete<Boolean>(false);
    public Acceptor<Boolean> onTracker = new Acceptor<Boolean>() {
        public void accept(Boolean accepted) {
            biomesOn.set(accepted);
        }
    };

    public EBXLController() {
        super("EBXLInCC.cfg");
        Class biomesClass = extrabiomes.lib.BiomeSettings.class;
    }

    public void setTracker(Settings.Category climateControlCategory) {
        try {
            // attach a setting
            biomesFromConfig = climateControlCategory.booleanSetting(
                    EBXLBiomesOnName, "", false);
            // pass changes to the flag guaranteed to exists;
            biomesFromConfig.informOnChange(onTracker);

        } catch (java.lang.NoClassDefFoundError e){
            // EBXL isn't installed
        }
    }

    @Override
    public BiomeSettings freshBiomeSetting() {
        return new EBXLBiomeSettings();
    }
}
