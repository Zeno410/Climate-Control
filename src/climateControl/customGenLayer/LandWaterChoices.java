
package climateControl.customGenLayer;

import com.Zeno410Utils.IntRandomizer;

/**
 *
 * @author Zeno410
 */
public class LandWaterChoices {
    int original;
    boolean isWater;
    UpToFour land = new UpToFour();
    UpToFour water = new UpToFour();

    LandWaterChoices() {
    }

    void setOriginal(int original, boolean isWater) {
        this.original = original;
        this.isWater = isWater;
        land.clear();
        water.clear();
    }

    int totalItems() {
        int result = water.size();
        result += land.size();
        return result;
    }

    void add(int value, boolean isAddedWater) {
        item(isAddedWater).add(value);
    }

    boolean equal() {
        return water.size() ==2;
    }

    boolean isChoiceWater() {
        return land.size() <2;
    }

    UpToFour item(boolean waterItem) {
        if (waterItem) return water;
        return land;
    }

    int mostCommon(IntRandomizer randomizer) {
        int oldCount = water.count;
        // if mostly land return one of the land items
        if (oldCount <2) {
            // if original land return it
            if (!isWater) return original;
            return land.items[randomizer.nextInt(land.count)];
        }
        // if tied no change
        if (oldCount == 2) return original;
        if (!isWater) return water.items[randomizer.nextInt(water.count)];
        return original;
    }


    private class UpToFour {
        int count = 0;
        int value;
        int [] items = new int[4];
        void setValue(int newValue) {
            value = newValue;
            count = 0;
        }
        void clear() {count = 0;}
        void add(int added) {
            items[count]=added;
            count++;
        }
        int size() {return count;}
    }
}