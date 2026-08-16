package net.straws11.egyptianpast.datagen.villager;

import net.minecraft.advancements.predicates.DataComponentMatchers;
import net.minecraft.advancements.predicates.EnchantmentPredicate;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.advancements.predicates.MinMaxBounds;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.component.predicates.DataComponentPredicates;
import net.minecraft.core.component.predicates.EnchantmentsPredicate;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.trading.TradeCost;
import net.minecraft.world.item.trading.VillagerTrade;
import net.minecraft.world.item.trading.VillagerTrades;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.functions.DiscardItem;
import net.minecraft.world.level.storage.loot.functions.FilteredFunction;
import net.minecraft.world.level.storage.loot.functions.SetEnchantmentsFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.straws11.egyptianpast.EgyptianPast;
import net.straws11.egyptianpast.block.ModBlocks;
import net.straws11.egyptianpast.datagen.ModEnchantments;
import net.straws11.egyptianpast.item.ModItems;

import java.util.List;
import java.util.Optional;

public class ModVillagerTrades {

    public static final ResourceKey<VillagerTrade> ANCIENT_EGYPTIAN_1_EMERALD_MUMMY_WRAP =
        createKey("ancient_egyptian/1/emerald_mummy_wrap");

    public static final ResourceKey<VillagerTrade> ANCIENT_EGYPTIAN_1_EMERALD_POMEGRANATE_SEEDS =
        createKey("ancient_egyptian/1/emerald_pomegranate_seeds");

    public static final ResourceKey<VillagerTrade> ANCIENT_EGYPTIAN_2_EMERALD_AND_BOOK_HARVESTING_BOOK =
        createKey("ancient_egyptian/2/emerald_harvesting_book");

    public static void bootstrap(BootstrapContext<VillagerTrade> context) {
        HolderGetter<Enchantment> enchantments = context.lookup(Registries.ENCHANTMENT);
        HolderGetter<Item> items = context.lookup(Registries.ITEM);

        context.register(ANCIENT_EGYPTIAN_1_EMERALD_MUMMY_WRAP, new VillagerTrade(
            new TradeCost(Items.EMERALD, UniformGenerator.between(1f, 5f)),
            new ItemStackTemplate(ModItems.MUMMY_WRAP, 16),
            12, 6, 0.05f, Optional.empty(), List.of()
        ));

        context.register(ANCIENT_EGYPTIAN_1_EMERALD_POMEGRANATE_SEEDS, new VillagerTrade(
            new TradeCost(Items.EMERALD, UniformGenerator.between(1f, 3f)),
            new ItemStackTemplate(ModBlocks.POMEGRANATE_SEEDS, 4),
            12, 2, 0.05f, Optional.empty(), List.of()
        ));

        context.register(ANCIENT_EGYPTIAN_2_EMERALD_AND_BOOK_HARVESTING_BOOK, new VillagerTrade(
            new TradeCost(Items.EMERALD, UniformGenerator.between(12f, 32f)),
            Optional.of(new TradeCost(Items.BOOK, 1)),
            new ItemStackTemplate(Items.ENCHANTED_BOOK, 1),
            12, 1, 0.05f, Optional.empty(),
            VillagerTrades.enchantedBook(items, enchantments.getOrThrow(ModEnchantments.HARVESTING), 1)
        ));
    }

    private static ResourceKey<VillagerTrade> createKey(String name) {
        return ResourceKey.create(Registries.VILLAGER_TRADE,
            Identifier.fromNamespaceAndPath(EgyptianPast.MOD_ID, name));
    }
}
