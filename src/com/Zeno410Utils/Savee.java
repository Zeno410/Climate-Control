
package com.Zeno410Utils;
import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.World;


import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.WorldSavedData;
/**
 *
 * @author Zeno410
 */
public abstract class Savee<Type extends SelfTrackable<Type>> extends WorldSavedData {
    //public static Logger logger = new Zeno410Logger("Savee").logger();
    public final String DATA_NAME = "data";
    private final String saveName;
    private Type data;
    private Acceptor<Type> toSelf = new Acceptor<Type>() {
        public void accept(Type accepted) {
            set(accepted);
        }
    };

    private static Accessor<MapStorage,ISaveHandler> saveHandlerAccess =
            new Accessor<MapStorage,ISaveHandler>("field_75751_a");


    public void loadData(String saveName, Savee<Type> worldsaveddata, World world, Maker<Type> maker){
        final ISaveHandler saveHandler = saveHandlerAccess.get(world.getMapStorage());
        final MapStorage mapStorage = world.getMapStorage();
         if (saveHandler != null){
            try {
                File file1 = saveHandler.getMapFileFromName(saveName);

                if (file1 != null && file1.exists()) {
                    FileInputStream fileinputstream = new FileInputStream(file1);
                    NBTTagCompound nbttagcompound = CompressedStreamTools.readCompressed(fileinputstream);
                    fileinputstream.close();
                    worldsaveddata.readFromNBT(nbttagcompound.getCompoundTag("data"));
                }
            }
            catch (Exception exception1){
                exception1.printStackTrace();
            }
        }

        if (worldsaveddata.data() == null) {
            worldsaveddata.data = maker.item();
            worldsaveddata.data.informOnChange(toSelf);
        }

        if (worldsaveddata != null) {

            mapStorage.setData(mapName, worldsaveddata);
            worldsaveddata.setDirty(true);
            mapStorage.saveAllData();
        }
    }

    public Savee(String saveName) {
        super(saveName);
        this.saveName = saveName;
    }

    public Savee(String saveName,Type data) {
        super(saveName);
        this.saveName = saveName;
        set(data); // also dirties
        data.informOnChange(toSelf);
    }

    public Savee(String saveName,Type data, boolean startDirty) {
        super(saveName);
        this.saveName = saveName;
        if (startDirty)
            set(data); // also dirties
        else {
            this.data = data;
        }
        data.informOnChange(toSelf);
    }
    abstract Streamer<Type> streamer();


    public Type data() {return data;}

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        DataInput inputStream = new DataInputStream(
                new ByteArrayInputStream(nbttagcompound.getByteArray(DATA_NAME)));
        try {
            data = streamer().readFrom(inputStream);
            data.informOnChange(toSelf);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(72);
        DataOutput outputStream = new DataOutputStream(bos);
        try {
            streamer().writeTo(data, outputStream);
            nbttagcompound.setByteArray(DATA_NAME, bos.toByteArray())   ;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return nbttagcompound;
    }

    private void set(Type newData) {
        if (data != newData) {
            if (data != null) data.stopInforming(toSelf);
            newData.informOnChange(toSelf);
        }
        data = newData;
        setDirty(true);
    }

}