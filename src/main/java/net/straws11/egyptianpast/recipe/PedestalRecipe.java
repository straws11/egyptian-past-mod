package net.straws11.egyptianpast.recipe;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PedestalRecipe implements Recipe<PedestalRecipeInput> {

    private final Ingredient mainItem;
    private final List<Ingredient> childItems;
    private final ItemStackTemplate result;

    public static final MapCodec<PedestalRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
        Ingredient.CODEC.fieldOf("mainItem").forGetter(PedestalRecipe::getMainItem),
        Ingredient.CODEC.listOf()
            .fieldOf("childItems")
            .flatXmap(field -> {
                int max = 4;
                var childItems = field.toArray(Ingredient[]::new);
                if (childItems.length == 0) {
                    return DataResult.error(() -> "No ingredients for pedestal ritual recipe");
                } else {
                    return childItems.length > max
                        ? DataResult.error(() -> "Too many ingredients for pedestal ritual recipe")
                        : DataResult.success(Arrays.stream(childItems).toList());
                }
            },
                DataResult::success
            )
            .forGetter(PedestalRecipe::getChildItems),
        ItemStackTemplate.CODEC.fieldOf("result").forGetter(recipe -> recipe.result)
    ).apply(inst, PedestalRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, PedestalRecipe> STREAM_CODEC = StreamCodec.of(
        PedestalRecipe::toNetwork, PedestalRecipe::fromNetwork
    );

    public PedestalRecipe(Ingredient mainItem, List<Ingredient> childItems, ItemStackTemplate result) {
        this.mainItem = mainItem;
        this.childItems = childItems;
        this.result = result;
    }

    public Ingredient getMainItem() {
        return this.mainItem;
    }

    public List<Ingredient> getChildItems() {
        return this.childItems;
    }

    @Override
    public boolean matches(PedestalRecipeInput input, Level level) {
        if (!this.mainItem.test(input.mainItem())) return false;

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
        return remainingInputs.isEmpty();
    }

    @Override
    public ItemStack assemble(PedestalRecipeInput input) {
        return this.result.create();
    }

    @Override
    public boolean isSpecial() {
        // TODO: check if need to override?
        return true;
    }

    @Override
    public boolean showNotification() {
        return false;
    }

    @Override
    public String group() {
        return "egyptianpast:pedestal_ritual";
    }

    @Override
    public RecipeSerializer<? extends Recipe<PedestalRecipeInput>> getSerializer() {
        return ModRecipes.PEDESTAL_RITUAL_SERIALIZER.get();
    }

    @Override
    public RecipeType<? extends Recipe<PedestalRecipeInput>> getType() {
        return ModRecipes.PEDESTAL_RITUAL_TYPE.get();
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }

    private static PedestalRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
        var mainItem = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
        var childItems = Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()).decode(buffer);
        var result = ItemStackTemplate.STREAM_CODEC.decode(buffer);
        return new PedestalRecipe(mainItem, childItems, result);
    }

    private static void toNetwork(RegistryFriendlyByteBuf buffer, PedestalRecipe recipe) {
        Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.mainItem);
        Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()).encode(buffer, recipe.childItems);
        ItemStackTemplate.STREAM_CODEC.encode(buffer, recipe.result);
    }

}
