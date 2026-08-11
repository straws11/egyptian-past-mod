package net.straws11.egyptianpast.datagen;

import net.minecraft.advancements.predicates.StatePropertiesPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.straws11.egyptianpast.block.ModBlockRegistration;
import net.straws11.egyptianpast.block.Sarcophagus;
import net.straws11.egyptianpast.block.entity.ModBlockEntityRegistration;

import java.util.Set;
import java.util.function.Function;

public class ModBlockLootTableProvider extends BlockLootSubProvider {

    public ModBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        dropSelf(ModBlockRegistration.LIMESTONE.get());
        dropSelf(ModBlockRegistration.EGYPTIAN_STONE.get());
        dropSelf(ModBlockRegistration.EGYPTIAN_COBBLESTONE.get());
        dropSelf(ModBlockRegistration.PAPYRUS_REED_BLOCK.get());
        add(ModBlockRegistration.SARCOPHAGUS.get(), block ->
            LootTable.lootTable().withPool(LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1.0F))
                    .add(LootItem.lootTableItem(block))
                    .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                            .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(Sarcophagus.PART, BedPart.HEAD))
                    )
            )
        );
        dropSelf(ModBlockRegistration.PEDESTAL_BLOCK.get());
    }

    // not sure if this is needed, got this function from tutorial
    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlockRegistration.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }
}
