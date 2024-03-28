
package com.Zeno410Utils;

/**
 *
 * @author Zeno410
 */
public interface Reservation<Type> {

     public void cancel();

     public Type use();

     public Integer key();

}