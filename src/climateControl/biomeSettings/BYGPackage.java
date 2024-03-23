package climateControl.biomeSettings;

import climateControl.api.BiomePackage;
import climateControl.api.BiomeSettings;

public class BYGPackage extends BiomePackage {

    public BYGPackage() {
        super("biomesyougoInCC.cfg");
    }

    @Override
    public BiomeSettings freshBiomeSetting() {
        return new BYGSettings();
    }

}
