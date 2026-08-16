package net.straws11.egyptianpast.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.straws11.egyptianpast.EgyptianPast;
import net.straws11.egyptianpast.datagen.villager.ModTradeSets;
import net.straws11.egyptianpast.datagen.villager.ModVillagerTrades;
import net.straws11.egyptianpast.structure.ModStructureSets;
import net.straws11.egyptianpast.structure.ModStructures;
import net.straws11.egyptianpast.worldgen.ModBiomeModifiers;
import net.straws11.egyptianpast.worldgen.ModConfiguredFeatures;
import net.straws11.egyptianpast.worldgen.ModPlacedFeatures;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModDatapackProvider extends DatapackBuiltinEntriesProvider {

    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
        .add(Registries.ENCHANTMENT, ModEnchantments::bootstrap)
        .add(Registries.TEMPLATE_POOL, ModStructures::bootstrapPools)
        .add(Registries.STRUCTURE, ModStructures::bootstrapStructures)
        .add(Registries.STRUCTURE_SET, ModStructureSets::bootstrap)
        .add(Registries.VILLAGER_TRADE, ModVillagerTrades::bootstrap)
        .add(Registries.TRADE_SET, ModTradeSets::bootstrap)
        .add(Registries.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap)
        .add(Registries.PLACED_FEATURE, ModPlacedFeatures::bootstrap)
        // is this line correct?
        .add(Registries.BIOME, ModBiomeProvider::bootstrap)
        .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ModBiomeModifiers::bootstrap);

    public ModDatapackProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(EgyptianPast.MOD_ID));
    }
}
