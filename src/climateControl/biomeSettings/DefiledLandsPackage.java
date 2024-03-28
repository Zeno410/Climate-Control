package climateControl.biomeSettings;

import climateControl.api.BiomePackage;
import climateControl.api.BiomeSettings;

public class DefiledLandsPackage extends BiomePackage {

    public DefiledLandsPackage() {
        super("defiledlandsinCC.cfg");
    }

    @Override
    public BiomeSettings freshBiomeSetting() {
        return new DefiledLandsSettings();
    }

}