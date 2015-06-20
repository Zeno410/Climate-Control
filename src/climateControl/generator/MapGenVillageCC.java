
package climateControl.generator;

/**
 *
 * @author Zeno410
 */

import climateControl.utils.Zeno410Logger;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import java.util.logging.Logger;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStart;
import net.minecraft.world.gen.structure.StructureVillagePieces;

public class MapGenVillageCC extends MapGenVillage {

    //public static Logger logger = new Zeno410Logger("MapGenVillage").logger();
   /**
     * World terrain type, 0 for normal, 1 for flat map
     */
    private int terrainType;
    private int field_82665_g;
    private int field_82666_h;
    long startTime;
    private long totalTime = 0;
    private int timesSinceReport = 0;
    private static final String __OBFID = "CL_00000514";

    public MapGenVillageCC()
    {
        this.field_82665_g = 32;
        this.field_82666_h = 8;
    }

    public MapGenVillageCC(Map par1Map)
    {
        this();
        Iterator iterator = par1Map.entrySet().iterator();

        while (iterator.hasNext())
        {
            Entry entry = (Entry)iterator.next();

            if (((String)entry.getKey()).equals("size"))
            {
                this.terrainType = MathHelper.parseIntWithDefaultAndMax((String)entry.getValue(), this.terrainType, 0);
            }
            else if (((String)entry.getKey()).equals("distance"))
            {
                this.field_82665_g = MathHelper.parseIntWithDefaultAndMax((String)entry.getValue(), this.field_82665_g, this.field_82666_h + 1);
            }
        }
    }

    @Override
    public String func_143025_a()
    {
        return "Village";
    }

    @Override
    protected boolean canSpawnStructureAtCoords(int par1, int par2)
    {
        startTime = System.currentTimeMillis();
        int k = par1;
        int l = par2;

        if (par1 < 0)
        {
            par1 -= this.field_82665_g - 1;
        }

        if (par2 < 0)
        {
            par2 -= this.field_82665_g - 1;
        }

        int i1 = par1 / this.field_82665_g;
        int j1 = par2 / this.field_82665_g;
        Random random = this.worldObj.setRandomSeed(i1, j1, 10387312);
        i1 *= this.field_82665_g;
        j1 *= this.field_82665_g;
        i1 += random.nextInt(this.field_82665_g - this.field_82666_h);
        j1 += random.nextInt(this.field_82665_g - this.field_82666_h);

        if (k == i1 && l == j1) {
            boolean flag = this.worldObj.getWorldChunkManager().areBiomesViable(k * 16 + 8, l * 16 + 8, 0, villageSpawnBiomes);

            if (flag){
                //logger.info("village: "+ (k * 16 + 8) + " " + (l * 16 + 8));
                reportTime() ;
                return true;
            }
        }

        //logger.info("nothing: "+ (k * 16 + 8) + " " + (l * 16 + 8));
        reportTime() ;
        return false;
    }
    private void reportTime() {
        totalTime += System.currentTimeMillis()-this.startTime;
        if (++timesSinceReport>1000) {
            //logger.info("total time"+totalTime);
            timesSinceReport =0;
        }
    }

    @Override
    protected StructureStart getStructureStart(int par1, int par2)
    {

        return new MapGenVillageCC.Start(this.worldObj, this.rand, par1, par2, this.terrainType);
    }

    public static class Start extends StructureStart
        {
            /**
             * well ... thats what it does
             */
            private boolean hasMoreThanTwoComponents;
            private static final String __OBFID = "CL_00000515";

            public Start() {}

            public Start(World par1World, Random par2Random, int par3, int par4, int par5)
            {
                super(par3, par4);
                List list = StructureVillagePieces.getStructureVillageWeightedPieceList(par2Random, par5);
                StructureVillagePieces.Start start = new StructureVillagePieces.Start(par1World.getWorldChunkManager(), 0, par2Random, (par3 << 4) + 2, (par4 << 4) + 2, list, par5);
                this.components.add(start);
                start.buildComponent(start, this.components, par2Random);
                List list1 = start.field_74930_j;
                List list2 = start.field_74932_i;
                int l;

                while (!list1.isEmpty() || !list2.isEmpty())
                {
                    StructureComponent structurecomponent;

                    if (list1.isEmpty())
                    {
                        l = par2Random.nextInt(list2.size());
                        structurecomponent = (StructureComponent)list2.remove(l);
                        structurecomponent.buildComponent(start, this.components, par2Random);
                    }
                    else
                    {
                        l = par2Random.nextInt(list1.size());
                        structurecomponent = (StructureComponent)list1.remove(l);
                        structurecomponent.buildComponent(start, this.components, par2Random);
                    }
                }

                this.updateBoundingBox();
                l = 0;
                Iterator iterator = this.components.iterator();

                while (iterator.hasNext())
                {
                    StructureComponent structurecomponent1 = (StructureComponent)iterator.next();

                    if (!(structurecomponent1 instanceof StructureVillagePieces.Road))
                    {
                        ++l;
                    }
                }

                this.hasMoreThanTwoComponents = l > 2;
            }

            /**
             * currently only defined for Villages, returns true if Village has more than 2 non-road components
             */
            public boolean isSizeableStructure()
            {
                return this.hasMoreThanTwoComponents;
            }

            public void func_143022_a(NBTTagCompound par1NBTTagCompound)
            {
                super.func_143022_a(par1NBTTagCompound);
                par1NBTTagCompound.setBoolean("Valid", this.hasMoreThanTwoComponents);
            }

            public void func_143017_b(NBTTagCompound par1NBTTagCompound)
            {
                super.func_143017_b(par1NBTTagCompound);
                this.hasMoreThanTwoComponents = par1NBTTagCompound.getBoolean("Valid");
            }
        }
}