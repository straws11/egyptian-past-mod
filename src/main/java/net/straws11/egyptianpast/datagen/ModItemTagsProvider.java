package net.straws11.egyptianpast.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.ItemTagsProvider;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.straws11.egyptianpast.EgyptianPast;
import net.straws11.egyptianpast.item.ModItems;
import net.straws11.egyptianpast.tags.ModTags;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class ModItemTagsProvider extends ItemTagsProvider {

    public ModItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, EgyptianPast.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        tag(ItemTags.HEAD_ARMOR).add(ModItems.PHARAOH_CROWN.getKey());
        TagAppender<Item> organTag = tag(ModTags.Items.ORGAN);

        Stream.of(ModItems.LUNGS, ModItems.LIVER, ModItems.STOMACH, ModItems.INTESTINES)
            .map(DeferredHolder::getKey)
            .forEach(organTag::add);

    }
}
