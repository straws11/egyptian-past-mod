package net.straws11.egyptianpast.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ConversionParams;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.straws11.egyptianpast.entity.ModEntityRegistration;

public class MummyWrapItem extends Item {

    public MummyWrapItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack itemStack, Player player, LivingEntity target, InteractionHand type) {
        if (target instanceof Zombie entity && !entity.isBaby()) {
            entity.convertTo(ModEntityRegistration.MUMMY_ENTITY.get(),
                    ConversionParams.single(
                            (Mob) entity,
                            true,
                            true),
                    mob -> {}
            );        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        return super.use(level, player, hand);
    }
}
