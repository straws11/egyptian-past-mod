package net.straws11.egyptianpast.creativetab;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.straws11.egyptianpast.data.ModDataComponentRegistration;
import net.straws11.egyptianpast.item.OrganType;

import static net.straws11.egyptianpast.EgyptianPast.MOD_ID;
import static net.straws11.egyptianpast.block.ModBlockRegistration.*;
import static net.straws11.egyptianpast.item.ModItemRegistration.*;

public class ModCreativeTabRegistration {

    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "egyptianpast" namespace
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);

    // Creates a creative tab with the id "egyptianpast:example_tab" for the example item, that is placed after the combat tab
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.egyptianpast")) //The language key for the title of your CreativeModeTab
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> PAPYRUS_REED.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(POMEGRANATE.get());// Add the example item to the tab. For your own tabs, this method is preferred over the event
                output.accept(PAPYRUS_REED_BLOCK.get());
                output.accept(PAPYRUS_SHEET.get());
                output.accept(LIMESTONE.get());
                output.accept(EGYPTIAN_STONE.get());
                output.accept(EGYPTIAN_COBBLESTONE.get());
                output.accept(ANKH_OF_LIFE.get());
                output.accept(MUMMY_WRAP.get());
                output.accept(KEY_FRAGMENT.get());
                output.accept(CRYPT_KEY.get());
                output.accept(PHARAOH_CROWN.get());

                // canopic jar variants
                for (OrganType organType : OrganType.values()) {
                    ItemStack jarStack = CANOPIC_JAR.get().getDefaultInstance();
                    jarStack.set(ModDataComponentRegistration.ORGAN_TYPE.get(), organType);
                    output.accept(jarStack);
                }

                output.accept(SARCOPHAGUS.get());
                output.accept(PEDESTAL_BLOCK.get());
            }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }

}
