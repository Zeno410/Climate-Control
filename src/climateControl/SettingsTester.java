
package climateControl;

import climateControl.api.BiomeRandomizer;
import climateControl.api.Climate;
import climateControl.api.ClimateControlSettings;
import com.Zeno410Utils.GuiChoice;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;

/**
 *
 * @author Zeno410
 */
public class SettingsTester {

    public void test(ClimateControlSettings tested) {
        if (tested.biomeSettings().size()<2) {
            ArrayList messages = new ArrayList();
            messages.add("There are no land biomes groups");
            messages.add("Vanilla biomes are off but no mod biomes are on");
            messages.add("Running will almost certainly crash the system");
            Minecraft minecraft = Minecraft.getMinecraft();
            if (minecraft!=null) {
                //GuiScreen guiScreen= new GuiChoice(messages);
                //minecraft.displayGuiScreen(guiScreen);
                for (int i = 1; i <4; i++) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(SettingsTester.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } else {
                throw new RuntimeException("Climate Control: Vanilla biomes are inactive but no mod biomes are on");
            }
        }
        // get active climate list
        if (tested.randomBiomes.value() == false) {
            Climate[] climates = new Climate[5];
            climates[1] = Climate.HOT;
            climates[2] = Climate.WARM;
            climates[3] = Climate.COOL;
            climates[4] = Climate.SNOWY;
            // which biomes are active
            boolean [] active = new boolean[5];
            if (tested.hotIncidence.value()<0) throw new RuntimeException("Climate Incidences must all be positive");
            if (tested.warmIncidence.value()<0) throw new RuntimeException("Climate Incidences must all be positive");
            if (tested.coolIncidence.value()<0) throw new RuntimeException("Climate Incidences must all be positive");
            if (tested.snowyIncidence.value()<0) throw new RuntimeException("Climate Incidences must all be positive");

            active[1] = tested.hotIncidence.value()>0;
            active[2] = tested.warmIncidence.value()>0;
            active[3] = tested.coolIncidence.value()>0;
            active[4] = tested.snowyIncidence.value()>0;
                        // fill in smoothed biomes
            // fill in smoothed biomes
            int highestActive= -1;
            for (int i = 1; i < 5; i ++) {
                if (active[i]) {
                    highestActive = i;
                }
            }
            if (highestActive == -1) {
                throw new RuntimeException("Climate Control: All Climate incidences set to 0. At least one much be positive");
            }
            // fill in the ones created by smoothing
            for (int i = 0; i < highestActive; i ++) {
                if (active[i]) {
                    for (int j = i+1; j < highestActive; j++) {
                        active[j] = true;
                    }
                }
            }


            BiomeRandomizer.PickByClimate testClimatePicker = 
                    new BiomeRandomizer(tested.biomeSettings()).pickByClimate();

            String activeWithoutBiomes = "";
            // ocean always active
            if (testClimatePicker.hasBiomes(0) == false)
                activeWithoutBiomes = activeWithoutBiomes.concat(Climate.OCEAN.name+" ");
            if (testClimatePicker.hasBiomes(Biome.getIdForBiome(Biomes.DEEP_OCEAN)) == false)
                activeWithoutBiomes = activeWithoutBiomes.concat(Climate.DEEP_OCEAN.name+" ");
            for (int i = 1; i < 5; i ++) {
                if (active[i]) {
                    if (testClimatePicker.hasBiomes(i) == false){
                        activeWithoutBiomes = activeWithoutBiomes.concat(climates[i].name+" ");
                    }
                }
            }
            if (activeWithoutBiomes.length()>0) {
                throw new RuntimeException("Climate Control: No Biomes present for climates "+activeWithoutBiomes);
            }


        }
    }

}
