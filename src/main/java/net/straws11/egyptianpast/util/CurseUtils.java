package net.straws11.egyptianpast.util;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;

/**
 * General helpers for detecting and removing curses off items and books
 * Applies to items/equipment with #minecraft:curse tag on
 */
public class CurseUtils {

    public static boolean hasCurse(ItemStack item) {
        ItemEnchantments enchants =  EnchantmentHelper.getEnchantmentsForCrafting(item);
        for (Holder<Enchantment> holder: enchants.keySet()) {
            if (holder.is(EnchantmentTags.CURSE)) return true;
        }
        return false;
    }

    public static ItemStack removeCurse(ItemStack item) {
        boolean isEnchantedBook = item.is(Items.ENCHANTED_BOOK);
        var componentType = isEnchantedBook
            ? DataComponents.STORED_ENCHANTMENTS
            : DataComponents.ENCHANTMENTS;
        ItemEnchantments currentEnchants = item.getOrDefault(componentType, ItemEnchantments.EMPTY);

        assert !currentEnchants.isEmpty();

        ItemEnchantments.Mutable mutable = new ItemEnchantments.Mutable(currentEnchants);
        mutable.removeIf(holder -> holder.is(EnchantmentTags.CURSE));
        ItemEnchantments cleansedEnchants = mutable.toImmutable();

        if (isEnchantedBook) {
            return removeBookCurse(item, cleansedEnchants);
        } else {
            return removeEquipmentCurse(item, cleansedEnchants);
        }
    }

    private static ItemStack removeEquipmentCurse(ItemStack item, ItemEnchantments cleansedEnchants) {
        if (cleansedEnchants.isEmpty()) {
            item.remove(DataComponents.ENCHANTMENTS);
        } else {
            item.set(DataComponents.ENCHANTMENTS, cleansedEnchants);
        }
        return item;
    }

    private static ItemStack removeBookCurse(ItemStack item, ItemEnchantments cleansedEnchants) {
        if (cleansedEnchants.isEmpty()) {
            ItemStack normalBook = new ItemStack(Items.BOOK, item.getCount());

            // copy custom name if relevant
            if (item.has(DataComponents.CUSTOM_NAME)) {
                normalBook.set(DataComponents.CUSTOM_NAME, item.get(DataComponents.CUSTOM_NAME));
            }
            return normalBook;
        }
        item.set(DataComponents.STORED_ENCHANTMENTS, cleansedEnchants);
        return item;
    }
}
