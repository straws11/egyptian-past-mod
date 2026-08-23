package net.straws11.egyptianpast.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.straws11.egyptianpast.item.AnkhOfLifeItem;
import net.straws11.egyptianpast.item.ModItems;

public class AnkhChargeRecipe extends CustomRecipe {

    public static final MapCodec<AnkhChargeRecipe> CODEC = RecordCodecBuilder.mapCodec(
        inst -> inst.group(CraftingBookCategory.CODEC.fieldOf("category")
            .forGetter(CraftingRecipe::category)).apply(inst, AnkhChargeRecipe::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, AnkhChargeRecipe> STREAM_CODEC = StreamCodec.of(
        (buf, r) -> buf.writeEnum(r.category()),
        buf -> new AnkhChargeRecipe(buf.readEnum(CraftingBookCategory.class))
    );

    public AnkhChargeRecipe(CraftingBookCategory category) {
        super();
    }

    @Override
    public boolean matches(CraftingInput craftingInput, Level level) {
        ItemStack foundAnkh = ItemStack.EMPTY;
        int totemCount = 0;

        for (int i = 0; i < craftingInput.size(); i++) {
            ItemStack stack = craftingInput.getItem(i);
            if (stack.isEmpty()) continue;

            if (stack.is(ModItems.ANKH_OF_LIFE.get())) {
                if (!foundAnkh.isEmpty()) return false;
                foundAnkh = stack;
            } else if (stack.is(Items.TOTEM_OF_UNDYING)) {
                totemCount++;
            } else {
                return false; // invalid item
            }
        }

        if (foundAnkh.isEmpty() || totemCount == 0) return false;
        int currentCharges = AnkhOfLifeItem.getCharges(foundAnkh);

        return (currentCharges + totemCount) <= AnkhOfLifeItem.MAX_TOTEMS;
    }

    @Override
    public ItemStack assemble(CraftingInput craftingInput) {
        ItemStack foundAnkh = ItemStack.EMPTY;
        int totemCount = 0;

        for (int i = 0; i < craftingInput.size(); i++) {
            ItemStack stack = craftingInput.getItem(i);
            if (stack.isEmpty()) continue;

            if (stack.is(ModItems.ANKH_OF_LIFE)) {
                foundAnkh = stack;
            } else if (stack.is(Items.TOTEM_OF_UNDYING)) {
                totemCount++;
            }
        }

        if (foundAnkh.isEmpty()) return ItemStack.EMPTY;

        ItemStack result = foundAnkh.copy();
        int newCharges = AnkhOfLifeItem.getCharges(foundAnkh) + totemCount;
        AnkhOfLifeItem.setCharges(result, newCharges);
        return result;
    }


    @Override
    public RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return ModRecipes.ANKH_CHARGE_RECIPE_SERIALIZER.get();
    }
}
