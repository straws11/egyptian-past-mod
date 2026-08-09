package net.straws11.egyptianpast.datagen;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class ModBiomeProvider {

    public static void bootstrap(BootstrapContext<Biome> context) {
        HolderGetter<ConfiguredWorldCarver<?>> carversGetter =
                context.lookup(Registries.CONFIGURED_CARVER);

        HolderGetter<PlacedFeature> featuresGetter =
                context.lookup(Registries.PLACED_FEATURE);

        register(context, ModBiomes.EGYPTIAN_DESERT, ModBiomes.egyptianDesert(featuresGetter, carversGetter));
    }

    private static void register(BootstrapContext<Biome> context, ResourceKey<Biome> key, Biome biome) {
        context.register(key, biome);
    }
}
