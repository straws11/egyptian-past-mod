package net.straws11.egyptianpast.recipe;

import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;

import javax.annotation.Nullable;
import java.util.List;

@FunctionalInterface
public interface PedestalRecipeFactory<T extends PedestalRecipe>{
    T create(Ingredient mainItem, List<Ingredient> childItems, @Nullable ItemStackTemplate result);
}
