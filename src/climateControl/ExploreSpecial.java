
package climateControl;

import com.Zeno410Utils.Zeno410Logger;
import java.util.logging.Logger;

/**
 *
 * @author Zeno410
 */
public class ExploreSpecial {
    public static Logger logger = new Zeno410Logger("ExploreSpecial").logger();

    public void look() {
        for (int climate = 1; climate < 5; climate++) {
            for (int special = 0; special < 16; special ++) {
                int k1 = climate;
                k1 |= 1+ special <<8 & 3840;
                int l1 = (k1 & 3840) >> 8;
                int k2 = k1;
                k2 &= -3841;
                logger.info("climate "+climate + " flag " + special + " stored " + k1 + " l1 " + l1 + " k2 "+k2);
            }
        }
        throw new RuntimeException();
    }

}
