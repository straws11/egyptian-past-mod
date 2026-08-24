package net.straws11.egyptianpast.worldgen;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.VegetationFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.placement.*;
import net.straws11.egyptianpast.EgyptianPast;
import net.straws11.egyptianpast.entity.ModEntities;

import java.util.List;

import static net.minecraft.data.worldgen.BiomeDefaultFeatures.caveSpawns;

public class ModPlacedFeatures {

    public static final ResourceKey<PlacedFeature> PATCH_PAPYRUS_REED_EGYPTIAN_DESERT =
        registerKey("patch_papyrus_reed_egyptian_desert");
    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        var configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        var papyrus_reed = configuredFeatures.getOrThrow(ModConfiguredFeatures.PAPYRUS_REED);

        PlacedFeature papyrusPlacedFeature = new PlacedFeature(papyrus_reed, List.of(
            InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome(),
            CountPlacement.of(20), RandomOffsetPlacement.ofTriangle(4, 0),
            VegetationFeatures.nearWaterPredicate(Blocks.SUGAR_CANE)
        ));

        context.register(PATCH_PAPYRUS_REED_EGYPTIAN_DESERT, papyrusPlacedFeature);

    }

    public static void egyptianDesertSpawns(MobSpawnSettings.Builder builder) {
        builder.addSpawn(MobCategory.CREATURE, 8, new MobSpawnSettings.SpawnerData(EntityTypes.RABBIT, 2, 3));
        builder.addSpawn(MobCategory.CREATURE, 2, new MobSpawnSettings.SpawnerData(EntityTypes.CAMEL, 1, 1));
        caveSpawns(builder);
        // not including normal mobs from minecraft here monsters(builder, 19, 1, 0, 50, false);
        builder.addSpawn(MobCategory.MONSTER, 80, new MobSpawnSettings.SpawnerData(ModEntities.MUMMY_ENTITY.get(), 4, 4));
    }

    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE,
            Identifier.fromNamespaceAndPath(EgyptianPast.MOD_ID, name));
    }
}
