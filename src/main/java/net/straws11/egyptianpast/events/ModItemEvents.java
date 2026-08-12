package net.straws11.egyptianpast.events;

import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;
import net.straws11.egyptianpast.EgyptianPast;
import net.straws11.egyptianpast.item.PharaohCrown;

@EventBusSubscriber(modid = EgyptianPast.MOD_ID)
public class ModItemEvents {

    @SubscribeEvent
    // on querying the item attributes
    public static void onItemAttributeModifiers(ItemAttributeModifierEvent event) {
        ItemStack stack = event.getItemStack();

        if (!(stack.getItem() instanceof PharaohCrown crown)) return;
        if (!crown.isCursed(stack)) return;

        event.removeAllModifiersFor(Attributes.ARMOR);
        event.removeAllModifiersFor(Attributes.ARMOR_TOUGHNESS);
        event.removeAllModifiersFor(Attributes.KNOCKBACK_RESISTANCE);
    }
}
