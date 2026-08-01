package net.straws11.egyptianpast.events;

import net.minecraft.world.entity.ConversionParams;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.straws11.egyptianpast.entity.ModEntityRegistration;
import net.straws11.egyptianpast.item.MummyWrapItem;

public class EntityInteractionEvent {

    /*@SubscribeEvent
    public void clickOnEntity(PlayerInteractEvent.EntityInteractSpecific event) {
        event.getEntity()
        .sendSystemMessage(net.minecraft.network.chat.Component.literal("Hello, World!"));

        if (event.getTarget() instanceof LivingEntity entity) {
            if (entity instanceof Zombie && !entity.isBaby()) {
                Level world = entity.level();
                ItemStack eventItem = event.getItemStack();
                if (!eventItem.isEmpty() && eventItem.getItem() instanceof MummyWrapItem
                        && !world.isClientSide()) {
                    ((Mob) entity).convertTo(ModEntityRegistration.MUMMY_ENTITY.get(),
                            ConversionParams.single(
                                    (Mob) entity,
                                    false,
                                    false),
                            mob -> {}
                    );

                }
            }
        }
    }*/
}
