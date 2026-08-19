package net.straws11.egyptianpast.datagen;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.advancements.predicates.EnchantmentPredicate;
import net.minecraft.advancements.predicates.MinMaxBounds;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentExactPredicate;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.predicates.DataComponentPredicate;
import net.minecraft.core.component.predicates.DataComponentPredicates;
import net.minecraft.core.component.predicates.EnchantmentsPredicate;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;
import net.straws11.egyptianpast.block.ModBlocks;
import net.straws11.egyptianpast.data.ModDataComponentRegistration;
import net.straws11.egyptianpast.item.CanopicJarBlockItem;
import net.straws11.egyptianpast.item.ModItems;
import net.straws11.egyptianpast.item.OrganType;
import net.straws11.egyptianpast.item.PharaohCrown;
import net.straws11.egyptianpast.recipe.PedestalCleansingRecipe;
import net.straws11.egyptianpast.recipe.PedestalRecipeBuilder;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        shaped(RecipeCategory.MISC, ModItems.PAPYRUS_SHEET.get(), 2)
            .pattern("AAA")
            .define('A', ModBlocks.PAPYRUS_REED.get())
            .unlockedBy(getHasName(ModBlocks.PAPYRUS_REED.get()),
                    has(ModBlocks.PAPYRUS_REED.get()))
            .group("papyrus")
            .save(output);

        shaped(RecipeCategory.MISC, ModItems.CRYPT_KEY.get(), 1)
            .pattern("AAA")
            .pattern("A A")
            .pattern(" A ")
            .define('A', ModItems.KEY_FRAGMENT.get())
            .unlockedBy(getHasName(ModItems.KEY_FRAGMENT.get()), has(ModItems.KEY_FRAGMENT.get()))
            .group("crypt_key")
            .save(output);

        shaped(RecipeCategory.MISC, ModBlocks.CANOPIC_JAR.get(), 1)
            .pattern("AAA")
            .pattern("B B")
            .pattern(" B ")
            .define('A', Blocks.SMOOTH_STONE_SLAB)
            .define('B', ItemTags.TERRACOTTA)
            .unlockedBy(getHasName(Blocks.SMOOTH_STONE_SLAB), has(Blocks.SMOOTH_STONE_SLAB))
            .group("canopic_jar")
            .save(output);

        shaped(RecipeCategory.MISC, ModBlocks.PEDESTAL_BLOCK.get(), 1)
            .pattern("AAA")
            .pattern(" G ")
            .pattern("AAA")
            .define('A', Blocks.BLACKSTONE)
            .define('G', Blocks.GOLD_BLOCK)
            .unlockedBy(getHasName(Blocks.BLACKSTONE), has(Blocks.BLACKSTONE))
            .group("pedestal")
            .save(output);

        PedestalRecipeBuilder.cleansing(
            cursedCrown(),
            uniqueCanopicJars(),
            new ItemStackTemplate(ModItems.PHARAOH_CROWN)
        )
            .unlockedBy("has_main", has(ModItems.PHARAOH_CROWN))
            .save(output);

        PedestalRecipeBuilder.cleansing(
            Ingredient.of(Items.ENCHANTED_BOOK),
            uniqueCanopicJars(),
            new ItemStackTemplate(Items.ENCHANTED_BOOK)
        )
            .unlockedBy("has_main", has(Items.ENCHANTED_BOOK))
            .save(output);

        PedestalRecipeBuilder.cleansing(
                Ingredient.of(Items.ENCHANTED_BOOK),
                uniqueCanopicJars(),
                new ItemStackTemplate(Items.ENCHANTED_BOOK)
            )
            .unlockedBy("has_main", has(Items.BOOK))
            .save(output);

        PedestalRecipeBuilder.cleansing(
                enchantableTool(),
                uniqueCanopicJars(),
                new ItemStackTemplate(Items.ENCHANTED_BOOK)
            )
            .unlockedBy("has_main", has(Items.BOOK))
            .save(output);
    }

    // --- HELPERS ---

    private Ingredient canopicJarWithOrgan(OrganType organ) {
        var predicate = DataComponentPatch.builder().set(ModDataComponentRegistration.ORGAN_TYPE.get(), organ).build();

        return DataComponentIngredient.of(
            false,
            predicate,
            ModBlocks.CANOPIC_JAR.get()
        );
    }

    private Ingredient enchantableTool() {
        var itemLookup = this.registries.lookupOrThrow(Registries.ITEM);

        return Ingredient.of(
            itemLookup.getOrThrow(ItemTags.ARMOR_ENCHANTABLE),
            itemLookup.getOrThrow(ItemTags.WEAPON_ENCHANTABLE)
        );
    }

    private List<Ingredient> uniqueCanopicJars() {
        return Arrays.stream(OrganType.values())
            .filter(x -> x != OrganType.EMPTY)
            .map(this::canopicJarWithOrgan)
            .toList();
    }

    private Ingredient cursedCrown() {
        // doing this roundabout way because I can't just instantiate an ItemStack during datagen

        var predicate = DataComponentPatch.builder().set(ModDataComponentRegistration.IS_CURSED.get(), true).build();

        return DataComponentIngredient.of(
            false,
            predicate,
            ModItems.PHARAOH_CROWN.get()
        );
    }
}
