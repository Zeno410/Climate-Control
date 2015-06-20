
package climateControl.utils;

/**
 *
 * @author Zeno410
 */
public interface Mutable<Type> extends Trackable<Type>{
    public void set(Type newValue);
    public Type value();

    public class Concrete<CType> implements Mutable<CType>{
        private CType type;
        private Trackers<CType> trackers = new Trackers<CType>();
        public Concrete(CType initial) {type  = initial;}
        public void set(CType newValue) {
            type = newValue;
            trackers.update(type);
        }

        public CType value() {return type;}

        public void informOnChange(Acceptor<CType> target) {
            trackers.informOnChange(target);
        }

        public void stopInforming(Acceptor<CType> target) {
            trackers.stopInforming(target);
        }
        
    }
}
