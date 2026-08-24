package net.straws11.egyptianpast.worldgen;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.BiasedToBottomInt;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.BlockColumnConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.straws11.egyptianpast.EgyptianPast;
import net.straws11.egyptianpast.block.ModBlocks;

public class ModConfiguredFeatures {

    public static final ResourceKey<ConfiguredFeature<?, ?>> PAPYRUS_REED = registerKey("papyrus_reed");

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        context.register(PAPYRUS_REED, new ConfiguredFeature<>(Feature.BLOCK_COLUMN,
            BlockColumnConfiguration.simple(BiasedToBottomInt.of(2, 4),
                BlockStateProvider.simple(ModBlocks.PAPYRUS_REED_BLOCK.get())
            ))
        );

    }

    private static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE,
            Identifier.fromNamespaceAndPath(EgyptianPast.MOD_ID, name));
    }

}
