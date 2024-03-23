
package climateControl.api;

import com.Zeno410Utils.Numbered;
import java.util.ArrayList;
import java.util.Collection;
import net.minecraft.world.biome.Biome;

/**
 *
 * @author Zeno410
 */
public class ClimateDistribution {
    public static final ArrayList<ClimateDistribution> list = new ArrayList<ClimateDistribution>() ;

    private ArrayList<Climate> climates =new ArrayList<Climate>();
    private final String name;
    public static ClimateDistribution SNOWY = new ClimateDistribution(Climate.SNOWY,"SNOWY");
    public static ClimateDistribution COOL = new ClimateDistribution(Climate.COOL,"COOL");
    public static ClimateDistribution WARM = new ClimateDistribution(Climate.WARM,"WARM");
    public static ClimateDistribution HOT = new ClimateDistribution(Climate.HOT,"HOT");
    public static ClimateDistribution MEDIUM = new ClimateDistribution(Climate.COOL,Climate.WARM,"MEDIUM");
    public static ClimateDistribution PLAINS = new ClimateDistribution(Climate.COOL,Climate.WARM,Climate.HOT,"PLAINS");
    public static ClimateDistribution OCEAN = new ClimateDistribution(Climate.OCEAN,"OCEAN");
    public static ClimateDistribution DEEP_OCEAN = new ClimateDistribution(Climate.DEEP_OCEAN,"DEEP_OCEAN");
    public static ClimateDistribution LAND = new ClimateDistribution(Climate.SNOWY,Climate.COOL,Climate.WARM,Climate.HOT,"LAND");
    public static ClimateDistribution SEA = new ClimateDistribution(Climate.OCEAN,Climate.DEEP_OCEAN,"SEA");

    public ClimateDistribution(Climate base,String name){
        climates.add(base);
        this.name = name;
        list.add(this);
    }

    private ClimateDistribution(Climate one, Climate two,String name){
        climates.add(one);
        climates.add(two);
        this.name = name;
        list.add(this);
    }

    private ClimateDistribution(Climate one, Climate two, Climate three,String name){
        climates.add(one);
        climates.add(two);
        climates.add(three);
        this.name = name;
        list.add(this);
    }

    private ClimateDistribution(Climate one, Climate two, Climate three,Climate four,String name){
        climates.add(one);
        climates.add(two);
        climates.add(three);
        climates.add(four);
        this.name = name;
        list.add(this);
    }

    public String name() {return name;}
    
    public class Incidence {
        public final int biome;
        public final int incidence;  // final because it's constantly regenerated
        public final Climate climate;
        private Incidence(int biome, int incidence,Climate climate) {
            this.biome = biome;
            this.incidence = incidence;
            this.climate = climate;
        }
    }

    public Collection<Incidence> incidences(BiomeSettings.Element element) {
        // return a list of Incidences with that element's incidence split up amont that distribution's
        // climates;
        ArrayList<Incidence> result = new ArrayList<Incidence>();
        int doneSoFar = 0;
        int remainingIncidence = element.biomeIncidences().value();
        for (Climate climate: climates) {
            int thisIncidence = remainingIncidence/(climates.size()-doneSoFar);
            doneSoFar++;
            result.add(new Incidence(element.biomeID().value(),thisIncidence,climate));
            remainingIncidence -= thisIncidence;
        }
        return result;
    }
}
