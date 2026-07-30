package net.straws11.egyptianpast.datagen;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.straws11.egyptianpast.block.ModBlockRegistration;
import net.straws11.egyptianpast.item.ModItemRegistration;

import java.util.Set;

public class ModBlockLootTableProvider extends BlockLootSubProvider {

    public ModBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        dropSelf(ModBlockRegistration.LIMESTONE.get());
        dropSelf(ModBlockRegistration.PAPYRUS_REED_BLOCK.get());
    }

    // not sure if this is needed, got this function from tutorial
    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlockRegistration.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }
}
