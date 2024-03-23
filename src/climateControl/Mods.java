package climateControl;

import java.util.Arrays;

import javax.annotation.Nullable;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;

public enum Mods {
    abyssalcraft,
    auxbiomes,
    betteragriculture,
    biomesoplenty,
    byg("biomesyougo"),
    bionisation3,
    buildcraftenergy,
    candymod,
    defiledlands,
    douglas_forest,
    environs,
    explorercraft,
    floricraft,
    fyrecraft,
    geographicraft,
    gravityfalls,
    minecraft,
    mistbiomes,
    nt("novamterram"),
    odioitamod,
    plants2,
    pvj("vibrantjourneys"),
    realworld,
    redwoods,
    rockhounding_surface,
    spookybiomes,
    subaquatic,
    sugiforest,
    terscraft,
    thaumcraft,
    traverse,
    vampirism,
    valoegheses_be("zoesteria");

    private final String prettyName;
    private boolean loaded;

    Mods() {
        this("");
    }

    Mods(String name) {
        this.prettyName = (!name.isEmpty()) ? name : name();
    }

    static void init() {
        Arrays.stream(Mods.values()).forEach(mod -> mod.loaded = Loader.isModLoaded(mod.name()));
    }

    @Nullable
    public static Mods get(final String modId) {
        return Arrays.stream(values())
                .filter(mod -> mod.name().equals(modId))
                .findFirst()
                .orElse(null);
    }

    public boolean isLoaded() {
        return this.loaded;
    }

    public ResourceLocation getResourceLocation(final String biome) {
        return new ResourceLocation(name() + ":" + biome);
    }

    public String getPrettyName() {
        return this.prettyName;
    }
}
