package net.straws11.egyptianpast.datagen;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.features.VegetationFeatures;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.attribute.BackgroundMusic;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.straws11.egyptianpast.EgyptianPast;
import net.straws11.egyptianpast.worldgen.ModPlacedFeatures;

public class ModBiomes {
    /*public static final ResourceKey<? extends Registry<Biome>> ROOT_ID = ResourceKey.createRegistryKey(
            Identifier.withDefaultNamespace("mod_biomes")
    );*/
    public static final ResourceKey<Biome> EGYPTIAN_DESERT = ResourceKey.create(
            Registries.BIOME, Identifier.fromNamespaceAndPath(EgyptianPast.MOD_ID, "egyptian_desert"));

    public static Biome egyptianDesert(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        MobSpawnSettings.Builder mobs = new MobSpawnSettings.Builder();

        ModPlacedFeatures.egyptianDesertSpawns(mobs);

        BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(placedFeatures, carvers);
        BiomeDefaultFeatures.addFossilDecoration(generation);
        globalOverworldGeneration(generation);
        BiomeDefaultFeatures.addDefaultOres(generation);
        BiomeDefaultFeatures.addDefaultSoftDisks(generation);
        BiomeDefaultFeatures.addDefaultFlowers(generation);
        BiomeDefaultFeatures.addDefaultGrass(generation);
        BiomeDefaultFeatures.addDesertVegetation(generation);
        BiomeDefaultFeatures.addDefaultMushrooms(generation);
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.PATCH_PAPYRUS_REED_EGYPTIAN_DESERT);
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_CACTUS_DESERT);
        BiomeDefaultFeatures.addDesertExtraDecoration(generation);

        return baseBiome(2.0F, 0.0F)
                .hasPrecipitation(false)
                .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic(SoundEvents.MUSIC_BIOME_DESERT))
                .setAttribute(EnvironmentAttributes.SNOW_GOLEM_MELTS, true)
                .mobSpawnSettings(mobs.build())
                .generationSettings(generation.build())
                .build();
    }

    private static void globalOverworldGeneration(BiomeGenerationSettings.Builder generation) {
        BiomeDefaultFeatures.addDefaultCarversAndLakes(generation);
        BiomeDefaultFeatures.addDefaultCrystalFormations(generation);
        BiomeDefaultFeatures.addDefaultMonsterRoom(generation);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(generation);
        BiomeDefaultFeatures.addDefaultSprings(generation);
        BiomeDefaultFeatures.addSurfaceFreezing(generation);
    }

    public static int calculateSkyColor(float temperature) {
        float temp = temperature;
        temp /= 3.0F;
        temp = Mth.clamp(temp, -1.0F, 1.0F);
        return ARGB.opaque(Mth.hsvToRgb(0.62222224F - temp * 0.05F, 0.5F + temp * 0.1F, 1.0F));
    }

    private static Biome.BiomeBuilder baseBiome(float temperature, float downfall) {
        return new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .temperature(temperature)
                .downfall(downfall)
                .setAttribute(EnvironmentAttributes.SKY_COLOR, calculateSkyColor(temperature))
                .specialEffects(new BiomeSpecialEffects.Builder().waterColor(4159204).build());
    }
}
