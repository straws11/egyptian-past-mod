package net.straws11.egyptianpast.recipe;

import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class PedestalRecipeBuilder extends SimpleRecipeBuilder {

    private final Ingredient mainItem;
    private final List<Ingredient> childItems;
    private final PedestalRecipeFactory<?> factory;

    public PedestalRecipeBuilder(ItemStackTemplate result, RecipeCategory category,
                                 Ingredient mainItem, List<Ingredient> childItems) {
        this(result, category, mainItem, childItems, PedestalRecipe::new);
    }

    public <T extends PedestalRecipe> PedestalRecipeBuilder(ItemStackTemplate result, RecipeCategory category,
                                 Ingredient mainItem, List<Ingredient> childItems, PedestalRecipeFactory<T> factory) {
        super(result, category);
        this.mainItem = mainItem;
        this.childItems = childItems;
        this.factory = factory;
    }

    /**
     * Creates builder for a cleansing recipe with no output item
     * @param mainItem central pedestal item
     * @param childItems surrounding pedestal items
     * @return PedestalRecipeBuilder
     */
    public static PedestalRecipeBuilder cleansing(Ingredient mainItem, List<Ingredient> childItems) {
        return new PedestalRecipeBuilder(
            null,
            RecipeCategory.MISC, mainItem, childItems,
            (main, child, ignoredResult) -> new PedestalCleansingRecipe(main, child));
    }

    @Override
    public @NonNull RecipeBuilder unlockedBy(String s, Criterion<?> criterion) {
        this.advancementBuilder.unlockedBy(s, criterion);
        return this;
    }

    @Override
    public void save(RecipeOutput recipeOutput, @NonNull ResourceKey<Recipe<?>> resourceKey) {
        PedestalRecipe recipe = this.factory.create(
            this.mainItem,
            this.childItems,
            this.result
        );

        recipeOutput.accept(resourceKey, recipe,
            this.advancementBuilder.build(recipeOutput, resourceKey, this.category));
    }
}
