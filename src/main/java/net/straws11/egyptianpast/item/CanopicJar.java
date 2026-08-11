package net.straws11.egyptianpast.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.straws11.egyptianpast.data.ModDataComponentRegistration;

import java.util.function.Consumer;

public class CanopicJar extends Item {

    public CanopicJar(Properties properties) {
        // default item is an empty
        super(properties.component(ModDataComponentRegistration.ORGAN_TYPE.get(), OrganType.EMPTY));
    }

    public static void setOrgan(ItemStack stack, OrganType type) {
        stack.set(ModDataComponentRegistration.ORGAN_TYPE.get(), type);
    }

    public static OrganType getOrgan(ItemStack stack) {
        return stack.get(ModDataComponentRegistration.ORGAN_TYPE.get());
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display,
                                Consumer<Component> builder, TooltipFlag tooltipFlag) {
        OrganType organ = getOrgan(itemStack);
        if (organ == OrganType.EMPTY) {
            builder.accept(
                    Component.translatable("tooltip.egyptianpast.jar.empty")
                            .withStyle(ChatFormatting.GRAY)
            );
        } else {
            builder.accept(Component.translatable("tooltip.egyptianpast.jar.contains"));
            builder.accept(
                    Component.translatable("organ.egyptianpast." + organ.getSerializedName())
                            .withStyle(ChatFormatting.DARK_RED)
            );
        }
    }
}
