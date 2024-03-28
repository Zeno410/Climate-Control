
package com.Zeno410Utils;

import net.minecraft.world.World;

/**
 * This class uses the MapStorage saving system to provide access to a persistent set
 * of numbered objects
 * @author Zeno410
 */
abstract public class SavedNumberedItems<Type extends SelfTrackable<Type>> {
    private final Saver<Type> saver;
    private final String saveTitle;
    private final Streamer<Type> streamer;

    public SavedNumberedItems(String saveTitle,Streamer<Type> streamer) {
        this.saveTitle= saveTitle;
        this.streamer = streamer;
        saver = new Saver<Type>(streamer());
    }

    public final Streamer<Type> streamer(){
        return streamer;
    }
    public abstract Maker<Type> maker(final int index);
    public abstract boolean saveOnNew(int index);

    public int nextInt(World world) {
        return world.getMapStorage().getUniqueDataId(saveTitle);
    }

    public String saveName(int index) {
        return saveTitle + "_"+ index;
    }
    public Type saved(int index, World world) {
        try {
            return saver.savee(saveName(index), world, maker(index), saveOnNew(index)).data();
        } catch (Exception e) {
            //ConfigManager.logger.info(saveName(index).toString());
            //ConfigManager.logger.info(world.toString());
            //ConfigManager.logger.info(maker(index).toString());
            //ConfigManager.logger.info(saver.savee(saveName(index), world, maker(index), saveOnNew(index)).toString());
            throw new RuntimeException();
        }
    }

    public Type saved(int index, World world, Maker<Type> maker) {
        return saver.savee(saveName(index), world, maker,saveOnNew(index)).data();
    }
}