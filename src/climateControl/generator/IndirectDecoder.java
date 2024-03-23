
package climateControl.generator;

import com.Zeno410Utils.Acceptor;

/**
 *
 * @author Zeno410
 */
public class IndirectDecoder extends Acceptor<Decoder> implements Decoder {
    private Decoder manager;

    public int decode(Integer code) {
        return manager.decode(code);
    }

    @Override
    public void accept(Decoder accepted) {
        manager = accepted;
    }

}
