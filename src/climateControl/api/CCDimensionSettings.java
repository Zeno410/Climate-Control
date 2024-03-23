
package climateControl.api;

import com.Zeno410Utils.Acceptor;
import com.Zeno410Utils.DimensionSet;
import com.Zeno410Utils.Mutable;
import com.Zeno410Utils.Named;
import com.Zeno410Utils.Settings;
import com.Zeno410Utils.Zeno410Logger;
import java.util.logging.Logger;


/**
 *
 * @author Zeno410
 */
public class CCDimensionSettings extends Settings {
    public final Mutable<String> excludeDimensions = this.general().stringSetting(
            "excludeDimensionIDs", "-1,1", "Comma-separated list of dimension IDs, used only if include list is *");

    public final Mutable<String> includeDimensions = this.general().stringSetting(
            "includeDimensionIDs", "0", "Comma-separated list of dimension IDs, put * to use exclude list");

    public final Named<CCDimensionSettings> named() {return Named.from("CCDimensions.cfg",this);}

    private DimensionSet.Exclude excludedDimensions = new DimensionSet.Exclude(excludeDimensions.value());
    private DimensionSet.Include includedDimensions = new DimensionSet.Include(includeDimensions.value());

    private Acceptor<String> excludeUpdater = new Acceptor<String>(){

        @Override
        public void accept(String accepted) {
                excludedDimensions = new DimensionSet.Exclude(accepted);
        }

    };

    private Acceptor<String> includeUpdater = new Acceptor<String>(){

        @Override
        public void accept(String accepted) {
                includedDimensions = new DimensionSet.Include(accepted);
        }

    };

    public CCDimensionSettings() {
        // the dimension lists can be changed by various events, so put an observer on to keep
        // the calculated fields up-to-date
        this.excludeDimensions.informOnChange(excludeUpdater);
        this.includeDimensions.informOnChange(includeUpdater);
    }

    public boolean ccOnIn(Integer dimension) {
        return includedDimensions.isIncluded(dimension, excludedDimensions);
    }
}
