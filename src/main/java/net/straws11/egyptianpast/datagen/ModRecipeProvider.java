package net.straws11.egyptianpast.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.straws11.egyptianpast.block.ModBlockRegistration;
import net.straws11.egyptianpast.item.ModItemRegistration;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {

    public ModRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    public static class Runner extends RecipeProvider.Runner {
        public Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
            super(packOutput, registries);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
            return new ModRecipeProvider(registries, output);
        }

        @Override
        public String getName() {
            return "EgyptianPast Recipes";
        }
    }

    @Override
    protected void buildRecipes() {
        shaped(RecipeCategory.MISC, ModItemRegistration.PAPYRUS_SHEET.get(), 2)
                .pattern("AAA")
                .define('A', ModBlockRegistration.PAPYRUS_REED.get())
                .unlockedBy(getHasName(ModBlockRegistration.PAPYRUS_REED.get()),
                        has(ModBlockRegistration.PAPYRUS_REED.get()))
                .group("papyrus")
                .save(output);

        shaped(RecipeCategory.MISC, ModItemRegistration.CRYPT_KEY.get(), 1)
                .pattern("AAA")
                .pattern("A A")
                .pattern(" A ")
                .define('A', ModItemRegistration.KEY_FRAGMENT.get())
                .unlockedBy(getHasName(ModItemRegistration.KEY_FRAGMENT.get()), has(ModItemRegistration.KEY_FRAGMENT.get()))
                .group("crypt_key")
                .save(output);

    }
}
