
package climateControl.generator;

import climateControl.utils.Acceptor;

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
