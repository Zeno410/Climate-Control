
package com.Zeno410Utils;

/**
 *
 * @author Zeno410
 */
public abstract class Maker<Type> {
    public abstract Type item();

    public class Self<Type> extends Maker<Type> {
        // just sends itself
        private final Type self;
        public Self(Type toSend){
            self = toSend;
        }
        public Type item() {return self;}
    }
}
