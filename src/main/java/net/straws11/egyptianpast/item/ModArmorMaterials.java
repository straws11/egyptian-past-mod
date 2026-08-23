package net.straws11.egyptianpast.item;

import com.google.common.collect.Maps;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.straws11.egyptianpast.EgyptianPast;
import net.straws11.egyptianpast.tags.ModTags;

import java.util.Map;

public class ModArmorMaterials {
    public static final ResourceKey<? extends Registry<EquipmentAsset>> ROOT_ID = ResourceKey.createRegistryKey(
           Identifier.withDefaultNamespace("equipment_asset")
    );

    public static final ResourceKey<EquipmentAsset> PHARAOH_ARMOR_KEY =
            ResourceKey.create(ROOT_ID, Identifier.fromNamespaceAndPath(EgyptianPast.MOD_ID, "pharaoh"));

    public static final ArmorMaterial PHARAOH_ARMOR_MATERIAL = new ArmorMaterial(
            40,
            makeDefense(5, 7, 9, 4, 12), 16, SoundEvents.ARMOR_EQUIP_DIAMOND,
            2f, 0.1f, ModTags.Items.PHARAOH_ARMOR_REPAIRABLE, PHARAOH_ARMOR_KEY);

    private static Map<ArmorType, Integer> makeDefense(int boots, int legs, int chest, int helm, int body) {
        return Maps.newEnumMap(
                Map.of(ArmorType.BOOTS, boots, ArmorType.LEGGINGS, legs,
                        ArmorType.CHESTPLATE, chest, ArmorType.HELMET, helm,
                        ArmorType.BODY, body
                )
        );
    }
}
