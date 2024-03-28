package climateControl.biomeSettings;

import climateControl.api.BiomePackage;
import climateControl.api.BiomeSettings;

public class SpookyPackage extends BiomePackage {

    public SpookyPackage() {
        super("spookyBiomesInCC.cfg");
    }

    @Override
    public BiomeSettings freshBiomeSetting() {
        return new SpookySettings();
    }

}