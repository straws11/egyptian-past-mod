package net.straws11.egyptianpast.worldgen;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.straws11.egyptianpast.EgyptianPast;

public class ModBiomeModifiers {
    public static final ResourceKey<BiomeModifier> ADD_EGYPTIAN_DESERT =
            ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS,
                    Identifier.fromNamespaceAndPath(EgyptianPast.MOD_ID, "add_egyptian_desert"));

    public static void bootstrap(BootstrapContext<BiomeModifier> context) {
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var biomes = context.lookup(Registries.BIOME);
    }
}
