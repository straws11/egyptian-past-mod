package net.straws11.egyptianpast.datagen;


import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.straws11.egyptianpast.EgyptianPast;

import java.util.List;
import java.util.Optional;

public class ModEnchantments {

    public static final ResourceKey<Enchantment> HARVESTING = ResourceKey.create(
        Registries.ENCHANTMENT,
        Identifier.fromNamespaceAndPath(EgyptianPast.MOD_ID, "harvesting")
    );

    public static void bootstrap(BootstrapContext<Enchantment> context) {
        HolderGetter<Item> itemGetter = context.lookup(Registries.ITEM);

        HolderSet<Item> supportedItems = itemGetter.getOrThrow(ItemTags.WEAPON_ENCHANTABLE);
        context.register(
            HARVESTING,
            new Enchantment(
                Component.translatable("enchantment.egyptianpast.harvesting"),
                new Enchantment.EnchantmentDefinition(
                    supportedItems,
                    Optional.of(supportedItems),
                    30,
                    3,
                    Enchantment.dynamicCost(3, 1),
                    Enchantment.dynamicCost(4, 2),
                    2,
                    List.of(EquipmentSlotGroup.MAINHAND)
                ),
                HolderSet.empty(),
                DataComponentMap.EMPTY
            )
        );
    }
}
