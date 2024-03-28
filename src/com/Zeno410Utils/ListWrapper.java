
package com.Zeno410Utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 *
 * @author Zeno410
 */
public class ListWrapper<Type> implements List<Type> {
    private final List<Type> wrappee;

    public ListWrapper(List<Type> toWrap) {
        wrappee = toWrap;
    }
    public int size() {
        return wrappee.size();
    }

    public boolean isEmpty() {
        return wrappee.isEmpty();
    }

    public boolean contains(Object arg0) {
        return wrappee.contains(arg0);
    }

    public Iterator iterator() {
        return wrappee.iterator();
    }

    public Object[] toArray() {
        return wrappee.toArray();
    }

    public Object[] toArray(Object[] arg0) {
        return wrappee.toArray(arg0);
    }

    public boolean add(Type arg0) {
        return wrappee.add(arg0);
    }

    public boolean remove(Object arg0) {
        return wrappee.remove(arg0);
    }

    public boolean containsAll(Collection arg0) {
        return wrappee.containsAll(arg0);
    }

    public boolean addAll(Collection arg0) {
        return wrappee.addAll(arg0);
    }

    public boolean addAll(int arg0, Collection arg1) {
        return wrappee.addAll(arg0, arg1);
    }

    public boolean removeAll(Collection arg0) {
        return wrappee.removeAll(arg0);
    }

    public boolean retainAll(Collection arg0) {
        return wrappee.retainAll(arg0);
    }

    public void clear() {
        wrappee.clear();
    }

    public Type get(int arg0) {
        return wrappee.get(arg0);
    }

    public Type set(int arg0, Type arg1) {
        return wrappee.set(arg0, arg1);
    }

    public void add(int arg0, Type arg1) {
        wrappee.add(arg0, arg1);
    }

    public Type remove(int arg0) {
        return wrappee.remove(arg0);
    }

    public int indexOf(Object arg0) {
        return wrappee.indexOf(arg0);
    }

    public int lastIndexOf(Object arg0) {
        return wrappee.lastIndexOf(arg0);
    }

    public ListIterator listIterator() {
        return wrappee.listIterator();
    }

    public ListIterator listIterator(int arg0) {
        return wrappee.listIterator(arg0);
    }

    public List subList(int arg0, int arg1) {
        return wrappee.subList(arg0, arg1);
    }

}
