package net.straws11.egyptianpast.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.straws11.egyptianpast.util.CurseUtils;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class PedestalCleansingRecipe extends PedestalRecipe {

    public PedestalCleansingRecipe(Ingredient mainItem, List<Ingredient> childItems, ItemStackTemplate result) {
        super(mainItem, childItems, result);
    }

    @Override
    public boolean matches(PedestalRecipeInput input, @NonNull Level level) {
        // TODO: do i need to accommodate for the output being book/enchanted book here?
        return super.matches(input, level) && CurseUtils.hasCurse(input.mainItem());
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public @NonNull ItemStack assemble(@NonNull PedestalRecipeInput input) {
        return CurseUtils.removeCurse(input.mainItem());
    }
}
