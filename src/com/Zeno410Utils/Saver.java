
package com.Zeno410Utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.HashMap;

import java.util.logging.Logger;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
/**
 *
 * @author Zeno410
 */
public class Saver<Type extends SelfTrackable<Type>> {
    //private final Maker<Type> maker;
    //public static Logger logger = new Zeno410Logger("Saver").logger();
    private final Streamer<Type> streamer;
    private Accessor<MapStorage,HashMap> mapAccess = new Accessor<MapStorage,HashMap>("field_75749_b");
    private Accessor<MapStorage,ISaveHandler> handlerAccess = new Accessor<MapStorage,ISaveHandler>("field_75751_a");
    public Saver(Streamer<Type> streamer) {
        this.streamer = streamer;
        assert(streamer != null);
        String test = streamer.toString();
    }

    public Streamer<Type> streamer() {return streamer;}

    public Savee<Type> savee(String saveName, World world, Maker<Type> maker, boolean saveNew) {
        MapStorage storage = world.getMapStorage();
        @SuppressWarnings("unchecked")
        Savee<Type> worldsaveddata = (Savee<Type>)mapAccess.get(storage).get(saveName);

        if (worldsaveddata != null) {
            return worldsaveddata;
        } else {
            ISaveHandler saveHandler = handlerAccess.get(storage);
            if (saveHandler != null){

                try {
                    File file1 = saveHandler.getMapFileFromName(saveName);
                    //logger.info(file1.getAbsolutePath());

                    if (file1 != null && file1.exists()) {
                        // object already saved to disk
                        worldsaveddata = new ItsSavee(saveName);
                        FileInputStream fileinputstream = new FileInputStream(file1);
                        NBTTagCompound nbttagcompound = CompressedStreamTools.readCompressed(fileinputstream);
                        fileinputstream.close();
                        worldsaveddata.readFromNBT(nbttagcompound.getCompoundTag("data"));
                    } else {
                        // object must be created
                        worldsaveddata = new ItsSavee(saveName,maker.item(),saveNew);
                        worldsaveddata.setDirty(saveNew);
                    }
                }
                catch (Exception exception1){
                    exception1.printStackTrace();
                    throw new RuntimeException(exception1);
                }
            } else {
                //ConfigManager.logger.info("no storage for "+storage.toString());
            }

        }

        if (worldsaveddata == null) {
            // object must be created
            worldsaveddata = new ItsSavee(saveName,maker.item(),saveNew);
            worldsaveddata.setDirty(saveNew);
            throw new RuntimeException();

        }
        if (worldsaveddata != null) {
            storage.setData(saveName, worldsaveddata);
            worldsaveddata.setDirty(true);
            storage.saveAllData();

        } else {
        }

        return worldsaveddata;
    }

    private class ItsSavee extends Savee<Type> {

        ItsSavee(String saveName) {
            super(saveName);
        }

        ItsSavee(String saveName, Type type, boolean saveNew) {
            super(saveName, type, saveNew);
        }

        Streamer<Type> streamer() {return Saver.this.streamer();}
    }

}