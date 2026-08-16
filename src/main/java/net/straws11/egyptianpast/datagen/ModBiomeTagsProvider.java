package net.straws11.egyptianpast.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.straws11.egyptianpast.EgyptianPast;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModBiomeTagsProvider extends TagsProvider<Biome> {


    public ModBiomeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, Registries.BIOME, lookupProvider, EgyptianPast.MOD_ID);
    }

    public void addTags(HolderLookup.@NotNull Provider provider) {
        this.tag(BiomeTags.HAS_VILLAGE_DESERT)
                .add(ModBiomes.EGYPTIAN_DESERT);

        this.tag(BiomeTags.IS_OVERWORLD)
                .add(ModBiomes.EGYPTIAN_DESERT);

        this.tag(BiomeTags.HAS_DESERT_PYRAMID)
            .add(ModBiomes.EGYPTIAN_DESERT);
    }
}
