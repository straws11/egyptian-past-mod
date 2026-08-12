package net.straws11.egyptianpast.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.TooltipDisplay;
import net.straws11.egyptianpast.data.ModDataComponentRegistration;
import org.jspecify.annotations.NonNull;

import java.util.function.Consumer;

public class PharaohCrown extends Item implements ICursedItem {
    public PharaohCrown(Properties properties) {
        super(properties.component(ModDataComponentRegistration.IS_CURSED.get(), true));
    }

    @Override
    public @NonNull ItemAttributeModifiers getDefaultAttributeModifiers(ItemStack stack) {
        // prevent cursed items from having armor ratings
        if (isCursed(stack)) {
            return ItemAttributeModifiers.EMPTY;
        }
        return super.getDefaultAttributeModifiers(stack);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        appendCursedTooltip(itemStack, context, display, builder, tooltipFlag);
        super.appendHoverText(itemStack, context, display, builder, tooltipFlag);
    }
}
