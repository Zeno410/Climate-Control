
package climateControl.api;

/**
 *
 * @author Zeno410
 */
public class ClimateControlRules {
    private boolean[] riversDisallowed = new boolean[256];
    private boolean[] stoneBeachDisallowed = new boolean[256];
    private boolean[] noBeaches = new boolean[256];
    private boolean[] hasVillages = new boolean[256];

    public final boolean riversAllowed(int biomeID ) {return !riversDisallowed[biomeID];}
    public final boolean riversDisallowed(int biomeID ) {return riversDisallowed[biomeID];}
    public final boolean stoneBeachAllowed(int biomeID ) {return !stoneBeachDisallowed[biomeID];}
    public final boolean stoneBeachDisallowed(int biomeID ) {return stoneBeachDisallowed[biomeID];}
    public final boolean noBeachesAllowed(int biomeID ) {return noBeaches[biomeID];}
    public final boolean beachesAllowed(int biomeID ) {return !noBeaches[biomeID];}
    public final boolean hasVillages(int biomeID ) {return hasVillages[biomeID];}

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
