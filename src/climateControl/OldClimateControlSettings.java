
package climateControl;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldSavedData;

import net.minecraftforge.common.config.Configuration;

/**
 *
 * @author Zeno410
 */
public class OldClimateControlSettings extends WorldSavedData implements PublicallyCloneable {
    static final String halfSizeName = "Half Zone Size";
    static final String quarterZoneName = "Quarter Zone Size";
    static final String randomBiomesName = "Random Biomes";
    static final String allowDerpyIslandsName = "Allow Derpy Islands";
    static final String moreOceanName = "More Ocean";
    static final String largeContinentFrequencyName = "Large Continent Frequency";
    static final String mediumContinentFrequencyName = "Medium Continent Frequency";
    static final String smallContinentFrequencyName = "Small Continent Frequency";
    static final String largeIslandFrequencyName ="Large Island Frequency";
    static final String hotIncidenceName = "Hot Zone Incidence";
    static final String warmIncidenceName = "Warm Zone Incidence";
    static final String coolIncidenceName = "Cool Zone Incidence";
    static final String snowyIncidenceName = "Snowy Zone Incidence";

    static final String climateControlCategory = "Assorted Parameters";
    static final String climateZoneCategory = "Climate Zone Parameters";
    static final String climateIncidenceCategory = "Climate Incidences";
    static final String oceanControlCategory = "Ocean Control Parameters";

    static final String customParameter = "Custom Climate Control Parameters";

        boolean halfSize = true;
        boolean quarterSize = false;
        boolean allowDerpyIslands = false;
        boolean moreOcean = true;
        boolean randomBiomes = false;

        int largeContinentFrequency = 8;
        int mediumContinentFrequency = 11;
        int smallContinentFrequency = 16;
        int largeIslandFrequency = 30;

        int hotIncidence = 1;
        int warmIncidence = 1;
        int coolIncidence = 1;
        int snowyIncidence = 1;

        BiomeIncidences biomeIncidences;

        public int hotIncidence() {return hotIncidence;}
        public int warmIncidence() {return warmIncidence;}
        public int chillyIncidence() {return coolIncidence;}
        public int snowyIncidence() {return snowyIncidence;}

        public OldClimateControlSettings clone() {
            OldClimateControlSettings clone = new OldClimateControlSettings();
            clone.halfSize = this.halfSize;
            clone.quarterSize = this.quarterSize;
            clone.randomBiomes = this.randomBiomes;
            clone.allowDerpyIslands = this.allowDerpyIslands;
            clone.moreOcean = this.moreOcean;
            clone.hotIncidence = this.hotIncidence;
            clone.warmIncidence = this.warmIncidence;
            clone.coolIncidence = this.coolIncidence;
            clone.snowyIncidence = this.snowyIncidence;
            clone.biomeIncidences = this.biomeIncidences;
            return clone;
        }

        public boolean doFull() {return !halfSize && !quarterSize;}
        public boolean doHalf() {return halfSize && !quarterSize;}

        OldClimateControlSettings() {super("ClimateControlSettings");}

        void set(Configuration config) {
            config.addCustomCategoryComment(climateZoneCategory, "" +
                    "Full-size is 1.7 defaults. " +
                    "Half-size creates zones similar to pre-1.7 snowy zones. " +
                    "Quarter-size creates fairly small zones but the hot and snowy incidences need to be increased" +
                    "Smoothing to eliminate clashes erases a lot of quarter-size hot and snowy zones");
            halfSize = config.get(climateZoneCategory, halfSizeName, true,
                    "half the climate zone size from vanilla, unless quartering").getBoolean(true);
            quarterSize = config.get(climateZoneCategory, quarterZoneName, false,
                    "quarter the climate zone size from vanilla, and ignore halfsize flag").getBoolean(false);
            randomBiomes = config.get(climateZoneCategory, randomBiomesName, false,
                    "ignore climate zones altogether").getBoolean(false);
            allowDerpyIslands = config.get(climateControlCategory, allowDerpyIslandsName, false,
                    "place little islands near shore rather than in deep ocean").getBoolean(false);

            config.addCustomCategoryComment(climateIncidenceCategory, "" +
                    "Blocks of land are randomly assigned to each zone with a frequency proportional to the incidence. " +
                    "Processing eliminates some extreme climates later, especially for quarter size zones. " +
                    "Consider doubling hot and snowy incidences for quarter size zones.");
            hotIncidence = config.get(climateIncidenceCategory, hotIncidenceName, 1,
                    "relative incidence of hot zones with climates like savanna and desert").getInt(1);
            warmIncidence = config.get(climateIncidenceCategory, warmIncidenceName, 1,
                    "relative incidence of warm zones with forest/plain/hills + jungle/swamp").getInt(1);
            coolIncidence = config.get(climateIncidenceCategory, coolIncidenceName, 1,
                    "relative incidence of cool zones with forest/plain/hills + taiga").getInt(1);
            snowyIncidence = config.get(climateIncidenceCategory, snowyIncidenceName, 1,
                    "relative incidence of snowy zones").getInt(1);

            config.addCustomCategoryComment(oceanControlCategory, "" +
                    "Frequencies are x per thousand but a little goes a very long way because seeds grow a lot. " +
                    "About half the total continent frequencies is the percent land. " +
                    "For worlds with 1.7-like generation set large island seeds to about 300. " +
                    "That will largely fill the oceans after seed growth.");

            largeContinentFrequency = config.get(oceanControlCategory, largeContinentFrequencyName, 8,
                    "frequency of large continent seeds, about 8000x16000").getInt(8);
            mediumContinentFrequency = config.get(oceanControlCategory, mediumContinentFrequencyName, 11,
                    "frequency of medium continent seeds, about 4000x8000").getInt(11);
            smallContinentFrequency = config.get(oceanControlCategory, smallContinentFrequencyName, 16,
                    "frequency of small continent seeds, about 2000x4000").getInt(16);
            largeIslandFrequency = config.get(oceanControlCategory, largeIslandFrequencyName, 30,
                    "frequency of large island seeds, about 1000x2000").getInt(30);

            biomeIncidences = new BiomeIncidences(config);

        }

        public void readFromNBT(NBTTagCompound tag) {
            halfSize = tag.getBoolean(halfSizeName);
            quarterSize = tag.getBoolean(quarterZoneName);
            try {
                allowDerpyIslands = tag.getBoolean(allowDerpyIslandsName);
                moreOcean = tag.getBoolean(moreOceanName);
            } catch (Exception e) {}
            largeContinentFrequency = tag.getInteger(largeContinentFrequencyName);
            mediumContinentFrequency = tag.getInteger(mediumContinentFrequencyName);
            smallContinentFrequency = tag.getInteger(smallContinentFrequencyName);
            largeIslandFrequency = tag.getInteger(largeIslandFrequencyName);
            hotIncidence = tag.getInteger(hotIncidenceName);
            warmIncidence = tag.getInteger(warmIncidenceName);
            coolIncidence = tag.getInteger(coolIncidenceName);
            snowyIncidence = tag.getInteger(snowyIncidenceName);

            biomeIncidences.readFromNBT(tag);
        }

        public void writeToNBT(NBTTagCompound tag) {
            tag.setBoolean(halfSizeName, halfSize);
            tag.setBoolean(quarterZoneName, quarterSize);
            tag.setBoolean(allowDerpyIslandsName,allowDerpyIslands);
            tag.setBoolean(moreOceanName,moreOcean);

            tag.setInteger(largeContinentFrequencyName,largeContinentFrequency);
            tag.setInteger(mediumContinentFrequencyName,mediumContinentFrequency);
            tag.setInteger(smallContinentFrequencyName,smallContinentFrequency);
            tag.setInteger(largeIslandFrequencyName,largeIslandFrequency);

            tag.setInteger(hotIncidenceName,hotIncidence);
            tag.setInteger(warmIncidenceName,warmIncidence);
            tag.setInteger(coolIncidenceName,coolIncidence);
            tag.setInteger(snowyIncidenceName,snowyIncidence);

            biomeIncidences.writeToNBT(tag);
        }

    }