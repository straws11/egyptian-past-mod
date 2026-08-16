package net.straws11.egyptianpast.datagen;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.advancements.predicates.LocationPredicate;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.advancements.triggers.InventoryChangeTrigger;
import net.minecraft.advancements.triggers.KilledTrigger;
import net.minecraft.advancements.triggers.PlayerTrigger;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.biome.Biome;
import net.straws11.egyptianpast.EgyptianPast;
import net.straws11.egyptianpast.entity.ModEntities;
import net.straws11.egyptianpast.item.ModItems;

import java.util.List;
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
            var entityTypes = provider.lookupOrThrow(Registries.ENTITY_TYPE);
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
                    ModBiomes.EGYPTIAN_DESERT.identifier().toString(),
                    PlayerTrigger.TriggerInstance.located(
                        LocationPredicate.Builder.inBiome(
                            biomeRegistry.getOrThrow(ModBiomes.EGYPTIAN_DESERT)
                        )
                    )
                ).save(consumer, Identifier.fromNamespaceAndPath(EgyptianPast.MOD_ID, "egyptianpast/root"));

            AdvancementHolder getKey = Advancement.Builder.advancement()
                .parent(root)
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

            AdvancementHolder killPharaoh = Advancement.Builder.advancement()
                .parent(getKey)
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
