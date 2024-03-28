
package com.Zeno410Utils;
import net.minecraft.world.World;

/**
 *
 * @author Zeno410
 */
public class Reserver<Type> extends Savee<ReservationNumbers> {

    public final String name;
    public Streamer<ReservationNumbers> streamer() {return ReservationNumbers.streamer();}
    private World savedWorld;
    public Reserver(String name, World world) {
        super(name+"_reservations");
        this.name = name;
        savedWorld = world;
        loadData(mapName,this,world,newNumbers());
    }

    public void alreadyUsed(int inUse) {
        // for setting numbers which should not be used;
           data().setUnavailable(inUse);
    }

    public static Maker<ReservationNumbers> newNumbers() {
        return new Maker<ReservationNumbers>() {
            public ReservationNumbers item() {return new ReservationNumbers();}
        };
    }

    public Reservation<Type> reservation(Function<Integer,Type> maker, final World world) {
        Maker<Integer> nextNumber = new Maker<Integer>() {
            public Integer item() {
                return world.getMapStorage().getUniqueDataId(name);
            }
        };
        return new ActualReservation(data().reserve(nextNumber),maker);
    }

    private class ActualReservation implements Reservation<Type>{
        public final Integer number;
        private final Function<Integer,Type> maker;
        private boolean valid = true;

        public ActualReservation(Integer number, Function<Integer,Type> maker) {
            this.number = number;
            this.maker = maker;
        }

        public void cancel() {
            data().release(number);
            valid = false;
        }

        public Type use() {
            data().use(number);
            valid = false;
            return maker.result(number);
        }

        public void finalize() {
            if (valid) {
                cancel();
            }
        }

        public Integer key() {return number;}
    }

}
