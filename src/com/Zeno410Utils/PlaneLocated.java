
package com.Zeno410Utils;

import java.util.HashMap;
import java.io.DataInput;
import java.io.IOException;
import java.io.DataOutput;
import java.util.Set;

/**
 *
 * @author Zeno410
 */
public class PlaneLocated<Type> implements SelfTrackable<PlaneLocated<Type>> {
    private HashMap<PlaneLocation,Type> storedVals = new HashMap<PlaneLocation,Type>();
    private Trackers<PlaneLocated<Type>> trackers = new Trackers<PlaneLocated<Type>> ();

    public final Type get(PlaneLocation location) {
        return storedVals.get(location);
    }
    public final void put(PlaneLocation location, Type stored) {
        storedVals.put(location, stored);
        trackers.update(this);
    }

    public final void putAll(HashMap<PlaneLocation,Type> newValues) {
        for (PlaneLocation location: newValues.keySet()) {
            storedVals.put(location, newValues.get(location));
        }
        trackers.update(this);
    }

    public boolean confirm(PlaneLocation location, Type value) {
        Type existing = storedVals.get(location);
        if (existing == null) {
            storedVals.put(location, value);
        } else {
            if (!existing.equals(value)) {
                return false;
            }
        }
        return true;
    }

    public int size() {return storedVals.size();}

    public Set<PlaneLocation> locations() {return this.storedVals.keySet();}

    public static <StoredType> Streamer<PlaneLocated<StoredType>> streamer(Streamer<StoredType> typeStreamer) {
        return new PlaneLocatedStreamer(typeStreamer);
    }

    public void informOnChange(Acceptor<PlaneLocated<Type>> target) {
        trackers.informOnChange(target);
    }

    public void stopInforming(Acceptor<PlaneLocated<Type>> target) {
        trackers.stopInforming(target);
    }

    public final void remove(PlaneLocation location) {
        storedVals.remove(location);
        trackers.update(this);
    }

    private static class PlaneLocatedStreamer<StoredType> extends Streamer<PlaneLocated<StoredType>>  {
        private final Streamer<StoredType> storedStreamer;
        PlaneLocatedStreamer(Streamer<StoredType> storedStreamer){
            this.storedStreamer = storedStreamer;
        }

        @Override
        public PlaneLocated<StoredType> readFrom(DataInput input) throws IOException {
            PlaneLocated<StoredType> result = new PlaneLocated<StoredType>();
            int entries = input.readInt();
            for (int entry = 0; entry < entries; entry ++) {
                int x = input.readInt();
                int z = input.readInt();
                result.storedVals.put(new PlaneLocation(x,z), storedStreamer.readFrom(input));
            }
            return result;
        }

        @Override
        public void writeTo(PlaneLocated<StoredType> written, DataOutput output) throws IOException {
            output.writeInt(written.storedVals.size());
            for (PlaneLocation location: written.storedVals.keySet()){
                output.writeInt(location.x());
                output.writeInt(location.z());
                storedStreamer.writeTo(written.storedVals.get(location),output);
            }
        }

    }
}
