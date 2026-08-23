package net.straws11.egyptianpast.recipe;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PedestalRecipe implements Recipe<PedestalRecipeInput> {

    protected final Ingredient mainItem;
    protected final List<Ingredient> childItems;
    protected final ItemStackTemplate result;

    public static final MapCodec<PedestalRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
        Ingredient.CODEC.fieldOf("mainItem").forGetter(PedestalRecipe::getMainItem),
        Ingredient.CODEC.listOf()
            .fieldOf("childItems")
            .flatXmap(PedestalRecipe::validateChildItems, DataResult::success)
            .forGetter(PedestalRecipe::getChildItems),
        ItemStackTemplate.CODEC.fieldOf("result").forGetter(PedestalRecipe::getResult)
    ).apply(inst, PedestalRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, PedestalRecipe> STREAM_CODEC = StreamCodec.of(
        PedestalRecipe::toNetwork, PedestalRecipe::fromNetwork
    );

    public PedestalRecipe(Ingredient mainItem, List<Ingredient> childItems, ItemStackTemplate result) {
        this.mainItem = mainItem;
        this.childItems = childItems;
        this.result = result;
    }

    // constructor for no static result item
    public PedestalRecipe(Ingredient mainItem, List<Ingredient> childItems) {
        this.mainItem = mainItem;
        this.childItems = childItems;
        this.result = null;
    }

    public Ingredient getMainItem() {
        return this.mainItem;
    }

    public List<Ingredient> getChildItems() {
        return this.childItems;
    }

    public ItemStackTemplate getResult() {
        return this.result == null ? new ItemStackTemplate(Items.EMERALD) : this.result;
    }

    protected static DataResult<List<Ingredient>> validateChildItems(List<Ingredient> childItems) {
        int max = 4;
        var childItemsArray = childItems.toArray(Ingredient[]::new);
        if (childItemsArray.length == 0) {
            return DataResult.error(() -> "No ingredients for pedestal ritual recipe");
        }

        return childItemsArray.length > max
            ? DataResult.error(() -> "Too many ingredients for pedestal ritual recipe")
            : DataResult.success(Arrays.stream(childItemsArray).toList());
    }

    @Override
    public boolean matches(PedestalRecipeInput input, @NonNull Level level) {
        if (!this.mainItem.test(input.mainItem())) return false;
        System.out.println("matching recipe..");

        List<ItemStack> nonNullInputs = input.childItems().stream()
            .filter(stack -> !stack.isEmpty())
            .toList();

        if (nonNullInputs.size() != this.childItems.size()) return false;

        List<ItemStack> remainingInputs = new ArrayList<>(nonNullInputs);

        // filter out for each item to ensure all are present
        for (Ingredient ingredient : this.childItems) {
            boolean matched = false;
            for (int i = 0; i < remainingInputs.size(); i++) {
                if (ingredient.test(remainingInputs.get(i))) {
                    remainingInputs.remove(i);
                    matched = true;
                    break;
                }
            }
            if (!matched) return false;
        }
        System.out.println(remainingInputs.isEmpty());
        return remainingInputs.isEmpty();
    }

    @Override
    public @NonNull ItemStack assemble(@NonNull PedestalRecipeInput input) {
        return this.result != null ? this.result.create() : ItemStack.EMPTY;
    }

    @Override
    public boolean showNotification() {
        return false;
    }

    @Override
    public @NonNull String group() {
        return "egyptianpast:pedestal_ritual";
    }

    @Override
    public @NonNull RecipeSerializer<? extends Recipe<PedestalRecipeInput>> getSerializer() {
        return ModRecipes.PEDESTAL_RITUAL_SERIALIZER.get();
    }

    @Override
    public @NonNull RecipeType<? extends Recipe<PedestalRecipeInput>> getType() {
        return ModRecipes.PEDESTAL_RITUAL_TYPE.get();
    }

    @Override
    public @NonNull PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public @NonNull RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }

    protected static PedestalRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
        var mainItem = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
        var childItems = Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()).decode(buffer);
        var result = ItemStackTemplate.STREAM_CODEC.decode(buffer);
        return new PedestalRecipe(mainItem, childItems, result);
    }

    protected static void toNetwork(RegistryFriendlyByteBuf buffer, PedestalRecipe recipe) {
        Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.mainItem);
        Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()).encode(buffer, recipe.childItems);
        ItemStackTemplate.STREAM_CODEC.encode(buffer, recipe.result);
    }

}
