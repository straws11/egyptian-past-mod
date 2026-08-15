package net.straws11.egyptianpast.events;

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
