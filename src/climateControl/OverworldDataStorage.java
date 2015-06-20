package climateControl;

import climateControl.utils.Acceptor;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldSavedData;

import net.minecraft.world.World;
import net.minecraft.world.WorldType;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;


import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.*;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.eventhandler.EventBus;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

/**
 * This class stores data in a file in a Minecraft Overworld (dimension 0)
 * It can also store information for an entire Minecraft universde, as long as it's
 * guaranteed to have a dimension 0.
 * @author Zeno410
 */
public class OverworldDataStorage<DataType extends WorldSavedData> {

    DataType dataToStore;
    Default<DataType> defaultData;
    Acceptor<DataType> target;
    String baseFileName;
    public OverworldDataStorage(String baseFileName, DataType dataToStore,
            Default<DataType> defaultData, Acceptor<DataType> target) {
        this.baseFileName = baseFileName;
        this.dataToStore = dataToStore;
        this.defaultData = defaultData;
        this.target = target;

        // check nothing is null
        baseFileName.toString();
        dataToStore.toString();
        defaultData.toString();

    }

    public void onWorldLoad(WorldEvent.Load event) {
        World world = event.world;
        if (world.isRemote) return;
        int dimension = world.provider.dimensionId;
        if (dimension != 0) return; // using the Overworld to define things  - for now

        DimensionalDataStorage<DataType> storage =
                new DimensionalDataStorage<DataType>(world.getSaveHandler(),baseFileName,dataToStore);
        DataType data = storage.load(0, defaultData.item());
        target.accept(data);
    }

    public void onWorldSave(WorldEvent.Save event, DataType toStore){
        World world = event.world;
        if (world.isRemote) return;
        int dimension = world.provider.dimensionId;
        if (dimension != 0) return; // using the Overworld to define things  - for now
        dataToStore = toStore;
        DimensionalDataStorage<DataType> storage =
                new DimensionalDataStorage<DataType>(world.getSaveHandler(),baseFileName,dataToStore);
        storage.save(this.dataToStore, 0);
    }

}
