package net.straws11.egyptianpast.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.straws11.egyptianpast.data.ModDataComponentRegistration;

import java.util.function.Consumer;

public interface ICursedItem {
    default boolean isCursed(ItemStack stack) {
        return stack.getOrDefault(ModDataComponentRegistration.IS_CURSED.get(), false);
    }

    default void cleanse(ItemStack stack) {
        stack.set(ModDataComponentRegistration.IS_CURSED.get(), false);
    }

    default void triggerAbility(ItemStack stack, Player player, Runnable ability) {
        if (isCursed(stack)) {
            player.sendOverlayMessage(Component.translatable("cursed_message.egyptianpast"));
            return;
        }
        ability.run();
    }

    default void appendCursedTooltip(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        if (isCursed(stack)) {
            builder.accept(Component.translatable("tooltip.egyptianpast.cursed")
                    .withStyle(ChatFormatting.DARK_RED)
                    .withStyle(ChatFormatting.ITALIC)
            );
        }
    }
}
