package com.Zeno410Utils;

/**
 *
 * @author Zeno410
 *
 * IMPORTANT: Use of this interface implies a contract in which the implementing object will
 * remember it's trackers and update them whenever it's changed. This is not an enforceable contract,
 * and if you don't follow it you get a obnoxious and hard-to-find fictitious implementation bug.
 */

public interface SelfTrackable<Type extends SelfTrackable<Type>> {
    public void informOnChange(Acceptor<Type> target);
    public void stopInforming(Acceptor<Type> target);
}
