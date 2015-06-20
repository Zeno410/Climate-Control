package climateControl;
import climateControl.utils.Zeno410Logger;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.logging.Logger;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldSavedData;

/**
 *
 * @author Zeno410
 */

public class DimensionalDataStorage<DataType extends WorldSavedData> {


    public final String dataStorageName;
    private ISaveHandler saveHandler;
    private final DataType dataType;

    public DimensionalDataStorage(ISaveHandler saveHandler, String dataStorageName, DataType dataType){
        this.saveHandler = saveHandler;
        this.dataStorageName = dataStorageName;
        this.dataType = dataType;
    }


    public String fileName(int dimension) {
        return dataStorageName + dimension;
    }
    
    /**
     * Loads an existing data from the world's "fileName" file,
     * returns the default if none such file exists.
     */
    public DataType load(int dimension, DataType defaultValue) {

        if (this.saveHandler != null) {
            try {
                File file1 = this.saveHandler.getMapFileFromName(fileName(dimension));

                if (file1 != null && file1.exists())  {
                    FileInputStream fileinputstream = new FileInputStream(file1);
                    NBTTagCompound nbttagcompound = CompressedStreamTools.readCompressed(fileinputstream);
                    fileinputstream.close();
                    DataType data = this.dataType;
                    data.readFromNBT(nbttagcompound.getCompoundTag("data"));
                    return data;
                }
            }
            catch (Exception exception1){
                exception1.printStackTrace();
            }
        }
        return defaultValue;

    }

    public void save(DataType data,int dimension) {

        if (this.saveHandler != null) {
            try {
                File file1 = this.saveHandler.getMapFileFromName(fileName(dimension));

                if (file1 != null)  {
                    NBTTagCompound tag = new NBTTagCompound();
                    data.writeToNBT(tag);
                    NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                    nbttagcompound1.setTag("data", tag);
                    FileOutputStream fileoutputstream = new FileOutputStream(file1);
                    CompressedStreamTools.writeCompressed(nbttagcompound1, fileoutputstream);
                    fileoutputstream.close();
                }
            }
            catch (Exception exception1){
                exception1.printStackTrace();
            }
        }
    }


}