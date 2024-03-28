
package com.Zeno410Utils;
import java.io.DataInput;
import java.io.IOException;
import java.io.DataOutput;

/**
 *
 * @author Zeno410
 */
public class Placed<Type> {
    private Numbered<Type> counted;
    public Placed(int _count,Type _item) {
        counted = new Numbered(_count,_item);
    }

    public Placed(Numbered<Type> _counted) {
        counted = _counted;
    }

    public Integer location() {return counted.count();}
    public Type item() {return counted.item();}

    public static <StreamType> Streamer<Placed<StreamType>> streamer(
            final Streamer<StreamType> substreamer) {

        return new Streamer<Placed<StreamType>>() {
            IntStreamer numberStreamer = new IntStreamer();

            public Placed<StreamType> readFrom(DataInput input) throws IOException {
                Integer number =numberStreamer.readFrom(input);
                return new Placed<StreamType>(number,substreamer.readFrom(input));
            }

            public void writeTo(Placed<StreamType> written, DataOutput output) throws IOException {
                numberStreamer.writeTo(written.counted.count(), output);
                substreamer.writeTo(written.counted.item(), output);
            }
        };

    }
}