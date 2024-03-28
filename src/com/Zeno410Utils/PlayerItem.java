/**
 * This class stores items by player and can remove them if a player quits
 * @author Zeno410
 */

package com.Zeno410Utils;
import java.util.HashMap;
import net.minecraft.entity.player.EntityPlayer;


public class PlayerItem<Type> extends Function<EntityPlayer,Type>{
    private Function<EntityPlayer,Type> itemMaker;
    HashMap<PlayerID,Type> items;

    public PlayerItem(Function<EntityPlayer,Type> _itemMaker) {
        itemMaker = _itemMaker;
        items = new HashMap<PlayerID,Type>();
    }

    public PlayerItem(final Maker<Type> maker) {
        this (new Function<EntityPlayer,Type>() {
            public Type result(EntityPlayer player) {
                return maker.item();
            }
        });
    }

    public Type result(EntityPlayer player) {
        PlayerID id = new PlayerID(player);
        if (items.containsKey(id)) return items.get(id);
        // otherwise we don't have it so we have to make it
        items.put(id, itemMaker.result(player));
        return items.get(id);
    }

    public void isLeaving(EntityPlayer leaving) {
        if (items.containsKey(new PlayerID(leaving)));
        items.remove(new PlayerID(leaving));
    }

}
