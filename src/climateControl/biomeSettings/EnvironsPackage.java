package climateControl.biomeSettings;

import climateControl.api.BiomePackage;
import climateControl.api.BiomeSettings;

public class EnvironsPackage extends BiomePackage {

    public EnvironsPackage() {
        super("environsInCC.cfg");
    }

    @Override
    public BiomeSettings freshBiomeSetting() {
        return new EnvironsSettings();
    }

}
