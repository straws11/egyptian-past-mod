package net.straws11.egyptianpast.tags;

import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.straws11.egyptianpast.EgyptianPast;

public class ModTags {
    public static class Blocks {

    }

    public static class Items {
        public static final TagKey<Item> PHARAOH_ARMOR_REPAIRABLE = createTag("pharaoh_armor_repairable");

        private static TagKey<Item> createTag(String name) {
            return ItemTags.create(Identifier.fromNamespaceAndPath(EgyptianPast.MOD_ID, name));
        }

    }

    public static class Trades {

    }
}
