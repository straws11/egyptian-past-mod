package net.straws11.egyptianpast.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class PedestalInfusingRecipe extends PedestalRecipe {

    public PedestalInfusingRecipe(Ingredient mainItem, List<Ingredient> childItems, ItemStackTemplate result) {
        super(mainItem, childItems, result);
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public @NonNull ItemStack assemble(@NonNull PedestalRecipeInput input) {
        assert this.result != null;
        return this.result.create();
    }
}
