package net.straws11.egyptianpast.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;
import net.straws11.egyptianpast.data.ModDataComponentRegistration;

import java.util.Properties;
import java.util.function.Consumer;

public class CanopicJarBlockItem extends BlockItem {

    public CanopicJarBlockItem(Block block, Properties properties) {
        super(block, properties.component(ModDataComponentRegistration.ORGAN_TYPE.get(), OrganType.EMPTY));
    }

    public static void setOrgan(ItemStack stack, OrganType type) {
        stack.set(ModDataComponentRegistration.ORGAN_TYPE.get(), type);
    }

    public static OrganType getOrgan(ItemStack stack) {
        return stack.getOrDefault(ModDataComponentRegistration.ORGAN_TYPE.get(), OrganType.EMPTY);
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
            MutableComponent component = Component.translatable("tooltip.egyptianpast.jar.contains");
            component.append(Component.translatable("organ.egyptianpast." + organ.getSerializedName())
                            .withStyle(ChatFormatting.DARK_RED));
            builder.accept(component);
        }
    }

    @Override
    public Component getName(ItemStack itemStack) {
        OrganType organType = itemStack.getOrDefault(ModDataComponentRegistration.ORGAN_TYPE.get(), OrganType.EMPTY);
        return Component.translatable("block.egyptianpast.canopic_jar." + organType.getSerializedName());
    }
}
