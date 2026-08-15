package net.straws11.egyptianpast.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.straws11.egyptianpast.EgyptianPast;
import net.straws11.egyptianpast.loot.OrganHarvestLootModifier;

import java.util.concurrent.CompletableFuture;

public class ModGlobalLootModifierProvider extends GlobalLootModifierProvider {

    public ModGlobalLootModifierProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, EgyptianPast.MOD_ID);
    }

    @Override
    protected void start() {
        add("organ_harvest_from_zombies", new OrganHarvestLootModifier(
            new LootItemCondition[] {
                LootItemKilledByPlayerCondition.killedByPlayer().build()
            },
            10
        ));

    }
}
