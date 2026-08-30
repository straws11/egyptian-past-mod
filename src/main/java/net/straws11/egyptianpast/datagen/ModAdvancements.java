package net.straws11.egyptianpast.datagen;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.predicates.*;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.advancements.predicates.entity.EntitySubPredicate;
import net.minecraft.advancements.predicates.entity.EntitySubPredicates;
import net.minecraft.advancements.predicates.entity.EntityTypePredicate;
import net.minecraft.advancements.triggers.*;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.predicates.VillagerTypePredicate;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.straws11.egyptianpast.EgyptianPast;
import net.straws11.egyptianpast.block.CanopicJarBlock;
import net.straws11.egyptianpast.block.ModBlocks;
import net.straws11.egyptianpast.data.ModDataComponentRegistration;
import net.straws11.egyptianpast.entity.ModEntities;
import net.straws11.egyptianpast.item.ModItems;
import net.straws11.egyptianpast.item.OrganType;
import net.straws11.egyptianpast.structure.ModStructures;
import net.straws11.egyptianpast.tags.ModTags;
import net.straws11.egyptianpast.villager.ModVillagers;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ModAdvancements extends AdvancementProvider {
    public ModAdvancements(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, List.of(new EgyptianPastAdvancements()));
    }

    public static class EgyptianPastAdvancements implements AdvancementSubProvider {

        @Override
        public void generate(HolderLookup.Provider provider, Consumer<AdvancementHolder> consumer) {
            var items = provider.lookupOrThrow(Registries.ITEM);
            var blocks = provider.lookupOrThrow(Registries.BLOCK);
            var entityTypes = provider.lookupOrThrow(Registries.ENTITY_TYPE);
            var professions = provider.lookupOrThrow(Registries.VILLAGER_PROFESSION);
            HolderGetter<Biome> biomeRegistry = provider.lookupOrThrow(Registries.BIOME);

            AdvancementHolder root = Advancement.Builder.advancement()
                .display(
                    ModItems.CRYPT_KEY,
                    Component.translatable("advancements.egyptianpast.root.title"),
                    Component.translatable("advancements.egyptianpast.root.description"),
                    Identifier.withDefaultNamespace("gui/advancements/backgrounds/adventure"),
                    AdvancementType.TASK,
                    false,
                    false,
                    false
                ).addCriterion(
                    "auto_unlock", PlayerTrigger.TriggerInstance.tick()
                ).save(consumer, Identifier.fromNamespaceAndPath(EgyptianPast.MOD_ID, "egyptianpast/root"));

            AdvancementHolder getOrgan = Advancement.Builder.advancement()
                .parent(root)
                .display(
                    ModItems.LUNGS,
                    Component.translatable("advancements.egyptianpast.get_organ.title"),
                    Component.translatable("advancements.egyptianpast.get_organ.description"),
                    null,
                    AdvancementType.TASK,
                    true,
                    true,
                    false
                ).addCriterion(
                    "organ",
                    InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(
                        items, ModTags.Items.ORGAN
                    ))
                ).save(consumer, Identifier.fromNamespaceAndPath(EgyptianPast.MOD_ID, "egyptianpast/obtain_organ"));

            AdvancementHolder tradeWithEgyptian = Advancement.Builder.advancement()
                .parent(root)
                .display(
                    Items.EMERALD,
                    Component.translatable("advancements.egyptianpast.trade_egyptian.title"),
                    Component.translatable("advancements.egyptianpast.trade_egyptian.description"),
                    null,
                    AdvancementType.TASK,
                    true,
                    true,
                    false
                ).addCriterion(
                    "trade_egyptian",
                    InventoryChangeTrigger.TriggerInstance.hasItems(
                        ItemPredicate.Builder.item().of(
                            items, ModBlocks.PEDESTAL_BLOCK.asItem()
                        )
                    )
                ).save(consumer, Identifier.fromNamespaceAndPath(EgyptianPast.MOD_ID, "egyptianpast/trade_egyptian"));

            AdvancementHolder getFragment = Advancement.Builder.advancement()
                .parent(tradeWithEgyptian)
                .display(
                    ModItems.KEY_FRAGMENT,
                    Component.translatable("advancements.egyptianpast.get_key_fragment.title"),
                    Component.translatable("advancements.egyptianpast.get_key_fragment.description"),
                    null,
                    AdvancementType.TASK,
                    true,
                    true,
                    false
                ).addCriterion(
                    "key_fragment",
                    InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(
                        items, ModItems.KEY_FRAGMENT.asItem()
                    ))
                ).save(consumer, Identifier.fromNamespaceAndPath(EgyptianPast.MOD_ID, "egyptianpast/get_key_fragment"));

            AdvancementHolder getKey = Advancement.Builder.advancement()
                .parent(getFragment)
                .display(
                    ModItems.CRYPT_KEY,
                    Component.translatable("advancements.egyptianpast.get_crypt_key.title"),
                    Component.translatable("advancements.egyptianpast.get_crypt_key.description"),
                    null,
                    AdvancementType.TASK,
                    true,
                    true,
                    false
                ).addCriterion(
                    "crypt_key",
                    InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(
                        items, ModItems.CRYPT_KEY.asItem()
                    ))
                ).save(consumer, Identifier.fromNamespaceAndPath(EgyptianPast.MOD_ID, "egyptianpast/get_crypt_key"));

            AdvancementHolder findBiome = Advancement.Builder.advancement()
                .parent(getKey)
                .display(Blocks.SANDSTONE,
                        Component.translatable("advancement.egyptianpast.find_egyptian_desert.title"),
                        Component.translatable("advancement.egyptianpast.find_egyptian_desert.description"),
                        null,
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                ) .addCriterion(
                    ModBiomes.EGYPTIAN_DESERT.identifier().toString(),
                    PlayerTrigger.TriggerInstance.located(
                        LocationPredicate.Builder.inBiome(
                            biomeRegistry.getOrThrow(ModBiomes.EGYPTIAN_DESERT)
                        )
                    )
                ).save(consumer, Identifier.fromNamespaceAndPath(EgyptianPast.MOD_ID, "egyptianpast/find_egyptian_desert"));

            AdvancementHolder findAncientPyramid = Advancement.Builder.advancement()
                .parent(findBiome)
                .display(
                    ModBlocks.SARCOPHAGUS,
                    Component.translatable("advancements.egyptianpast.find_ancient_pyramid.title"),
                    Component.translatable("advancements.egyptianpast.find_ancient_pyramid.description"),
                    null,
                    AdvancementType.TASK,
                    true,
                    true,
                    false
                ).addCriterion(
                    "ancient_pyramid",
                    PlayerTrigger.TriggerInstance.located(
                        LocationPredicate.Builder.inStructure(
                            provider.lookupOrThrow(Registries.STRUCTURE).getOrThrow(ModStructures.ANCIENT_PYRAMID)
                        )
                    )
                ).save(consumer, Identifier.fromNamespaceAndPath(EgyptianPast.MOD_ID, "egyptianpast/find_ancient_pyramid"));

            AdvancementHolder killPharaoh = Advancement.Builder.advancement()
                .parent(findAncientPyramid)
                .display(
                    ModItems.PHARAOH_CROWN,
                    Component.translatable("advancements.egyptianpast.kill_pharaoh.title"),
                    Component.translatable("advancements.egyptianpast.kill_pharaoh.description"),
                    null,
                    AdvancementType.GOAL,
                    true,
                    true,
                    false
                ).addCriterion(
                    "kill_pharaoh",
                    KilledTrigger.TriggerInstance.playerKilledEntity(
                        EntityPredicate.Builder.entity().of(
                            entityTypes, ModEntities.PHARAOH_ENTITY.get()
                        ))
                ).save(consumer, Identifier.fromNamespaceAndPath(EgyptianPast.MOD_ID, "egyptianpast/kill_pharaoh"));
        }
    }
}
