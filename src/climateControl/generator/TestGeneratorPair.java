
package climateControl.generator;

import climateControl.genLayerPack.GenLayerPack;
import com.Zeno410Utils.AccessLong;
import com.Zeno410Utils.Accessor;
import net.minecraft.world.gen.layer.GenLayer;

/**
 *
 * @author Zeno410
 */
public class TestGeneratorPair {
    private GenLayer oldGen;
    private GenLayer newGen;


    private Accessor<GenLayerPack,GenLayerPack> genLayerPackParent =
        new Accessor<GenLayerPack,GenLayerPack>("field_75909_a");

    private Accessor<GenLayer,GenLayer> genLayerParent =
        new Accessor<GenLayer,GenLayer>("field_75909_a");

    private AccessLong<GenLayer> genLayerSeed = new AccessLong<GenLayer>("field_75907_b");

    public TestGeneratorPair(GenLayer oldGen, GenLayer newGen) {
        this.oldGen = oldGen;
        this.newGen = newGen;
    }

    public TestGeneratorPair next() {
        return new TestGeneratorPair(parent(oldGen),parent(newGen));
    }

    public boolean hasNext() {
        return ((oldGen !=null)||(newGen!=null));
    }

    public GenLayer parent(GenLayer child) {
        if (child == null) return null;
        if (child instanceof GenLayerPack) {
            return ((GenLayerPack)child).getParent();
        }
        return genLayerParent.get(child);
    }

    public String description() {
        return description(oldGen) + " vs "+ description(newGen);
    }

    private String description(GenLayer described) {
        if (described == null) return "missing";
        return described.getClass().getSimpleName()+ " "+genLayerSeed.get(described);
    }
}
