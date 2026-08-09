package net.straws11.egyptianpast;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.straws11.egyptianpast.datagen.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = EgyptianPast.MOD_ID)
public class EgyptianPastDataGen {

    // i think this might not be necessary, at least not now. doesn't seem to do much
    /*@SubscribeEvent
    public static void gatherServerData(GatherDataEvent.Server event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ModDatapackProvider datapackProvider = new ModDatapackProvider(packOutput, event.getLookupProvider());
        generator.addProvider(true, datapackProvider);
        CompletableFuture<HolderLookup.Provider> lookupProvider = datapackProvider.getRegistryProvider();

        generator.addProvider(true, new ModBlockTagsProvider(packOutput, lookupProvider));
        generator.addProvider(true, new ModItemTagsProvider(packOutput, lookupProvider));
        generator.addProvider(true, new ModBiomeTagsProvider(packOutput, lookupProvider));
        generator.addProvider(true, new ModRecipeProvider.Runner(packOutput, lookupProvider));
    }*/

    @SubscribeEvent
    public static void gatherClientData(GatherDataEvent.Client event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ModDatapackProvider datapackProvider = new ModDatapackProvider(packOutput, event.getLookupProvider());
        generator.addProvider(true, datapackProvider);
        CompletableFuture<HolderLookup.Provider> lookupProvider = datapackProvider.getRegistryProvider();
        //var lookupProvider = event.getLookupProvider();

        generator.addProvider(true, new ModModelProvider(packOutput));
        generator.addProvider(true,
                new LootTableProvider(packOutput, Collections.emptySet(),
                    List.of(
                            new LootTableProvider.SubProviderEntry(ModBlockLootTableProvider::new, LootContextParamSets.BLOCK),
                            new LootTableProvider.SubProviderEntry( ModEntityLootTableProvider::new, LootContextParamSets.ENTITY)
                    ),
                lookupProvider));
        generator.addProvider(true, new ModEquipmentAssetProvider(packOutput));
        generator.addProvider(true, new ModBlockTagsProvider(packOutput, lookupProvider));
        generator.addProvider(true, new ModItemTagsProvider(packOutput, lookupProvider));
        generator.addProvider(true, new ModBiomeTagsProvider(packOutput, lookupProvider));
        generator.addProvider(true, new ModRecipeProvider.Runner(packOutput, lookupProvider));
    }
}
