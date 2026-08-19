package net.straws11.egyptianpast.recipe;

import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class PedestalRecipeBuilder extends SimpleRecipeBuilder {

    private final Ingredient mainItem;
    private final List<Ingredient> childItems;

    public PedestalRecipeBuilder(ItemStackTemplate result, RecipeCategory category,
                                 Ingredient mainItem, List<Ingredient> childItems) {
        super(result, category);
        this.mainItem = mainItem;
        this.childItems = childItems;
    }

    @Override
    public RecipeBuilder unlockedBy(String s, Criterion<?> criterion) {
        this.advancementBuilder.unlockedBy(s, criterion);
        return this;
    }

    @Override
    public void save(RecipeOutput recipeOutput, @NonNull ResourceKey<Recipe<?>> resourceKey) {
        PedestalRecipe recipe = new PedestalRecipe(
            this.mainItem,
            this.childItems,
            this.result
        );

        recipeOutput.accept(resourceKey, recipe,
            this.advancementBuilder.build(recipeOutput, resourceKey, this.category));

    }
}
