package net.straws11.egyptianpast.recipe;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.straws11.egyptianpast.EgyptianPast;

import java.util.function.Supplier;

public class ModRecipes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
        DeferredRegister.create(Registries.RECIPE_TYPE, EgyptianPast.MOD_ID);

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
        DeferredRegister.create(Registries.RECIPE_SERIALIZER, EgyptianPast.MOD_ID);

    public static final Supplier<RecipeType<PedestalRecipe>> PEDESTAL_RITUAL_TYPE =
        RECIPE_TYPES.register("pedestal_ritual", RecipeType::simple);

    public static final Supplier<RecipeSerializer<PedestalRecipe>> PEDESTAL_RITUAL_SERIALIZER =
        RECIPE_SERIALIZERS.register(
            "pedestal_ritual",
            () -> new RecipeSerializer<>(PedestalRecipe.CODEC, PedestalRecipe.STREAM_CODEC)
        );

    public static void register(IEventBus eventBus) {
        RECIPE_TYPES.register(eventBus);
        RECIPE_SERIALIZERS.register(eventBus);
    }
}
