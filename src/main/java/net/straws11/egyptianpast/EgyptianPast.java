package net.straws11.egyptianpast;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.straws11.egyptianpast.block.ModBlocks;
import net.straws11.egyptianpast.block.entity.ModBlockEntities;
import net.straws11.egyptianpast.creativetab.ModCreativeTabRegistration;
import net.straws11.egyptianpast.data.ModDataComponentRegistration;
import net.straws11.egyptianpast.entity.ModEntities;
import net.straws11.egyptianpast.entity.Mummy;
import net.straws11.egyptianpast.entity.Pharaoh;
import net.straws11.egyptianpast.item.ModItems;
import net.straws11.egyptianpast.loot.ModLootModifiers;
import net.straws11.egyptianpast.villager.ModVillagers;
import net.straws11.egyptianpast.worldgen.ModOverworldRegion;
import net.straws11.egyptianpast.worldgen.ModSurfaceRuleManager;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import terrablender.api.Regions;
import terrablender.api.SurfaceRuleManager;

import java.util.concurrent.CompletableFuture;

import static net.straws11.egyptianpast.block.ModBlocks.POMEGRANATE_SEEDS;
import static net.straws11.egyptianpast.entity.ModEntities.MUMMY_ENTITY;
import static net.straws11.egyptianpast.entity.ModEntities.PHARAOH_ENTITY;
import static net.straws11.egyptianpast.item.ModItems.MUMMY_SPAWN_EGG;
import static net.straws11.egyptianpast.item.ModItems.PHARAOH_SPAWN_EGG;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(EgyptianPast.MOD_ID)
public class EgyptianPast {
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "egyptianpast";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public EgyptianPast(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register the Deferred Register to the mod event bus so blocks get registered
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);

        ModDataComponentRegistration.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        ModItems.register(modEventBus);
        // Register the Deferred Register to the mod event bus so tabs get registered
        ModCreativeTabRegistration.register(modEventBus);

        ModLootModifiers.register(modEventBus);

        ModEntities.register(modEventBus);

        ModVillagers.register(modEventBus);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (EgyptianPast) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);
        modEventBus.addListener(this::createDefaultAttributes);
        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    public void createDefaultAttributes(EntityAttributeCreationEvent event) {
        event.put(
                MUMMY_ENTITY.get(),
                Mummy.createAttributes().build()
        );
        event.put(
                PHARAOH_ENTITY.get(),
                Pharaoh.createAttributes().build()
        );
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            event.accept(MUMMY_SPAWN_EGG);
            event.accept(PHARAOH_SPAWN_EGG);
        }
        if (event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS) {
            event.accept(POMEGRANATE_SEEDS);
        }
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.LOG_DIRT_BLOCK.getAsBoolean()) {
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));
        }

        LOGGER.info("{}{}", Config.MAGIC_NUMBER_INTRODUCTION.get(), Config.MAGIC_NUMBER.getAsInt());

        Config.ITEM_STRINGS.get().forEach((item) -> LOGGER.info("ITEM >> {}", item));
        event.enqueueWork(() -> {
            Regions.register(new ModOverworldRegion(
                    Identifier.fromNamespaceAndPath(EgyptianPast.MOD_ID, "overworld_region"),
                    4
            ));
        });
        SurfaceRuleManager.addSurfaceRules(
            SurfaceRuleManager.RuleCategory.OVERWORLD,
            EgyptianPast.MOD_ID,
            ModSurfaceRuleManager::makeRules
        );
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }
}
