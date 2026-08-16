package net.straws11.egyptianpast.villager;

import com.google.common.collect.ImmutableSet;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.straws11.egyptianpast.EgyptianPast;
import net.straws11.egyptianpast.block.ModBlocks;
import net.straws11.egyptianpast.datagen.villager.ModTradeSets;

public class ModVillagers {

    public static final DeferredRegister<PoiType> POI_TYPES =
        DeferredRegister.create(BuiltInRegistries.POINT_OF_INTEREST_TYPE, EgyptianPast.MOD_ID);

    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS =
        DeferredRegister.create(BuiltInRegistries.VILLAGER_PROFESSION, EgyptianPast.MOD_ID);

    public static final Holder<PoiType> EGYPTIAN_VILLAGER_POI = POI_TYPES.register("ancient_egyptian_poi",
        () -> new PoiType(
            ImmutableSet.copyOf(ModBlocks.PEDESTAL_BLOCK.get().getStateDefinition().getPossibleStates()),
            1, 1)
        );

    public static final Holder<VillagerProfession> ANCIENT_EGYPTIAN =
        VILLAGER_PROFESSIONS.register("ancient_egyptian",
            () -> new VillagerProfession(Component.translatable("entity.minecraft.villager.egyptianpast.ancient_egyptian"),
                holder -> holder.value() == EGYPTIAN_VILLAGER_POI.value(),
                holder -> holder.value() == EGYPTIAN_VILLAGER_POI.value(), ImmutableSet.of(), ImmutableSet.of(),
                SoundEvents.AMETHYST_BLOCK_FALL,
                Int2ObjectMap.ofEntries(
                    Int2ObjectMap.entry(1, ModTradeSets.ANCIENT_EGYPTIAN_LEVEL_1),
                    Int2ObjectMap.entry(2, ModTradeSets.ANCIENT_EGYPTIAN_LEVEL_2)
                )
            )
        );

    public static void register(IEventBus eventBus) {
        POI_TYPES.register(eventBus);
        VILLAGER_PROFESSIONS.register(eventBus);
    }
}
