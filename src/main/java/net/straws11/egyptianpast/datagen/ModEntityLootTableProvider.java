package net.straws11.egyptianpast.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.straws11.egyptianpast.entity.ModEntityRegistration;
import net.straws11.egyptianpast.item.ModItemRegistration;

import java.util.stream.Stream;

public class ModEntityLootTableProvider extends EntityLootSubProvider {

    public ModEntityLootTableProvider(HolderLookup.Provider provider) {
        super(FeatureFlags.DEFAULT_FLAGS, provider);
    }

    @Override
    protected Stream<EntityType<?>> getKnownEntityTypes() {
        return ModEntityRegistration.ENTITY_TYPES.getEntries()
                .stream()
                .map(e -> (EntityType<?>) e.value());
    }

    @Override
    public void generate() {
        add(ModEntityRegistration.MUMMY_ENTITY.get(),
            LootTable.lootTable()
                .withPool(LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1))
                    .add(
                        LootItem.lootTableItem(Items.ROTTEN_FLESH)
                            .apply(SetItemCountFunction.setCount(
                                    UniformGenerator.between(0f, 2f)
                            ))
                            .apply(EnchantedCountIncreaseFunction.lootingMultiplier(
                                    this.registries,
                                    UniformGenerator.between(0f, 1f)
                            ))
                    )
                )
                .withPool(LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1))
                    .add(
                        LootItem.lootTableItem(ModItemRegistration.MUMMY_WRAP)
                            .apply(SetItemCountFunction.setCount(
                                    UniformGenerator.between(-1.0F, 1.0F)
                            ))
                            .apply(EnchantedCountIncreaseFunction.lootingMultiplier(
                                    this.registries,
                                    UniformGenerator.between(0.0F, 1.0F))
                            )
                    )
                    .when(LootItemKilledByPlayerCondition.killedByPlayer())
                )
        );

        add(ModEntityRegistration.PHARAOH_ENTITY.get(),
                LootTable.lootTable()
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                .add(LootItem.lootTableItem(ModItemRegistration.KEY_FRAGMENT))
                        )
        );
    }
}
