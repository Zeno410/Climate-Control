
package com.Zeno410Utils;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashSet;
import java.util.logging.Logger;

/**
 *
 * @author Zeno410
 */
public class ReservationNumbers implements SelfTrackable<ReservationNumbers>{

    //static Logger logger= new Zeno410Logger("ReservationNumbers").logger();

    private Trackers<ReservationNumbers> trackers = new Trackers<ReservationNumbers>();
    private HashSet<Integer> availableNumbers = new HashSet<Integer>();
    private HashSet<Integer> reservedNumbers = new HashSet<Integer>();

    public void informOnChange(Acceptor<ReservationNumbers> target) {
        trackers.informOnChange(target);
    }

    public void stopInforming(Acceptor<ReservationNumbers> target) {
        trackers.stopInforming(target);
    }

    private String numberStatus() {
        String result = "";
        result += "reserved ";
        for (Integer reserved: reservedNumbers) {
            result += " " + reserved;
        }
        for (Integer available: availableNumbers) {
            result += " " + available;
        }
        return result;
    }

    public void release(Integer released) {
        //logger.info(this.toString()+ " releasing "+numberStatus());
        if (reservedNumbers.contains(released)) {
            this.reservedNumbers.remove(released);
            this.availableNumbers.add(released);
            trackers.update(this);
        } else throw new MissingException(released);
    }

    public Integer reserve(Maker<Integer> ifNothingAvailable) {
        //logger.info(this.toString()+ " reserving "+numberStatus());
        Integer result;
        if (availableNumbers.size()>0) {
            result = this.availableNumbers.iterator().next();
            this.availableNumbers.remove(result);
        } else {
            result = ifNothingAvailable.item();
        }
        this.reservedNumbers.add(result);

        //logger.info(this.toString()+ " reserved "+result + "leaving "+numberStatus());
        trackers.update(this);
        return result;
    }

    public void use(Integer used) {
        //logger.info(this.toString()+ " using "+numberStatus());
        if (reservedNumbers.contains(used)) {
            this.reservedNumbers.remove(used);
            trackers.update(this);
        } else throw new MissingException(used);

    }

    public void setUnavailable(Integer unavailable) {
        if (this.availableNumbers.contains(unavailable)) {
            availableNumbers.remove(unavailable);
        }
    }

    public static Streamer<ReservationNumbers> streamer() {return new ReservationNumberStreamer();}

    private static class ReservationNumberStreamer extends Streamer<ReservationNumbers> {

        @Override
        public ReservationNumbers readFrom(DataInput input) throws IOException {
            ReservationNumbers result = new ReservationNumbers();
            int available = input.readInt();
            for (int i = 0; i < available; i ++) {
                int number = input.readInt();
                //ItemCopyingMap.logger.info("reading "+number);
                result.availableNumbers.add(number);
            }
            // we don't read reservations as they can't be saved
            return result;
        }

        @Override
        public void writeTo(ReservationNumbers written, DataOutput output) throws IOException {
            // reservations are saved as available since all reservations are cancelled during
            // quit-restart;
            output.writeInt(written.availableNumbers.size()+written.reservedNumbers.size());
            for (Integer number: written.availableNumbers) {
                output.writeInt(number);
                //ItemCopyingMap.logger.info("available "+number);
            }
            for (Integer number: written.reservedNumbers) {
                //ItemCopyingMap.logger.info("reserved "+number);
                output.writeInt(number);
            }
        }

    }

    public static class MissingException extends RuntimeException {
        public final Integer missingNumber;
        MissingException(Integer missingNumber) {
            this.missingNumber = missingNumber;
        }
    }
}