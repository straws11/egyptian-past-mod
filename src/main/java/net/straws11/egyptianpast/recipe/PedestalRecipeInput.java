package net.straws11.egyptianpast.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

import java.util.List;

public record PedestalRecipeInput(ItemStack mainItem, List<ItemStack> childItems) implements RecipeInput {

    @Override
    public ItemStack getItem(int slot) {
        if (slot < 0 || slot > this.childItems().size()) throw new IllegalArgumentException("No item for index: " + slot);

        if (slot == 0) return this.mainItem;

        // order doesn't matter so really any of the 4 i guess
        return this.childItems.get(slot-1);
    }

    @Override
    public int size() {
        return this.childItems().size() + 1;
    }
}
