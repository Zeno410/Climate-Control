
package com.Zeno410Utils;
import java.io.DataInput;
import java.io.IOException;
import java.io.DataOutput;

/**
 *
 * @author Zeno410
 */
public class Counted<Type> {
    private Numbered<Type> counted;
    public Counted(int _count,Type _item) {
        counted = new Numbered(_count,_item);
    }

    public int count() {return counted.count();}
    public Type item() {return counted.item();}

    public static <StreamType> Streamer<Counted<StreamType>> streamer(
            final Streamer<StreamType> substreamer) {

        return new Streamer<Counted<StreamType>>() {
            IntStreamer numberStreamer = new IntStreamer();

            public Counted<StreamType> readFrom(DataInput input) throws IOException {
                Integer number =numberStreamer.readFrom(input);
                return new Counted<StreamType>(number,substreamer.readFrom(input));
            }

            public void writeTo(Counted<StreamType> written, DataOutput output) throws IOException {
                numberStreamer.writeTo(written.counted.count(), output);
                substreamer.writeTo(written.counted.item(), output);
            }
        };

    }
}