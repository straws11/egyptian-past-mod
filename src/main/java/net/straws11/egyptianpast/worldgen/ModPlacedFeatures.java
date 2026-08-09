package net.straws11.egyptianpast.worldgen;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.straws11.egyptianpast.entity.ModEntityRegistration;

import static net.minecraft.data.worldgen.BiomeDefaultFeatures.caveSpawns;

public class ModPlacedFeatures {

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        var configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

    }

    public static void egyptianDesertSpawns(MobSpawnSettings.Builder builder) {
        builder.addSpawn(MobCategory.CREATURE, 8, new MobSpawnSettings.SpawnerData(EntityTypes.RABBIT, 2, 3));
        builder.addSpawn(MobCategory.CREATURE, 2, new MobSpawnSettings.SpawnerData(EntityTypes.CAMEL, 1, 1));
        caveSpawns(builder);
        // not including normal mobs from minecraft here monsters(builder, 19, 1, 0, 50, false);
        builder.addSpawn(MobCategory.MONSTER, 80, new MobSpawnSettings.SpawnerData(ModEntityRegistration.MUMMY_ENTITY.get(), 4, 4));
    }
}
