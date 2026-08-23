package net.straws11.egyptianpast.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.straws11.egyptianpast.data.ModDataComponentRegistration;

import java.util.function.Consumer;

public class AnkhOfLifeItem extends Item {

    public static final int MAX_TOTEMS = 16;

    public AnkhOfLifeItem(Properties properties) {
        super(properties.component(ModDataComponentRegistration.STORED_TOTEMS.get(), 1));
    }

    public static int getCharges(ItemStack stack) {
        return stack.getOrDefault(ModDataComponentRegistration.STORED_TOTEMS.get(), 0);
    }

    public static void setCharges(ItemStack stack, int charges) {
        stack.set(ModDataComponentRegistration.STORED_TOTEMS.get(), Math.max(charges, 0));
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return getCharges(stack) > 0;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return Math.round(13f * getCharges(stack) / (float) MAX_TOTEMS);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 0xFFD700;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        int charges = getCharges(itemStack);
        builder.accept(
            Component.translatable("tooltip.egyptianpast.stored_totems")
                .append(Component.literal( ": " + charges + "/" + MAX_TOTEMS))
                .withStyle(ChatFormatting.GOLD)
        );

    }
}
