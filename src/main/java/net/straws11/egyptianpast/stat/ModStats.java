package net.straws11.egyptianpast.stat;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.straws11.egyptianpast.EgyptianPast;

import java.util.function.Supplier;

public class ModStats {
    public static final DeferredRegister<Identifier> CUSTOM_STATS =
        DeferredRegister.create(BuiltInRegistries.CUSTOM_STAT, EgyptianPast.MOD_ID);

    public static final Supplier<Identifier> ITEMS_CLEANSED = makeCustomStat("items_cleansed");

    private static Supplier<Identifier> makeCustomStat(String key) {
        Identifier statIdentifier = Identifier.fromNamespaceAndPath(EgyptianPast.MOD_ID, key);
        return CUSTOM_STATS.register(key, () -> statIdentifier);
    }

    public static void register(IEventBus eventBus) {
        CUSTOM_STATS.register(eventBus);
    }

}
