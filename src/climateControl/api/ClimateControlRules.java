
package climateControl.api;

/**
 *
 * @author Zeno410
 */
public class ClimateControlRules {
    private boolean[] riversDisallowed = new boolean[BiomeSettings.highestBiomeID()+1];
    private boolean[] stoneBeachDisallowed = new boolean[BiomeSettings.highestBiomeID()+1];
    private boolean[] noBeaches = new boolean[BiomeSettings.highestBiomeID()+1];
    private boolean[] hasVillages = new boolean[BiomeSettings.highestBiomeID()+1];
    private boolean moreComplexSubbiomes = false;

    public final boolean riversAllowed(int biomeID ) {return !riversDisallowed[biomeID];}
    public final boolean riversDisallowed(int biomeID ) {return riversDisallowed[biomeID];}
    public final boolean stoneBeachAllowed(int biomeID ) {return !stoneBeachDisallowed[biomeID];}
    public final boolean stoneBeachDisallowed(int biomeID ) {return stoneBeachDisallowed[biomeID];}
    public final boolean noBeachesAllowed(int biomeID ) {return noBeaches[biomeID];}
    public final boolean beachesAllowed(int biomeID ) {return !noBeaches[biomeID];}
    public final boolean hasVillages(int biomeID ) {return hasVillages[biomeID];}
    public final boolean moreComplexSubbiomes() {return moreComplexSubbiomes;}
    
    public final void setComplexSubBiomes() {moreComplexSubbiomes = true;}

    public void disallowRivers(int biomeID) {
        if (biomeID == -1 ) return;
        riversDisallowed[biomeID] = true;
    }

    public void allowVillages(int biomeID) {
        if (biomeID == -1 ) return;
        hasVillages[biomeID] = true;
    }


    public void disallowStoneBeach(int biomeID) {
        if (biomeID == -1 ) return;
        stoneBeachDisallowed[biomeID] = true;
    }

    public void noBeaches(int biomeID) {
        if (biomeID == -1 ) return;
        noBeaches[biomeID] = true;
    }
}
