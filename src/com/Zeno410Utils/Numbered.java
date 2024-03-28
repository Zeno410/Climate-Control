package com.Zeno410Utils;

import java.io.DataInput;
import java.io.IOException;
import java.io.DataOutput;
import java.util.Comparator;

/**
 * This class numbers an object
 * @author Zeno410
 */
public class Numbered<Type> {
    private int count;
    private Type item;

    public Numbered(int _count,Type _item) {
        count = _count;
        item  = _item;
    }
    
    public static <T> Numbered<T> from(int count, T object) {
        return new Numbered<T>(count,object);
    }

    public int count() {return count;}
    public Type item() {return item;}

    public static <StreamType> Streamer<Numbered<StreamType>> streamer(
            final Streamer<StreamType> substreamer) {

        return new Streamer<Numbered<StreamType>>() {
            IntStreamer numberStreamer = new IntStreamer();

            public Numbered<StreamType> readFrom(DataInput input) throws IOException {
                Integer number =numberStreamer.readFrom(input);
                return new Numbered<StreamType>(number,substreamer.readFrom(input));
            }

            public void writeTo(Numbered<StreamType> written, DataOutput output) throws IOException {
                numberStreamer.writeTo(written.count, output);
                substreamer.writeTo(written.item, output);
            }
        };
    }

    public static Comparator<Numbered> comparator() {
        return new Comparator<Numbered>() {

            public int compare(Numbered arg0, Numbered arg1) {
                return arg0.count - arg1.count;
            }

        };
    }

    public static <ItemType> Comparator<Numbered<ItemType>>  comparator(
            final Comparator<ItemType> subComparator) {
        return new Comparator<Numbered<ItemType>>() {

            public int compare(Numbered<ItemType> arg0, Numbered<ItemType> arg1) {
                int result = arg0.count - arg1.count;
                if (result == 0) return subComparator.compare(arg0.item, arg1.item);
                return result;
            }

        };
    }

}
