package net.straws11.egyptianpast.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetComponentsFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.straws11.egyptianpast.EgyptianPast;
import net.straws11.egyptianpast.block.ModBlocks;
import net.straws11.egyptianpast.data.ModDataComponentRegistration;
import net.straws11.egyptianpast.item.ModItems;
import net.straws11.egyptianpast.item.OrganType;

import java.util.function.BiConsumer;

public class ModExtraLootProvider implements LootTableSubProvider {
    public static final ResourceKey<LootTable> CANOPIC_JARS = ResourceKey.create(Registries.LOOT_TABLE,
        Identifier.fromNamespaceAndPath(EgyptianPast.MOD_ID, "extra/glm/canopic_jars"));

    public ModExtraLootProvider(HolderLookup.Provider provider) {

    }
    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
        LootPool.Builder pool = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1f))
                .when(LootItemRandomChanceCondition.randomChance(0.5f));

        for (OrganType organ : OrganType.values()) {
            if (organ == OrganType.EMPTY) continue;
            pool.add(LootItem.lootTableItem(ModBlocks.CANOPIC_JAR.get())
                .setWeight(1).apply(
                    SetComponentsFunction.setComponent(
                        ModDataComponentRegistration.ORGAN_TYPE.get(), organ
                    )
                ));
        }
        output.accept(CANOPIC_JARS, LootTable.lootTable().withPool(pool));
    }
}
