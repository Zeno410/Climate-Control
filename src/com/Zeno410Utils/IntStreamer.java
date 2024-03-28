
package com.Zeno410Utils;
import java.io.DataInput;
import java.io.IOException;
import java.io.DataOutput;

/**
 *
 * @author Zeno410
 */
public class IntStreamer extends Streamer<Integer>{


    public Integer readFrom(DataInput input) throws IOException {
        return input.readInt();
    }
    public void writeTo(Integer written, DataOutput output) throws IOException {
        output.writeInt(written);
    }

}
