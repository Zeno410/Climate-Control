
package climateControl.api;

import climateControl.biomeSettings.*;
import climateControl.utils.Mutable;
import climateControl.utils.Settings;
import climateControl.generator.BiomeSwapper;
import climateControl.ClimateDistribution;
import climateControl.generator.SubBiomeChooser;
import java.io.File;
import java.util.ArrayList;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenBase.TempCategory;
import climateControl.utils.ConcreteMutable;
import climateControl.utils.Zeno410Logger;
import java.util.logging.Logger;

/**
 *
 * @author Zeno410
 */
abstract public class BiomeSettings extends Settings {

    public static final Logger logger = new Zeno410Logger("BiomeSettings").logger();
    private final ArrayList<Element> elements = new ArrayList<Element>();
    private final ArrayList<ID> ids = new ArrayList<ID>();
    private final Category idCategory;
    private final Category incidenceCategory;
    private final Category villagesCategory;
    private final Category climateCategory;
    public final Category climateControlCategory = category("Assorted Parameters");


    public BiomeSettings (String categoryName) {
        idCategory = category(categoryName+"IDs");
        incidenceCategory = category(categoryName+"Incidences");
        villagesCategory = category(categoryName+"Villages");
        climateCategory = category(categoryName+"Climates");
    }

    public class ID {
        private Mutable<Integer> biomeID;
        private Mutable<Boolean> hasVillages;
        protected final Mutable<String> climate;
        public final String name;

        public ID(String name, int ID, boolean hasVillages, String climate) {
            this(name,idCategory.intSetting(name+" ID", ID),hasVillages,climate);
        }

        public ID(String name, int ID, boolean hasVillages) {
            this(name,ID,hasVillages,"DEFAULT");
        }

        public ID(String name, int ID) {
            this(name,ID,false);
        }

        public ID(String name, Mutable<Integer> ID, boolean hasVillages, String climate) {
            this.name = name;
            biomeID = ID;
            this.hasVillages = villagesCategory.booleanSetting(name+" hasVillages", hasVillages);
            this.climate = climateCategory.stringSetting(name + " climate", climate);
            ids.add(this);
        }

        public ID(String name, Mutable<Integer> ID) {
            this(name,ID,false,"DEFAULT");
        }

        public Mutable<Integer> biomeID() {return biomeID;}
        private ID MBiome = null;
        public void setMBiome(ID MBiome) {
            if (this.MBiome != null ) {
                throw new RuntimeException("accidental reset of M Biome for "+name);
            }
            this.MBiome = MBiome;
        }

        public void setIDFrom(BiomeGenBase biome) {
            try {
                if (BiomeGenBase.getBiomeGenArray()[biome.biomeID].equals(biome)) {
                    biomeID().set(biome.biomeID);
                } else biomeID().set(-1);
                //ClimateControl.logger.info(name + " set to "+biomeID().value());
            } catch (NullPointerException e) {
                biomeID().set(-1);
                //ClimateControl.logger.info(name + " disabled");
            }
        }
        
        public boolean active() {
            if (biomeID.value() <0) return false;
            if (BiomeGenBase.getBiomeGenArray()[biomeID.value()] == null) return false;
            int oldID = BiomeGenBase.getBiomeGenArray()[biomeID.value()].biomeID;
            if (oldID != biomeID.value()) {
                String text = " Attempted to use biome "+this.name + " at ID + " + biomeID.value() 
                        + " but that biome is assigned to "+oldID;
                throw new RuntimeException(text);
            }
            return true;
        }

        private BiomeReplacer subBiomeChooser = BiomeReplacer.noChange;

        public void setSubBiomeChooser(BiomeReplacer subBiomeChooser) {
            this.subBiomeChooser = subBiomeChooser;
        }

        public void setSubBiome(ID subBiome) {
            subBiomeChooser = new BiomeReplacer.Fixed(subBiome.biomeID.value());
        }
    }

    public class Element extends ID {

        public Element(String name, int ID, int incidence, boolean hasVillages, String climate) {
            super(name,ID,hasVillages,climate);
            biomeIncidence = incidenceCategory.intSetting(name+ " Incidence", incidence);
            biomeIncidence.set(incidence);
            //biomeIncidence = groupCategory.intSetting(name+"Active", incidence);
            elements.add(this);
            distribution = null;
        }

        public Element(String name, int ID, int incidence, boolean hasVillages) {
            this(name,ID,incidence,hasVillages,"DEFAULT");
        }

        public Element(String name, int ID, int incidence, String climate) {
            this(name,ID,incidence,false,climate);
        }

        public Element(String name, int ID, int incidence) {
            this(name,ID,incidence,false);
        }
        public Element(String name, int ID) {
            this(name,ID,10,false);
        }
        public Element(String name, int ID,String climate) {
            this(name,ID,10,false,climate);
        }
        public Element(String name, int ID, boolean hasVillages) {
            this(name,ID,10,false);
        }

        public Element(String name, int ID, boolean hasVillages,String climate) {
            this(name,ID,10,false,climate);
        }
        
        private Mutable<Integer> biomeIncidence;

        private ClimateDistribution distribution;
        
        public void setDistribution(ClimateDistribution newDistribution) {
            distribution = newDistribution;
        }
        
        public  ClimateDistribution distribution() {
            if (distribution != null) return distribution;
            return configDistribution();
        }

        public ClimateDistribution configDistribution() {
            if (this.climate.value().equalsIgnoreCase("DEFAULT")) return defaultDistribution();
            if (this.climate.value().equalsIgnoreCase("")) return defaultDistribution();
            for (ClimateDistribution testedDistribution: ClimateDistribution.list) {
                if (this.climate.value().equalsIgnoreCase(testedDistribution.name())) {
                    return testedDistribution;
                }
            }
            throw new RuntimeException("Climate "+climate.value()+" not recognized");
        }

        public Mutable<Integer> biomeIncidences() {return biomeIncidence;}
        //private Mutable<Boolean> biomeActive() {return biomeActive;}

        private ClimateDistribution defaultDistribution() {
            TempCategory temp = null;
            try {
                temp = BiomeGenBase.getBiomeGenArray()[biomeID().value()].getTempCategory();
                //ClimateControl.logger.info(" temp for "+this.name + " biome id" + biomeID().value());
                if (temp == BiomeGenBase.TempCategory.COLD) return ClimateDistribution.SNOWY;
                if (temp == BiomeGenBase.TempCategory.MEDIUM) return ClimateDistribution.MEDIUM;
                if (temp == BiomeGenBase.TempCategory.WARM) return ClimateDistribution.HOT;
            } catch (Exception e) {
                //ClimateControl.logger.info("temp for " + name + " error " + e.toString());
            }
            throw new NoTempSetting();
        }
    }

    public class NoTempSetting extends RuntimeException {}

    protected ID M(ID original, String name) {
        ID result = new ID(name,original.biomeID().value()+128);
        original.setMBiome(result);
        return result;
    }

    protected ID M(ID original) {
        return M(original, original.name+" M");
    }
    
    public ArrayList<ClimateDistribution.Incidence> incidences() {
        ArrayList<ClimateDistribution.Incidence> result = new ArrayList<ClimateDistribution.Incidence>();
        for (Element element: elements) {
            // skip if inactive
            if (!element.active()) continue;
            try {
                result.addAll(element.distribution().incidences(element));
                //ClimateControl.logger.info(element.name+ " " + element.biomeID().value() + " added");
            } catch (BiomeSettings.NoTempSetting e) {
                //ClimateControl.logger.info(element.name+ " " + element.biomeID().value() + " missed");
                // ignoring for now
            }
        }
        return result;
    }

    private ClimateDistribution climateDistribution(Element element) {
        return element.distribution();
    }

    public void update(SubBiomeChooser subBiomeChooser) {
        for (ID id: ids) {
            if (id.active()) {
                subBiomeChooser.set(id.biomeID().value(), id.subBiomeChooser);
            }
        }
    }

    public void updateMBiomes(BiomeSwapper mBiomes) {
        for (ID id: this.ids) {
            if (id.MBiome != null) {
                mBiomes.set(id.biomeID().value(),id.MBiome.biomeID().value());
            }
        }
    }

    protected ID externalBiome(String name,int biomeID){
        return new ID(name, new ConcreteMutable<Integer>(biomeID));
    }

    abstract public void setRules(ClimateControlRules rules);
    abstract public void setNativeBiomeIDs(File configDirectory);

    public void setVillages(ClimateControlRules rules) {
        for (ID id: this.ids) {
            if (id.hasVillages.value()) {
                rules.allowVillages(id.biomeID.value());
            }
        }
    }

    public final boolean equalsByIdentity(BiomeSettings compared) {
        return super.equals(compared);
    }

    public abstract boolean biomesAreActive();

    public void report() {}
}
