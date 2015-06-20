package climateControl.genLayerPack;

public class GenLayerFuzzyZoom extends GenLayerZoom
{
    private static final String __OBFID = "CL_00000556";

    public GenLayerFuzzyZoom(long par1, GenLayerPack par3GenLayer)
    {
        super(par1, par3GenLayer);
    }

    /**
     * returns the most frequently occurring number of the set, or a random number from those provided
     */
    protected int selectModeOrRandom(int p_151617_1_, int p_151617_2_, int p_151617_3_, int p_151617_4_)
    {
        return this.selectRandom(new int[] {p_151617_1_, p_151617_2_, p_151617_3_, p_151617_4_});
    }
}