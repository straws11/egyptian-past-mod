package net.straws11.egyptianpast.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
import net.straws11.egyptianpast.entity.ModEntities;

public class MummyWrapItem extends Item {

    public MummyWrapItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack itemStack, Player player, LivingEntity target, InteractionHand type) {
        if (target instanceof Zombie entity && !entity.isBaby()) {
            entity.convertTo(ModEntities.MUMMY_ENTITY.get(),
                    ConversionParams.single(
                        entity,
                            true,
                            true),
                    mob -> {}
            );        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (player.getHealth() == player.getMaxHealth()) return InteractionResult.SUCCESS;
        player.heal(1f);
        player.getItemInHand(hand).shrink(1);

        if (!level.isClientSide()) {
            level.playSound(null, player.getOnPos(), SoundEvents.SNOW_STEP,
                    SoundSource.PLAYERS, 2f, 0f);
        }
        return super.use(level, player, hand);
    }
}
