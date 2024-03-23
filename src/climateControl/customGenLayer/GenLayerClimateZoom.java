package climateControl.customGenLayer;

import climateControl.genLayerPack.GenLayerZoom;
import net.minecraft.world.gen.layer.GenLayer;

public class GenLayerClimateZoom extends GenLayerZoom {
	
    public GenLayerClimateZoom(long par1, GenLayer par3GenLayer)
    {
        super(par1,par3GenLayer);
        super.parent = par3GenLayer;
    }
    /**
     * returns the most frequently occurring number of the set, or a random number from those provided
     */
    @Override
    public int selectModeOrRandom(int p1, int p2, int p3, int p4)
    {
    	// this is adjusted to not automatically prefer temperate climates surrounding extreme ones.
    	int icys = 0;
    	int temps = 0;
    	int hots = 0;
    	
    	{
    		int climate = climate(p1);
    		if (climate == 4) {icys ++;}
    		else if (climate ==1) {hots ++;}
    		else if (climate >0 ) {temps ++;}
    	}    	
    	{
    		int climate = climate(p2);
    		if (climate == 4) {icys ++;}
    		else if (climate ==1) {hots ++;}
    		else if (climate >0 ) {temps ++;}
    	}    	
    	{
    		int climate = climate(p3);
    		if (climate == 4) {icys ++;}
    		else if (climate ==1) {hots ++;}
    		else if (climate >0 ) {temps ++;}
    	}    	
    	{
    		int climate = climate(p4);
    		if (climate == 4) {icys ++;}
    		else if (climate ==1) {hots ++;}
    		else if (climate >0 ) {temps ++;}
    	}
    	
    	// if hot or snowy would be edited out by a preponderance of temperate return a random choice
    	if (icys>0||hots>0) {
    		// prefer bunching hots and snowys
    		if (icys >1&&hots<2) {
    			// return a snowy
    			if (climate(p1)==4) return p1;
    			if (climate(p2)==4) return p2;
    			if (climate(p3)==4) return p3;
    	    	if (1>0) throw new RuntimeException();
    		}
    		if (icys <2&&hots>1) {
    			// return a hot
    			if (climate(p1)==1) return p1;
    			if (climate(p2)==1) return p2;
    			if (climate(p3)==1) return p3;
    	    	if (1>0) throw new RuntimeException();
    		}
    		if (temps ==3) {
    			return this.selectRandom(new int[] {p1, p2, p3, p4});
    		}
    	}
        return p2 == p3 && p3 == p4 ? p2 : 
        	(p1 == p2 && p1 == p3 ? p1 : 
        		(p1 == p2 && p1 == p4 ? p1 : 
        			(p1 == p3 && p1 == p4 ? p1 : 
        				(p1 == p2 && p3 != p4 ? p1 : (p1 == p3 && p2 != p4 ? p1 : (p1 == p4 && p2 != p3 ? p1 : (p2 == p3 && p1 != p4 ? p2 : (p2 == p4 && p1 != p3 ? p2 : (p3 == p4 && p1 != p2 ? p3 : this.selectRandom(new int[] {p1, p2, p3, p4}))))))))));
    }

    private int climate(int taggedClimate) {
    	// strips out the  tagging info and just return climate number
    	if (taggedClimate == 0) return taggedClimate; // that's ocean, not tagged.
    	int climate = taggedClimate%4;
    	if (climate == 0) climate = 4; // hots were just moduloed to zero; put back
    	return climate;
    }
}
