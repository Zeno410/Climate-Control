package climateControl;

/**
 * // just a signature class to distinguish different types of Makers
 * @author Zeno410
 */
abstract public class Default<Type> {

    abstract Type item();
    
    public static class Self<Type extends PublicallyCloneable<Type>> extends Default<Type> {
        // just sends itself
        private final Type self;
        public Self(Type toSend){
            self = toSend;
        }
        public Type item() {return (Type)self.clone();}
    }

}
