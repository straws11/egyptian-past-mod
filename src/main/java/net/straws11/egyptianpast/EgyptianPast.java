package net.straws11.egyptianpast;

import net.straws11.egyptianpast.block.EgyptianPastBlockRegistration;
import net.straws11.egyptianpast.creativetab.EgyptianPastCreativeTabRegistration;
import net.straws11.egyptianpast.entity.EgyptianPastEntityRegistration;
import net.straws11.egyptianpast.item.EgyptianPastItemRegistration;
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

import static net.straws11.egyptianpast.block.EgyptianPastBlockRegistration.EXAMPLE_BLOCK_ITEM;
import static net.straws11.egyptianpast.item.EgyptianPastItemRegistration.MUMMY_SPAWN_EGG;

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
        EgyptianPastBlockRegistration.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        EgyptianPastItemRegistration.register(modEventBus);
        // Register the Deferred Register to the mod event bus so tabs get registered
        EgyptianPastCreativeTabRegistration.register(modEventBus);

        EgyptianPastEntityRegistration.register(modEventBus);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (EgyptianPast) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(EXAMPLE_BLOCK_ITEM);
        }
        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            event.accept(MUMMY_SPAWN_EGG);
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
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }
}
