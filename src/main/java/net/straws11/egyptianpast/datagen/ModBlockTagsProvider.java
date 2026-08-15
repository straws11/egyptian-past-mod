package net.straws11.egyptianpast.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.straws11.egyptianpast.EgyptianPast;
import net.straws11.egyptianpast.block.ModBlocks;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends BlockTagsProvider {

    public ModBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, EgyptianPast.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
            .add(ModBlocks.getBlockResourceKey(ModBlocks.LIMESTONE.get()))
            .add(ModBlocks.getBlockResourceKey(ModBlocks.EGYPTIAN_STONE.get()))
            .add(ModBlocks.getBlockResourceKey(ModBlocks.EGYPTIAN_COBBLESTONE.get()))
            //.add(ModBlockRegistration.getBlockResourceKey(ModBlockRegistration.SARCOPHAGUS.get()))
            .add(ModBlocks.getBlockResourceKey(ModBlocks.CANOPIC_JAR_BLOCK.get()))
            .add(ModBlocks.getBlockResourceKey(ModBlocks.PEDESTAL_BLOCK.get()));

    }
}
