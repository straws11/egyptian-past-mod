package net.straws11.egyptianpast.recipe;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.straws11.egyptianpast.item.ICursedItem;
import net.straws11.egyptianpast.util.CurseUtils;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class PedestalCleansingRecipe extends PedestalRecipe {

    public static final MapCodec<PedestalRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
        Ingredient.CODEC.fieldOf("mainItem").forGetter(PedestalRecipe::getMainItem),
        Ingredient.CODEC.listOf()
            .fieldOf("childItems")
            .flatXmap(PedestalRecipe::validateChildItems, DataResult::success)
            .forGetter(PedestalRecipe::getChildItems)
    ).apply(inst, PedestalCleansingRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, PedestalRecipe> STREAM_CODEC = StreamCodec.of(
        PedestalCleansingRecipe::toNetwork, PedestalCleansingRecipe::fromNetwork
    );

    public PedestalCleansingRecipe(Ingredient mainItem, List<Ingredient> childItems) {
        super(mainItem, childItems);
    }

    @Override
    public boolean matches(PedestalRecipeInput input, @NonNull Level level) {
        return super.matches(input, level) && (
            CurseUtils.hasCurse(input.mainItem())
            || (input.mainItem().getItem() instanceof ICursedItem cursed && cursed.isCursed(input.mainItem()))
        );
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public ItemStackTemplate getResult() {
        return null;
    }

    @Override
    public @NonNull ItemStack assemble(@NonNull PedestalRecipeInput input) {
        if (input.mainItem().getItem() instanceof ICursedItem cursed && cursed.isCursed(input.mainItem())) {
            cursed.cleanse(input.mainItem());
            return input.mainItem();
        }
        return CurseUtils.removeCurse(input.mainItem());
    }

    @Override
    public @NonNull RecipeSerializer<? extends Recipe<PedestalRecipeInput>> getSerializer() {
        return ModRecipes.PEDESTAL_RITUAL_CLEANSING_SERIALIZER.get();
    }

    protected static PedestalRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
        var mainItem = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
        var childItems = Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()).decode(buffer);
        return new PedestalCleansingRecipe(mainItem, childItems);
    }

    protected static void toNetwork(RegistryFriendlyByteBuf buffer, PedestalRecipe recipe) {
        Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.mainItem);
        Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()).encode(buffer, recipe.childItems);
    }

}
