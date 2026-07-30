package net.straws11.egyptianpast.item;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import static net.straws11.egyptianpast.EgyptianPast.MOD_ID;
import static net.straws11.egyptianpast.entity.ModEntityRegistration.MUMMY_ENTITY;

public class ModItemRegistration {
    // Create a Deferred Register to hold Items which will all be registered under the "egyptianpast" namespace
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MOD_ID);

    // Creates a new food item with the id "egyptianpast:example_id", nutrition 1 and saturation 2
    public static final DeferredItem<Item> EXAMPLE_ITEM =
            ITEMS.registerSimpleItem("example_item", p -> p.food(new FoodProperties.Builder()
                    .alwaysEdible().nutrition(1).saturationModifier(2f).build()));

    public static final DeferredItem<Item> PAPYRUS_SHEET =
            ITEMS.registerSimpleItem("papyrus_sheet");

    public static final DeferredItem<Item> MUMMY_SPAWN_EGG =
            ITEMS.registerItem("mummy_spawn_egg", properties -> new SpawnEggItem(
                    properties.spawnEgg(MUMMY_ENTITY.get())
            ));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
