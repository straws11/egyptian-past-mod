package net.straws11.egyptianpast.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.minecraft.world.item.equipment.Equippable;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import static net.straws11.egyptianpast.EgyptianPast.MOD_ID;
import static net.straws11.egyptianpast.entity.ModEntityRegistration.MUMMY_ENTITY;
import static net.straws11.egyptianpast.entity.ModEntityRegistration.PHARAOH_ENTITY;

public class ModItemRegistration {
    // Create a Deferred Register to hold Items which will all be registered under the "egyptianpast" namespace
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MOD_ID);

    public static final DeferredItem<Item> POMEGRANATE =
            ITEMS.registerSimpleItem("pomegranate", p -> p.food(new FoodProperties.Builder()
                    .nutrition(1).saturationModifier(0.2f).build()));

    public static final DeferredItem<Item> PAPYRUS_SHEET =
            ITEMS.registerSimpleItem("papyrus_sheet");

    public static final DeferredItem<Item> MUMMY_SPAWN_EGG =
            ITEMS.registerItem("mummy_spawn_egg", properties -> new SpawnEggItem(
                    properties.spawnEgg(MUMMY_ENTITY.get())
            ));

    public static final DeferredItem<Item> PHARAOH_SPAWN_EGG =
            ITEMS.registerItem("pharaoh_spawn_egg", properties -> new SpawnEggItem(
                    properties.spawnEgg(PHARAOH_ENTITY.get())
            ));

    public static final DeferredItem<AnkhOfLifeItem> ANKH_OF_LIFE =
            ITEMS.registerItem("ankh_of_life",
                    p -> new AnkhOfLifeItem(p.useCooldown(30f).stacksTo(1).rarity(Rarity.RARE))
            );

    public static final DeferredItem<MummyWrapItem> MUMMY_WRAP =
            ITEMS.registerItem("mummy_wrap",
            p -> new MummyWrapItem(p)
    );

    public static final DeferredItem<CryptKey> CRYPT_KEY =
            ITEMS.registerItem("crypt_key", p -> new CryptKey(p.rarity(Rarity.EPIC).stacksTo(1)));

    public static final DeferredItem<Item> KEY_FRAGMENT =
            ITEMS.registerSimpleItem("key_fragment");

    public static final DeferredItem<PharaohCrown> PHARAOH_CROWN =
            ITEMS.registerItem("pharaoh_crown",p -> new PharaohCrown(
                    p.humanoidArmor(ModArmorMaterials.PHARAOH_ARMOR_MATERIAL, ArmorType.HELMET)
                            .rarity(Rarity.EPIC))
            );

    public static final DeferredItem<CanopicJar> CANOPIC_JAR =
            ITEMS.registerItem("canopic_jar", p ->
                    new CanopicJar(p.stacksTo(16))
            );

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
