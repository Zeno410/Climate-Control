
package climateControl;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.common.eventhandler.Event.Result;
import fwg.deco.DecoDungeons;
import fwg.deco.old.OldGenClay;
import fwg.deco.old.OldGenLakes;
import fwg.deco.old.OldGenMinable;
import fwg.map.MapGenOLD;
import fwg.map.MapGenOLDCaves;
import fwg.noise.NoiseOctavesBeta;
import fwg.world.ManagerFWG;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.BlockSand;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.SpawnerAnimals;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import net.minecraft.world.gen.feature.WorldGenCactus;
import net.minecraft.world.gen.feature.WorldGenDeadBush;
import net.minecraft.world.gen.feature.WorldGenFire;
import net.minecraft.world.gen.feature.WorldGenFlowers;
import net.minecraft.world.gen.feature.WorldGenHellLava;
import net.minecraft.world.gen.feature.WorldGenLiquids;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenPumpkin;
import net.minecraft.world.gen.feature.WorldGenTallGrass;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.ChunkProviderEvent;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.terraingen.TerrainGen;

public class ChunkGeneratorCCSkyDimension implements IChunkProvider
{
    private Random rand;
    private NoiseOctavesBeta field_912_k;
    private NoiseOctavesBeta field_911_l;
    private NoiseOctavesBeta field_910_m;
    private NoiseOctavesBeta field_909_n;
    private NoiseOctavesBeta field_908_o;
    public NoiseOctavesBeta field_922_a;
    public NoiseOctavesBeta field_921_b;
    public NoiseOctavesBeta mobSpawnerNoise;

    private World worldObj;
    private final boolean mapFeaturesEnabled;
    private double field_4180_q[];
    private double sandNoise[];
    private double gravelNoise[];
    private double stoneNoise[];
    private MapGenOLD field_902_u;
    private BiomeGenBase biomesForGeneration[];
	private int worldtype;

    double field_4185_d[];
    double field_4184_e[];
    double field_4183_f[];
    double field_4182_g[];
    double field_4181_h[];
    int field_914_i[][];
    private double generatedTemperatures[];
	private boolean nether;

	private WorldGenMinable ore_dirt = new WorldGenMinable(Blocks.dirt, 32);
	private WorldGenMinable ore_gravel = new WorldGenMinable(Blocks.gravel, 32);
	private WorldGenMinable ore_coal = new WorldGenMinable(Blocks.coal_ore, 16);
	private WorldGenMinable ore_iron = new WorldGenMinable(Blocks.iron_ore, 8);
	private WorldGenMinable ore_gold = new WorldGenMinable(Blocks.gold_ore, 8);
	private WorldGenMinable ore_redstone = new WorldGenMinable(Blocks.redstone_ore, 7);
	private WorldGenMinable ore_diamond = new WorldGenMinable(Blocks.diamond_ore, 7);
	private WorldGenMinable ore_lapis = new WorldGenMinable(Blocks.lapis_ore, 6);

	private Block[] tempBlocks;
	private byte[] tempMetadata;

    public ChunkGeneratorCCSkyDimension(World world, long l, boolean par4, int worldID, int noiseStyle)
    {
        sandNoise = new double[256];
        gravelNoise = new double[256];
        stoneNoise = new double[256];
        field_902_u = new MapGenOLDCaves();
        field_914_i = new int[32][32];
        worldObj = world;
        rand = new Random(l);
        mapFeaturesEnabled = par4;
		worldtype = worldID;
        field_912_k = new NoiseOctavesBeta(rand, 16);
        field_911_l = new NoiseOctavesBeta(rand, 16);
        field_910_m = new NoiseOctavesBeta(rand, 8);
        field_909_n = new NoiseOctavesBeta(rand, 4);
        field_908_o = new NoiseOctavesBeta(rand, 4);
        field_922_a = new NoiseOctavesBeta(rand, 10);
        field_921_b = new NoiseOctavesBeta(rand, 16);
        mobSpawnerNoise = new NoiseOctavesBeta(rand, 8);
    	tempBlocks = new Block[65536];
    	tempMetadata = new byte[65536];
    }

    public void generateTerrain(int i, int j, Block blocks[], BiomeGenBase abiomegenbase[], double ad[])
    {
        byte byte0 = 2;
        int k = byte0 + 1;
        byte byte1 = 33;
        int l = byte0 + 1;
        field_4180_q = func_4061_a(field_4180_q, i * byte0, 0, j * byte0, k, byte1, l);
        for(int i1 = 0; i1 < byte0; i1++)
        {
            for(int j1 = 0; j1 < byte0; j1++)
            {
                for(int k1 = 0; k1 < 32; k1++)
                {
                    double d = 0.25D;
                    double d1 = field_4180_q[((i1 + 0) * l + (j1 + 0)) * byte1 + (k1 + 0)];
                    double d2 = field_4180_q[((i1 + 0) * l + (j1 + 1)) * byte1 + (k1 + 0)];
                    double d3 = field_4180_q[((i1 + 1) * l + (j1 + 0)) * byte1 + (k1 + 0)];
                    double d4 = field_4180_q[((i1 + 1) * l + (j1 + 1)) * byte1 + (k1 + 0)];
                    double d5 = (field_4180_q[((i1 + 0) * l + (j1 + 0)) * byte1 + (k1 + 1)] - d1) * d;
                    double d6 = (field_4180_q[((i1 + 0) * l + (j1 + 1)) * byte1 + (k1 + 1)] - d2) * d;
                    double d7 = (field_4180_q[((i1 + 1) * l + (j1 + 0)) * byte1 + (k1 + 1)] - d3) * d;
                    double d8 = (field_4180_q[((i1 + 1) * l + (j1 + 1)) * byte1 + (k1 + 1)] - d4) * d;
                    for(int l1 = 0; l1 < 4; l1++)
                    {
                        double d9 = 0.125D;
                        double d10 = d1;
                        double d11 = d2;
                        double d12 = (d3 - d1) * d9;
                        double d13 = (d4 - d2) * d9;
                        for(int i2 = 0; i2 < 8; i2++)
                        {
                            int j2 = i2 + i1 * 8 << 11 | 0 + j1 * 8 << 7 | k1 * 4 + l1;
                            char c = '\200';
                            double d14 = 0.125D;
                            double d15 = d10;
                            double d16 = (d11 - d10) * d14;
                            for(int k2 = 0; k2 < 8; k2++)
                            {
                                Block l2 = Blocks.air;
                                if(d15 > 0.0D)
                                {
									if(nether) { l2 = Blocks.netherrack; }
									else { l2 = Blocks.stone; }
                                }

                                blocks[j2] = l2;
                                j2 += c;
                                d15 += d16;
                            }

                            d10 += d12;
                            d11 += d13;
                        }

                        d1 += d5;
                        d2 += d6;
                        d3 += d7;
                        d4 += d8;
                    }

                }

            }

        }

    }

    public void replaceBlocksForBiome(int i, int j, Block blocks[], byte metadata[], BiomeGenBase abiomegenbase[])
    {

        ChunkProviderEvent.ReplaceBiomeBlocks event = new ChunkProviderEvent.ReplaceBiomeBlocks(this, i, j, blocks,  metadata, abiomegenbase);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.getResult() == Result.DENY) return;

        double d = 0.03125D;
        stoneNoise = field_908_o.generateNoiseOctaves(stoneNoise, i * 16, j * 16, 0.0D, 16, 16, 1, d * 2D, d * 2D, d * 2D);

        int k, l, m;
        for(k = 0; k < 16; k++)
        {
            for(l = 0; l < 16; l++)
            {
            	//THIS IS THE SHITTIEST SOLUTION EVER, DEAL WITH IT!

            	for(m = 0; m < 256; m++)
            	{
            		if(m < 128)
            		{
            			tempBlocks[(l * 16 + k) * 256 + m + 64] = blocks[(l * 16 + k) * 128 + m];
                		tempMetadata[(l * 16 + k) * 256 + m + 64] = 0;
            		}
            		else
            		{
            			tempBlocks[m] = Blocks.air;
                		tempMetadata[(l * 16 + k) * 256 + m] = 0;
            		}
            	}

            	abiomegenbase[k + l * 16].genTerrainBlocks(worldObj, rand, tempBlocks, tempMetadata, i * 16 + k, j * 16 + l, stoneNoise[l + k * 16]);

            	for(m = 0; m < 128; m++)
            	{
            		blocks[(l * 16 + k) * 128 + m] = tempBlocks[(l * 16 + k) * 256 + m + 64];
            		metadata[(l * 16 + k) * 128 + m] = tempMetadata[(l * 16 + k) * 256 + m + 64];
            	}
            }
        }
    }

    public Chunk loadChunk(int par1, int par2)
    {
        return provideChunk(par1, par2);
    }

    public Chunk provideChunk(int i, int j)
    {
        rand.setSeed((long)i * 0x4f9939f508L + (long)j * 0x1ef1565bd5L);
        Block blocks[] = new Block[32768];
        byte metadata[] = new byte[32768];
        biomesForGeneration = worldObj.getWorldChunkManager().loadBlockGeneratorData(biomesForGeneration, i * 16, j * 16, 16, 16);
        double ad[] = ManagerFWG.temperature;
        generateTerrain(i, j, blocks, biomesForGeneration, ad);
        replaceBlocksForBiome(i, j, blocks, metadata, biomesForGeneration);
        field_902_u.generate(this, worldObj, i, j, blocks);

        Chunk chunk = new Chunk(worldObj, blocks, metadata, i, j);
        byte abyte1[] = chunk.getBiomeArray();

        for (int k = 0; k < abyte1.length; k++)
        {
            abyte1[k] = (byte)biomesForGeneration[k].biomeID;
        }

        chunk.generateSkylightMap();
        return chunk;
    }

    private double[] func_4061_a(double ad[], int i, int j, int k, int l, int i1, int j1)
    {
        if(ad == null)
        {
            ad = new double[l * i1 * j1];
        }
        double d = 684.41200000000003D;
        double d1 = 684.41200000000003D;
        double ad1[] = ManagerFWG.temperature;
        double ad2[] = ManagerFWG.humidity;
        field_4182_g = field_922_a.generateNoiseOctaves(field_4182_g, i, k, l, j1, 1.121D, 1.121D, 0.5D);
        field_4181_h = field_921_b.generateNoiseOctaves(field_4181_h, i, k, l, j1, 200D, 200D, 0.5D);
        d *= 2D;
        field_4185_d = field_910_m.generateNoiseOctaves(field_4185_d, i, j, k, l, i1, j1, d / 80D, d1 / 160D, d / 80D);
        field_4184_e = field_912_k.generateNoiseOctaves(field_4184_e, i, j, k, l, i1, j1, d, d1, d);
        field_4183_f = field_911_l.generateNoiseOctaves(field_4183_f, i, j, k, l, i1, j1, d, d1, d);
        int k1 = 0;
        int l1 = 0;
        int i2 = 16 / l;
        for(int j2 = 0; j2 < l; j2++)
        {
            int k2 = j2 * i2 + i2 / 2;
            for(int l2 = 0; l2 < j1; l2++)
            {
                int i3 = l2 * i2 + i2 / 2;
				double d3;
				if(!nether)
				{
					double d2 = ad1[k2 * 16 + i3];
					d3 = ad2[k2 * 16 + i3] * d2;
				}
				else
				{
					d3 = 0.5D;
				}
                double d4 = 1.0D - d3;
                d4 *= d4;
                d4 *= d4;
                d4 = 1.0D - d4;
                double d5 = (field_4182_g[l1] + 256D) / 512D;
                d5 *= d4;
                if(d5 > 1.0D)
                {
                    d5 = 1.0D;
                }
                double d6 = field_4181_h[l1] / 8000D;
                if(d6 < 0.0D)
                {
                    d6 = -d6 * 0.29999999999999999D;
                }
                d6 = d6 * 3D - 2D;
                if(d6 > 1.0D)
                {
                    d6 = 1.0D;
                }
                d6 /= 8D;
                d6 = 0.0D;
                if(d5 < 0.0D)
                {
                    d5 = 0.0D;
                }
                d5 += 0.5D;
                d6 = (d6 * (double)i1) / 16D;
                l1++;
                double d7 = (double)i1 / 2D;
                for(int j3 = 0; j3 < i1; j3++)
                {
                    double d8 = 0.0D;
                    double d9 = (((double)j3 - d7) * 8D) / d5;
                    if(d9 < 0.0D)
                    {
                        d9 *= -1D;
                    }
                    double d10 = field_4184_e[k1] / 512D;
                    double d11 = field_4183_f[k1] / 512D;
                    double d12 = (field_4185_d[k1] / 10D + 1.0D) / 2D;
                    if(d12 < 0.0D)
                    {
                        d8 = d10;
                    } else
                    if(d12 > 1.0D)
                    {
                        d8 = d11;
                    } else
                    {
                        d8 = d10 + (d11 - d10) * d12;
                    }
                    d8 -= 8D;
                    int k3 = 32;
                    if(j3 > i1 - k3)
                    {
                        double d13 = (float)(j3 - (i1 - k3)) / ((float)k3 - 1.0F);
                        d8 = d8 * (1.0D - d13) + -30D * d13;
                    }
                    k3 = 8;
                    if(j3 < k3)
                    {
                        double d14 = (float)(k3 - j3) / ((float)k3 - 1.0F);
                        d8 = d8 * (1.0D - d14) + -30D * d14;
                    }
                    ad[k1] = d8;
                    k1++;
                }

            }

        }

        return ad;
    }

    public boolean chunkExists(int par1, int par2)
    {
        return true;
    }

    public void populate(IChunkProvider ichunkprovider, int i, int j)
    {
        BlockFalling.fallInstantly = true;
        int x = i * 16;
        int y = j * 16;
        BiomeGenBase biomegenbase = worldObj.getWorldChunkManager().getBiomeGenAt(x + 16, y + 16);
        this.rand.setSeed(this.worldObj.getSeed());
        long i1 = this.rand.nextLong() / 2L * 2L + 1L;
        long j1 = this.rand.nextLong() / 2L * 2L + 1L;
        this.rand.setSeed((long)i * i1 + (long)j * j1 ^ this.worldObj.getSeed());
        boolean flag = false;

        MinecraftForge.EVENT_BUS.post(new PopulateChunkEvent.Pre(ichunkprovider, worldObj, rand, i, j, flag));

        boolean gen = false;

        gen = TerrainGen.populate(this, worldObj, rand, i, j, flag, PopulateChunkEvent.Populate.EventType.LAKE);
		if(rand.nextInt(4) == 0)
		{
			int ii1 = x + rand.nextInt(16) + 8;
			int l4 = rand.nextInt(128);
			int i8 = y + rand.nextInt(16) + 8;
			(new OldGenLakes(Blocks.water)).generate(worldObj, rand, ii1, l4, i8);
		}

        gen = TerrainGen.populate(this, worldObj, rand, i, j, flag, PopulateChunkEvent.Populate.EventType.LAVA);
		if(rand.nextInt(8) == 0)
		{
			int jj1 = x + rand.nextInt(16) + 8;
			int i5 = rand.nextInt(rand.nextInt(120) + 8);
			int j8 = y + rand.nextInt(16) + 8;
			if(i5 < 64 || rand.nextInt(10) == 0)
			{
				(new OldGenLakes(Blocks.lava)).generate(worldObj, rand, jj1, i5, j8);
			}
		}

		gen = TerrainGen.populate(this, worldObj, rand, i, j, flag, PopulateChunkEvent.Populate.EventType.DUNGEON);
		if(rand.nextInt(30) == 0)
		{
			int j5 = x + rand.nextInt(16) + 8;
			int k8 = rand.nextInt(15);
			int j11 = y + rand.nextInt(16) + 8;
			if(rand.nextInt(8) == 0)
			{
				(new DecoDungeons(1, false, false, false, true)).generate(worldObj, rand, j5, k8, j11);
			}
			else
			{
				(new DecoDungeons(1, false, false, true, false)).generate(worldObj, rand, j5, k8, j11);
			}
		}

		MinecraftForge.ORE_GEN_BUS.post(new OreGenEvent.Pre(worldObj, rand, x, y));

		if (TerrainGen.generateOre(worldObj, rand, ore_dirt, x, y, OreGenEvent.GenerateMinable.EventType.DIRT))
		{
			for(int j2 = 0; j2 < 10; j2++)
			{
				int l5 = x + rand.nextInt(16);
				int i9 = rand.nextInt(64);
				int l11 = y + rand.nextInt(16);
				ore_dirt.generate(worldObj, rand, l5, i9, l11);
			}
		}

		if (TerrainGen.generateOre(worldObj, rand, ore_gravel, x, y, OreGenEvent.GenerateMinable.EventType.GRAVEL))
		{
			for(int k2 = 0; k2 < 5; k2++)
			{
				int i6 = x + rand.nextInt(16);
				int j9 = rand.nextInt(64);
				int i12 = y + rand.nextInt(16);
				ore_gravel.generate(worldObj, rand, i6, j9, i12);
			}
		}

		if (TerrainGen.generateOre(worldObj, rand, ore_coal, x, y, OreGenEvent.GenerateMinable.EventType.COAL))
		{
			for(int i3 = 0; i3 < 20; i3++)
			{
				int j6 = x + rand.nextInt(16);
				int k9 = rand.nextInt(128);
				int j12 = y + rand.nextInt(16);
				ore_coal.generate(worldObj, rand, j6, k9, j12);
			}
		}

		if (TerrainGen.generateOre(worldObj, rand, ore_iron, x, y, OreGenEvent.GenerateMinable.EventType.IRON))
		{
			for(int j3 = 0; j3 < 20; j3++)
			{
				int k6 = x + rand.nextInt(16);
				int l9 = rand.nextInt(64);
				int k12 = y + rand.nextInt(16);
				ore_iron.generate(worldObj, rand, k6, l9, k12);
			}
		}

		if (TerrainGen.generateOre(worldObj, rand, ore_gold, x, y, OreGenEvent.GenerateMinable.EventType.GOLD))
		{
			for(int k3 = 0; k3 < 2; k3++)
			{
				int l6 = x + rand.nextInt(16);
				int i10 = rand.nextInt(32) + 8;
				int l12 = y + rand.nextInt(16);
				ore_gold.generate(worldObj, rand, l6, i10, l12);
			}
		}

		if (TerrainGen.generateOre(worldObj, rand, ore_redstone, x, y, OreGenEvent.GenerateMinable.EventType.REDSTONE))
		{
			for(int l3 = 0; l3 < 8; l3++)
			{
				int i7 = x + rand.nextInt(16);
				int j10 = rand.nextInt(16) + 8;
				int i13 = y + rand.nextInt(16);
				ore_redstone.generate(worldObj, rand, i7, j10, i13);
			}
		}

		if (TerrainGen.generateOre(worldObj, rand, ore_diamond, x, y, OreGenEvent.GenerateMinable.EventType.DIAMOND))
		{
			for(int i4 = 0; i4 < 1; i4++)
			{
				int j7 = x + rand.nextInt(16);
				int k10 = rand.nextInt(16) + 8;
				int j13 = y + rand.nextInt(16);
				ore_diamond.generate(worldObj, rand, j7, k10, j13);
			}
		}

		if (TerrainGen.generateOre(worldObj, rand, ore_lapis, x, y, OreGenEvent.GenerateMinable.EventType.LAPIS))
		{
			for(int j4 = 0; j4 < 1; j4++)
			{
				int k7 = x + rand.nextInt(16);
				int l10 = rand.nextInt(16) + rand.nextInt(16) + 8;
				int k13 = y + rand.nextInt(16);
				ore_lapis.generate(worldObj, rand, k7, l10, k13);
			}
		}

        for (int g12 = 0; g12 < 4; ++g12)
        {
            int n1 = x + rand.nextInt(16);
            int m1 = rand.nextInt(28) + 14;
            int p1 = y + rand.nextInt(16);

            if (worldObj.getBlock(n1, m1, p1).isReplaceableOreGen(worldObj, n1, m1, p1, Blocks.stone))
            {
            	worldObj.setBlock(n1, m1, p1, Blocks.emerald_ore, 0, 2);
            }
        }

        MinecraftForge.ORE_GEN_BUS.post(new OreGenEvent.Post(worldObj, rand, x, y));

        MinecraftForge.EVENT_BUS.post(new DecorateBiomeEvent.Pre(worldObj, rand, x, y));

        //lazy fix
        TerrainGen.decorate(worldObj, rand, x, y, DecorateBiomeEvent.Decorate.EventType.SAND);
        TerrainGen.decorate(worldObj, rand, x, y, DecorateBiomeEvent.Decorate.EventType.CLAY);
        TerrainGen.decorate(worldObj, rand, x, y, DecorateBiomeEvent.Decorate.EventType.SAND_PASS2);
        TerrainGen.decorate(worldObj, rand, x, y, DecorateBiomeEvent.Decorate.EventType.TREE);
        TerrainGen.decorate(worldObj, rand, x, y, DecorateBiomeEvent.Decorate.EventType.BIG_SHROOM);

		biomegenbase.decorate(this.worldObj, this.worldObj.rand, x, y);

        //lazy fix
        TerrainGen.decorate(worldObj, rand, x, y, DecorateBiomeEvent.Decorate.EventType.FLOWERS);
        TerrainGen.decorate(worldObj, rand, x, y, DecorateBiomeEvent.Decorate.EventType.GRASS);
        TerrainGen.decorate(worldObj, rand, x, y, DecorateBiomeEvent.Decorate.EventType.DEAD_BUSH);
        TerrainGen.decorate(worldObj, rand, x, y, DecorateBiomeEvent.Decorate.EventType.LILYPAD);
        TerrainGen.decorate(worldObj, rand, x, y, DecorateBiomeEvent.Decorate.EventType.SHROOM);
        TerrainGen.decorate(worldObj, rand, x, y, DecorateBiomeEvent.Decorate.EventType.REED);
        TerrainGen.decorate(worldObj, rand, x, y, DecorateBiomeEvent.Decorate.EventType.PUMPKIN);
        TerrainGen.decorate(worldObj, rand, x, y, DecorateBiomeEvent.Decorate.EventType.CACTUS);
        TerrainGen.decorate(worldObj, rand, x, y, DecorateBiomeEvent.Decorate.EventType.LAKE);

        MinecraftForge.EVENT_BUS.post(new DecorateBiomeEvent.Post(worldObj, rand, x, y));

		for(int l18 = 0; l18 < 50; l18++)
		{
			int l21 = x + rand.nextInt(16) + 8;
			int k23 = rand.nextInt(rand.nextInt(120) + 8);
			int l24 = y + rand.nextInt(16) + 8;
			(new WorldGenLiquids(Blocks.flowing_water)).generate(worldObj, rand, l21, k23, l24);
		}

		for(int i19 = 0; i19 < 20; i19++)
		{
			int i22 = x + rand.nextInt(16) + 8;
			int l23 = rand.nextInt(rand.nextInt(rand.nextInt(112) + 8) + 8);
			int i25 = y + rand.nextInt(16) + 8;
			(new WorldGenLiquids(Blocks.flowing_lava)).generate(worldObj, rand, i22, l23, i25);
		}

        if (TerrainGen.populate(this, worldObj, rand, i, j, flag, PopulateChunkEvent.Populate.EventType.ANIMALS))
        {
            SpawnerAnimals.performWorldGenSpawning(this.worldObj, worldObj.getBiomeGenForCoords(x + 16, y + 16), x + 8, y + 8, 16, 16, this.rand);
        }

        MinecraftForge.EVENT_BUS.post(new PopulateChunkEvent.Post(ichunkprovider, worldObj, rand, i, j, flag));

        TerrainGen.populate(this, worldObj, rand, i, j, flag, PopulateChunkEvent.Populate.EventType.ICE);

        x += 8;
        y += 8;

        for (int sn1 = 0; sn1 < 16; ++sn1)
        {
            for (int sn2 = 0; sn2 < 16; ++sn2)
            {
                int sn3 = worldObj.getPrecipitationHeight(x + sn1, y + sn2);

                /*if (worldObj.isBlockFreezable(sn1 + x, sn3 - 1, sn2 + y))
                {
                	worldObj.setBlock(sn1 + x, sn3 - 1, sn2 + y, Blocks.ice, 0, 2);
                }*/

                if(sn3 > 3)
                {
                    Block b = worldObj.getBlock(sn1 + x, sn3 - 1, sn2 + y);
                    if (worldObj.func_147478_e(sn1 + x, sn3, sn2 + y, false) && b != Blocks.ice && b != Blocks.water && sn3 > 3)
                    {
                        worldObj.setBlock(sn1 + x, sn3, sn2 + y, Blocks.snow_layer, 0, 2);
                    }
                }
            }
        }

        BlockFalling.fallInstantly = false;
    }

    public boolean saveChunks(boolean par1, IProgressUpdate par2IProgressUpdate)
    {
        return true;
    }

    public boolean unloadQueuedChunks()
    {
        return false;
    }

    public boolean unload100OldestChunks()
    {
        return false;
    }

    public boolean canSave()
    {
        return true;
    }

    public String makeString()
    {
        return "RandomLevelSource";
    }

    public List getPossibleCreatures(EnumCreatureType par1EnumCreatureType, int par2, int par3, int par4)
    {
        BiomeGenBase var5 = this.worldObj.getBiomeGenForCoords(par2, par4);
        return var5 == null ? null : var5.getSpawnableList(par1EnumCreatureType);
    }

    public ChunkPosition func_147416_a(World par1World, String par2Str, int par3, int par4, int par5)
    {
        return null;
    }

    public int getLoadedChunkCount()
    {
        return 0;
    }

    public void saveExtraData() {}

    public void recreateStructures(int par1, int par2)
    {
        if (mapFeaturesEnabled)
        {
		}
	}
}
