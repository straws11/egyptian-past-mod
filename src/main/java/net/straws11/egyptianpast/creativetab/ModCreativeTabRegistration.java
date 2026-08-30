package net.straws11.egyptianpast.creativetab;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.straws11.egyptianpast.block.ModBlocks;
import net.straws11.egyptianpast.block.Sarcophagus;
import net.straws11.egyptianpast.data.ModDataComponentRegistration;
import net.straws11.egyptianpast.item.OrganType;
import net.straws11.egyptianpast.item.ScrollType;

import static net.straws11.egyptianpast.EgyptianPast.MOD_ID;
import static net.straws11.egyptianpast.block.ModBlocks.*;
import static net.straws11.egyptianpast.item.ModItems.*;

public class ModCreativeTabRegistration {

    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "egyptianpast" namespace
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);

    // Creates a creative tab with the id "egyptianpast:example_tab" for the example item, that is placed after the combat tab
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.egyptianpast")) //The language key for the title of your CreativeModeTab
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> CRYPT_KEY.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(POMEGRANATE.get());// Add the example item to the tab. For your own tabs, this method is preferred over the event
                output.accept(PAPYRUS_REED_BLOCK.get());
                output.accept(PAPYRUS_SHEET.get());
                output.accept(ANKH_OF_LIFE.get());
                output.accept(MUMMY_WRAP.get());
                output.accept(KEY_FRAGMENT.get());
                output.accept(CRYPT_KEY.get());
                output.accept(PHARAOH_CROWN.get());
                output.accept(EGYPTIAN_SCROLL.get());
                output.accept(LIVER.get());
                output.accept(LUNGS.get());
                output.accept(STOMACH.get());
                output.accept(INTESTINES.get());

                // canopic jar variants
                for (OrganType organType : OrganType.values()) {
                    ItemStack jarStack = CANOPIC_JAR.get().getDefaultInstance();
                    jarStack.set(ModDataComponentRegistration.ORGAN_TYPE.get(), organType);
                    output.accept(jarStack);
                }

                // scroll variants
                for (ScrollType scrollType : ScrollType.values()) {
                    ItemStack scrollStack = EGYPTIAN_SCROLL.get().getDefaultInstance();
                    scrollStack.set(ModDataComponentRegistration.SCROLL_TYPE.get(), scrollType);
                    output.accept(scrollStack);
                }

                output.accept(SARCOPHAGUS.get());
                // sealed variant of sarcophagus
                ItemStack sealed = new ItemStack(SARCOPHAGUS.get());
                sealed.set(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY.with(Sarcophagus.OPENED, false));
                sealed.set(DataComponents.CUSTOM_NAME, Component.translatable("block.egyptianpast.sarcophagus.sealed"));
                output.accept(sealed);

                output.accept(PEDESTAL_BLOCK.get());
                output.accept(SPHINX_BLOCK.get());
                output.accept(EGYPTIAN_POT_1.get());
                output.accept(EGYPTIAN_POT_2.get());
                output.accept(EGYPTIAN_POT_3.get());
                output.accept(ANUBIS_BLOCK.get());
            }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }

}
