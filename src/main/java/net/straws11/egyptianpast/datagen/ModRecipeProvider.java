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
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;
import net.straws11.egyptianpast.EgyptianPast;
import net.straws11.egyptianpast.block.ModBlocks;
import net.straws11.egyptianpast.data.ModDataComponentRegistration;
import net.straws11.egyptianpast.item.*;
import net.straws11.egyptianpast.recipe.AnkhChargeRecipe;
import net.straws11.egyptianpast.recipe.PedestalCleansingRecipe;
import net.straws11.egyptianpast.recipe.PedestalRecipe;
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

        shaped(RecipeCategory.MISC, ModItems.EGYPTIAN_SCROLL.get(), 1)
            .pattern("AA")
            .pattern("AA")
            .pattern("AA")
            .define('A', ModItems.PAPYRUS_SHEET)
            .unlockedBy(getHasName(ModItems.PAPYRUS_SHEET), has(ModItems.PAPYRUS_SHEET))
            .group("egyptian_scroll")
            .save(output);

        shapeless(RecipeCategory.DECORATIONS, Items.DYE.red(), 1)
            .requires(ModItems.POMEGRANATE)
            .unlockedBy(getHasName(ModItems.POMEGRANATE), has(ModItems.POMEGRANATE))
            .group("dye")
            .save(output);

        PedestalRecipeBuilder.cleansing(
            cursedCrown(),
            uniqueCanopicJars()
        )
        .unlockedBy("has_main", has(ModItems.PHARAOH_CROWN))
        .save(output, ResourceKey.create(Registries.RECIPE,
            Identifier.fromNamespaceAndPath(EgyptianPast.MOD_ID, "cleanse_crown")));

        PedestalRecipeBuilder.cleansing(
            Ingredient.of(Items.ENCHANTED_BOOK),
            uniqueCanopicJars()
        )
        .unlockedBy("has_main", has(Items.ENCHANTED_BOOK))
        .save(output, ResourceKey.create(Registries.RECIPE,
            Identifier.fromNamespaceAndPath(EgyptianPast.MOD_ID, "cleanse_enchanted_book"))
        );

        PedestalRecipeBuilder.cleansing(
            this.tag(Tags.Items.TOOLS),
            uniqueCanopicJars()
        )
        .unlockedBy("has_main", has(Tags.Items.TOOLS))
        .save(output, ResourceKey.create(Registries.RECIPE,
            Identifier.fromNamespaceAndPath(EgyptianPast.MOD_ID, "cleanse_tools"))
        );

        PedestalRecipeBuilder.cleansing(
                this.tag(Tags.Items.ARMORS),
                uniqueCanopicJars()
            )
            .unlockedBy("has_main", has(Tags.Items.ARMORS))
            .save(output, ResourceKey.create(Registries.RECIPE,
                Identifier.fromNamespaceAndPath(EgyptianPast.MOD_ID, "cleanse_armor"))
            );

        PedestalRecipeBuilder.infusing(
            Ingredient.of(Items.TOTEM_OF_UNDYING),
            Stream.of(Items.DIAMOND, Items.NETHER_STAR, Items.WITHER_SKELETON_SKULL, Items.NETHERITE_INGOT)
                .map(Ingredient::of)
                .collect(Collectors.toList()),
            new ItemStackTemplate(ModItems.ANKH_OF_LIFE)
        )
        .unlockedBy(getHasName(Items.TOTEM_OF_UNDYING), has(Items.TOTEM_OF_UNDYING))
        .save(output);

        PedestalRecipeBuilder.infusing(
            scrollWithType(ScrollType.BLANK),
            Stream.of(Items.FIRE_CHARGE, Items.GOLD_NUGGET, Items.SUNFLOWER, Items.GOLD_NUGGET)
                .map(Ingredient::of)
                .collect(Collectors.toList()),
            scrollStackTemplate(ScrollType.SUN_STRIKE)
        )
        .unlockedBy(getHasName(ModItems.EGYPTIAN_SCROLL), has(ModItems.EGYPTIAN_SCROLL))
        .save(output, ResourceKey.create(Registries.RECIPE,
                Identifier.fromNamespaceAndPath(EgyptianPast.MOD_ID,
                    "infuse_scroll_" + ScrollType.SUN_STRIKE.getSerializedName())
            )
        );

        SpecialRecipeBuilder.special(
            () -> new AnkhChargeRecipe(CraftingBookCategory.MISC))
            .save(output, "ankh_charging");
    }

    private ItemStackTemplate scrollStackTemplate(ScrollType type) {
        var predicate = DataComponentPatch.builder().set(ModDataComponentRegistration.SCROLL_TYPE.get(), type).build();
        return new ItemStackTemplate(ModItems.EGYPTIAN_SCROLL.get(), predicate);
    }
    private Ingredient scrollWithType(ScrollType type) {
        var predicate = DataComponentPatch.builder().set(ModDataComponentRegistration.SCROLL_TYPE.get(), type).build();
        return DataComponentIngredient.of(
            false,
            predicate,
            ModItems.EGYPTIAN_SCROLL.get()
        );
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

        return this.tag(ItemTags.WEAPON_ENCHANTABLE);
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
