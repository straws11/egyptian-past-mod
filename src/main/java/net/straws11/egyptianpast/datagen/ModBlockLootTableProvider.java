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
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.straws11.egyptianpast.block.ModBlocks;
import net.straws11.egyptianpast.block.PomegranateCropBlock;
import net.straws11.egyptianpast.block.Sarcophagus;
import net.straws11.egyptianpast.data.ModDataComponentRegistration;
import net.straws11.egyptianpast.item.ModItems;

import java.util.Set;

public class ModBlockLootTableProvider extends BlockLootSubProvider {

    public ModBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        dropSelf(ModBlocks.LIMESTONE.get());
        dropSelf(ModBlocks.EGYPTIAN_STONE.get());
        dropSelf(ModBlocks.EGYPTIAN_COBBLESTONE.get());
        dropSelf(ModBlocks.PAPYRUS_REED_BLOCK.get());
        dropSelf(ModBlocks.SPHINX_BLOCK.get());
        dropSelf(ModBlocks.EGYPTIAN_POT_1.get());
        dropSelf(ModBlocks.EGYPTIAN_POT_2.get());
        dropSelf(ModBlocks.EGYPTIAN_POT_3.get());
        dropSelf(ModBlocks.ANUBIS_BLOCK.get());
        add(ModBlocks.SARCOPHAGUS.get(), block ->
            LootTable.lootTable().withPool(LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1.0F))
                    .add(LootItem.lootTableItem(block))
                    .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                            .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(Sarcophagus.PART, BedPart.HEAD))
                    )
            )
        );
        dropSelf(ModBlocks.PEDESTAL_BLOCK.get());

        add(ModBlocks.CANOPIC_JAR_BLOCK.get(), block ->
            this.createSingleItemTable(block)
                .apply(CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY)
                    .include(ModDataComponentRegistration.ORGAN_TYPE.get())
            )
        );

        add(ModBlocks.POMEGRANATE_CROP.get(), createCropDrops(ModBlocks.POMEGRANATE_CROP.get(),
            ModItems.POMEGRANATE.get(), ModBlocks.POMEGRANATE_SEEDS.get(),
            LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.POMEGRANATE_CROP.get())
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(PomegranateCropBlock.AGE, 3))
            )
        );
    }

    // not sure if this is needed, got this function from tutorial
    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }
}
