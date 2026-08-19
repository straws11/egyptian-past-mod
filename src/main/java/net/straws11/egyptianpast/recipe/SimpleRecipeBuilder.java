package net.straws11.egyptianpast.recipe;

import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeUnlockAdvancementBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Recipe;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

public abstract class SimpleRecipeBuilder implements RecipeBuilder {
    protected final ItemStackTemplate result;
    protected String group = "";
    protected boolean showNotifications = false;

    protected final RecipeUnlockAdvancementBuilder advancementBuilder;
    protected final RecipeCategory category;

    public SimpleRecipeBuilder(ItemStackTemplate result, RecipeCategory category) {
        this.result = result;
        this.category = category;
        this.advancementBuilder = new RecipeUnlockAdvancementBuilder();
    }

    @Override
    public RecipeBuilder group(@Nullable String group) {
        this.group = Objects.requireNonNullElse(group, "");
        return this;
    }

    @Override
    public ResourceKey<Recipe<?>> defaultId() {
        return RecipeBuilder.getDefaultRecipeId(this.result);
    }

    public SimpleRecipeBuilder showNotifications(boolean show) {
        this.showNotifications = show;
        return this;
    }
}
