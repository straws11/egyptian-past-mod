package net.straws11.egyptianpast.datagen.villager;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.VillagerTradesTagsProvider;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.VillagerTradeTags;
import net.straws11.egyptianpast.tags.ModTags;
import net.straws11.egyptianpast.villager.ModVillagers;

import java.util.concurrent.CompletableFuture;

public class ModVillagerTradeTags extends VillagerTradesTagsProvider {
    public ModVillagerTradeTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        getOrCreateRawBuilder(ModTags.Trades.ANCIENT_EGYPTIAN_LEVEL_1)
            .add(TagEntry.element(ModVillagerTrades.ANCIENT_EGYPTIAN_1_EMERALD_MUMMY_WRAP.identifier()))
            .add(TagEntry.element(ModVillagerTrades.ANCIENT_EGYPTIAN_1_EMERALD_POMEGRANATE_SEEDS.identifier()));

        getOrCreateRawBuilder(ModTags.Trades.ANCIENT_EGYPTIAN_LEVEL_2)
            .add(TagEntry.element(ModVillagerTrades.ANCIENT_EGYPTIAN_2_EMERALD_AND_BOOK_HARVESTING_BOOK.identifier()));

    }
}
