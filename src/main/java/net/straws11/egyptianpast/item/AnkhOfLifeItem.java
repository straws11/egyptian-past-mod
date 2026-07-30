package net.straws11.egyptianpast.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class AnkhOfLifeItem extends Item {

    public AnkhOfLifeItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        int duration = 1000;
        player.addEffect(new MobEffectInstance(MobEffects.STRENGTH, duration, 50));
        player.addEffect(new MobEffectInstance(MobEffects.LUCK, duration, 50));
        player.addEffect(new MobEffectInstance(MobEffects.GLOWING, duration));
        return InteractionResult.SUCCESS;
    }
}
