package net.straws11.egyptianpast.loot;

import com.mojang.serialization.MapCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.straws11.egyptianpast.EgyptianPast;

import java.util.function.Supplier;

public class ModLootModifiers {
    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> GLOBAL_LOOT_MODIFIERS =
        DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, EgyptianPast.MOD_ID);

    public static final Supplier<MapCodec<OrganHarvestLootModifier>> ORGAN_HARVESTING_LOOT_MODIFIER =
        GLOBAL_LOOT_MODIFIERS.register("organ_harvesting_loot_modifier", () -> OrganHarvestLootModifier.CODEC);

    public static void register(IEventBus modEventBus) {
        GLOBAL_LOOT_MODIFIERS.register(modEventBus);
    }
}
