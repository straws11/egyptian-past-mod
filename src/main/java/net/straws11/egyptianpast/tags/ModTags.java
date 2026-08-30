package net.straws11.egyptianpast.tags;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.trading.VillagerTrade;
import net.straws11.egyptianpast.EgyptianPast;

public class ModTags {
    public static class Blocks {

    }

    public static class Items {
        public static final TagKey<Item> PHARAOH_ARMOR_REPAIRABLE = createTag("pharaoh_armor_repairable");
        public static final TagKey<Item> ORGAN = createTag("organ");

        private static TagKey<Item> createTag(String name) {
            return ItemTags.create(Identifier.fromNamespaceAndPath(EgyptianPast.MOD_ID, name));
        }

    }

    public static class Trades {

        public static final TagKey<VillagerTrade> ANCIENT_EGYPTIAN_LEVEL_1 =
            createTag("ancient_egyptian/level_1");

        public static final TagKey<VillagerTrade> ANCIENT_EGYPTIAN_LEVEL_2 =
            createTag("ancient_egyptian/level_2");

        private static TagKey<VillagerTrade> createTag(String name) {
            return TagKey.create(Registries.VILLAGER_TRADE, Identifier.fromNamespaceAndPath(EgyptianPast.MOD_ID, name));
        }

    }
}
