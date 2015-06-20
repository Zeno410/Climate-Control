package climateControl.utils;

/**
 * this is an abstract that allows external packages to use GenLayer random numbers
 * Done as an abstract class to be used for a private call back set up by the GenLayer
 * The reason for this is that external access to nextInt() could cause nasty untraceable bugs
 * With this method the GenLayer can control what it gives access to.
 * @author Zeno410
 */
public abstract class IntRandomizer {
    public abstract int nextInt(int range);

}
