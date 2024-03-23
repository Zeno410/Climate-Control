
package climateControl.generator;

import climateControl.ClimateControl;
import climateControl.api.ClimateControlRules;
import climateControl.api.ClimateControlSettings;
import climateControl.api.BiomeSettings;
import climateControl.biomeSettings.BoPSettings;
import climateControl.customGenLayer.GenLayerBiomeByClimate;
import climateControl.customGenLayer.GenLayerCache;
import climateControl.customGenLayer.GenLayerRandomBiomes;
import climateControl.customGenLayer.GenLayerShoreCC;
import climateControl.customGenLayer.GenLayerSmoothClimate;
import climateControl.customGenLayer.GenLayerSubBiome;
import climateControl.customGenLayer.GenLayerTemperClimate;
import climateControl.genLayerPack.GenLayerAddIsland;
import climateControl.genLayerPack.GenLayerEdge;
import climateControl.genLayerPack.GenLayerHillsOneSix;
import climateControl.genLayerPack.GenLayerOneSixBiome;
import climateControl.genLayerPack.GenLayerRareBiome;
import climateControl.genLayerPack.GenLayerRiver;
import climateControl.genLayerPack.GenLayerRiverInit;
import climateControl.genLayerPack.GenLayerSmooth;
import climateControl.genLayerPack.GenLayerSwampRivers;
import climateControl.genLayerPack.GenLayerVoronoiZoom;
import climateControl.genLayerPack.GenLayerZoom;
import java.util.ArrayList;
import java.util.List;

import com.Zeno410Utils.Accessor;

import net.minecraft.init.Biomes;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerBiomeEdge;
import net.minecraft.world.gen.layer.GenLayerRiverMix;
import net.minecraft.world.gen.structure.MapGenVillage;

/**
 *
 * @author Zeno410
 */
abstract public class AbstractWorldGenerator {
    private final ClimateControlSettings settings;
    public ClimateControlSettings settings() {return settings;}


    protected final SubBiomeChooser subBiomeChooser = new SubBiomeChooser();
    protected final BiomeSwapper mBiomeChooser = new BiomeSwapper();
    private ClimateControlRules rules;

    public AbstractWorldGenerator(ClimateControlSettings settings) {
        this.settings = settings;
        setRules();
    }
    
    protected GenLayer smoothClimates(ClimateControlSettings settings, long worldSeed, GenLayer parent,long masterSeed) {
        return new GenLayerSmoothClimate(masterSeed,parent,settings);
    }

    protected ClimateControlRules rules() {return rules;}

    protected void setRules() {
        ClimateControlRules newRules = new ClimateControlRules();
        
        Accessor<Biome,Float> biomeTemperature= new Accessor<>("field_76750_F");
        if (settings.complexSubbiomes.value()) {
        	newRules.setComplexSubBiomes();
        	//Biomes.EXTREME_HILLS.
        	// set extreme Hills to temp 0.4
        	biomeTemperature.setField(Biomes.EXTREME_HILLS, 0.24f);
        	biomeTemperature.setField(Biomes.EXTREME_HILLS_EDGE, 0.24f);
        	biomeTemperature.setField(Biomes.EXTREME_HILLS_WITH_TREES, 0.24f);
        	biomeTemperature.setField(Biomes.MUTATED_EXTREME_HILLS, 0.24f);
        	biomeTemperature.setField(Biomes.MUTATED_EXTREME_HILLS_WITH_TREES, 0.24f);
        	
        } else {
        	// set extreme Hills to temp 0.2
        	biomeTemperature.setField(Biomes.EXTREME_HILLS, 0.2f);
        	biomeTemperature.setField(Biomes.EXTREME_HILLS_EDGE, 0.2f);
        	biomeTemperature.setField(Biomes.EXTREME_HILLS_WITH_TREES, 0.2f);
        	biomeTemperature.setField(Biomes.MUTATED_EXTREME_HILLS, 0.2f);
        	biomeTemperature.setField(Biomes.MUTATED_EXTREME_HILLS_WITH_TREES, 0.2f);
        }
        
        for (BiomeSettings biomeSettings: settings().biomeSettings()) {
            biomeSettings.setRules(newRules);
        }
        List<Biome> villageSpawnBiomes = new ArrayList<Biome>();
        for (int i = 0 ; i < 256;i++) {
            if (newRules.hasVillages(i)) {
                villageSpawnBiomes.add(Biome.getBiome(i));
            }
        }
        VillageBiomes villageBiomes = new VillageBiomes(villageSpawnBiomes);
        villageBiomes.reportMembers();
        if (settings.controlVillageBiomes.value()) {
            MapGenVillage.VILLAGE_SPAWN_BIOMES = villageBiomes;
        }
        rules = newRules;
    }

    abstract GenLayerRiverMix fromSeed(long worldSeed, WorldType worldType);

    public int rtgAwareRiverReduction(int baseReduction, WorldType worldType) {
        if (worldType.getName().equalsIgnoreCase("RTG")) return 100;
        return baseReduction;
    }
    
    public GenLayerRiverMix vanillaExpansion(long worldSeed, WorldType par2WorldType,
        GenLayer genlayer3,ClimateControlSettings settings){
        if (settings.oneSixCompatibility.value()) {
            return this.oneSixExpansion(worldSeed, par2WorldType, genlayer3, settings);
        } else {
            return this.oneSevenExpansion(worldSeed, par2WorldType, genlayer3, settings);
        }
    }

    public GenLayerRiverMix oneSevenExpansion(long worldSeed, WorldType par2WorldType,
            GenLayer genlayer3,ClimateControlSettings settings){
        byte b0 = settings().biomeSize.value().byteValue();

        GenLayer genlayer = GenLayerZoom.magnify(1000L, genlayer3, 0);
        GenLayerRiverInit genlayerriverinit = new GenLayerRiverInit(100L, genlayer);
        GenLayer object = null;

        if (settings().randomBiomes.value()) {
            object = new GenLayerRandomBiomes(worldSeed,genlayer3,settings);
        } else {
            object = new GenLayerBiomeByClimate(worldSeed,genlayer3,settings);
        }

        object.initWorldGenSeed(worldSeed);
        object = GenLayerZoom.magnify(1000L, object, 2);
        object = new GenLayerBiomeEdge(1000L, object);

        GenLayer genlayer1 = GenLayerZoom.magnify(1000L, genlayerriverinit, 2);
        for (BiomeSettings setting: settings.biomeSettings()) {
        	// note the default routine just returns the passed GenLayer
        	genlayer1 = setting.subBiomeEdges(1030, genlayer1, settings.maximumBiomes());
        }
        GenLayer genlayerhills = new GenLayerSubBiome(1000L, object, genlayer1,subBiomeChooser,mBiomeChooser,
                settings().doBoPSubBiomes());

        genlayerhills.initWorldGenSeed(worldSeed);        genlayer = GenLayerZoom.magnify(1000L, genlayerriverinit, 2);
        genlayer = GenLayerZoom.magnify(1000L, genlayer, b0);
        GenLayerRiver genlayerriver = new GenLayerRiver(1L, genlayer);
        GenLayerSmooth genlayersmooth = new GenLayerSmooth(1000L, genlayerriver);
        object = new GenLayerRareBiome(1001L, genlayerhills);

        for (int j = 0; j < b0; ++j)
        {
            object = new GenLayerZoom((long)(1000 + j), (GenLayer)object);

            if (j == 0)
            {
                for (BiomeSettings setting: settings.biomeSettings()) {
                	// note the default routine just returns the passed GenLayer
                	object = setting.subBiomeEdges(1030, object, settings.maximumBiomes());
                }
                object = new GenLayerAddIsland(3L, (GenLayer)object);
            }

            if (j == 1)
            {
                object = new GenLayerShoreCC(1000L, (GenLayer)object,this.rules);
            }
        }

        GenLayerSmooth genlayersmooth1 = new GenLayerSmooth(1000L, (GenLayer)object);
        GenLayerRiverMix genlayerrivermix = new GenLayerRiverMix(100L, genlayersmooth1, genlayersmooth);
        GenLayerVoronoiZoom genlayervoronoizoom = new GenLayerVoronoiZoom(10L, genlayerrivermix);
        genlayerrivermix.initWorldGenSeed(worldSeed);
        genlayervoronoizoom.initWorldGenSeed(worldSeed);
        genlayerrivermix.initWorldGenSeed(worldSeed);
        genlayervoronoizoom.initWorldGenSeed(worldSeed);
        return genlayerrivermix;
    }

    public GenLayerRiverMix oneSixExpansion(long worldSeed, WorldType par2WorldType,
            GenLayer genlayeraddmushroomisland,ClimateControlSettings settings){


        byte b0 = settings().biomeSize.value().byteValue();

        GenLayer genlayer = GenLayerZoom.magnify(1000L, genlayeraddmushroomisland, 0);
        GenLayerRiverInit genlayerriverinit = new GenLayerRiverInit(100L, genlayer);
        genlayer = GenLayerZoom.magnify(1000L, genlayerriverinit, b0 + 2);
        GenLayerRiver genlayerriver = new GenLayerRiver(1L, genlayer);
        GenLayerSmooth genlayersmooth = new GenLayerSmooth(1000L, genlayerriver);
        GenLayer genlayer1 = GenLayerZoom.magnify(1000L, genlayeraddmushroomisland, 0);
        GenLayer genlayerbiome = new GenLayerOneSixBiome(200L, genlayer1, WorldType.DEFAULT);
        genlayer1 = GenLayerZoom.magnify(1000L, genlayerbiome, 2);
        Object object = new GenLayerHillsOneSix(1000L, genlayer1);

        for (int j = 0; j < b0; ++j)
        {
            object = new GenLayerZoom((long)(1000 + j), (GenLayer)object);

            if (j == 0)
            {
                object = new GenLayerAddIsland(3L, (GenLayer)object);
            }

            if (j == 1)
            {
                object = new GenLayerShoreCC(1000L, (GenLayer)object,this.rules);
            }

            if (j == 1)
            {
                object = new GenLayerSwampRivers(1000L, (GenLayer)object);
            }
        }

        GenLayerSmooth genlayersmooth1 = new GenLayerSmooth(1000L, (GenLayer)object);
        GenLayerRiverMix genlayerrivermix = new GenLayerRiverMix(100L, genlayersmooth1, genlayersmooth);
        GenLayerVoronoiZoom genlayervoronoizoom = new GenLayerVoronoiZoom(10L, genlayerrivermix);
        genlayerrivermix.initWorldGenSeed(worldSeed);
        genlayervoronoizoom.initWorldGenSeed(worldSeed);
        return genlayerrivermix;
    }
}
