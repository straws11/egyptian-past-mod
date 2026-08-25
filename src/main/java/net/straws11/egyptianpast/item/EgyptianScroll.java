package net.straws11.egyptianpast.item;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.straws11.egyptianpast.data.ModDataComponentRegistration;

import java.util.List;

public class EgyptianScroll extends Item {

    public EgyptianScroll(Properties properties) {
        super(properties.component(ModDataComponentRegistration.SCROLL_TYPE.get(), ScrollType.BLANK));
    }

    public static ScrollType getScroll(ItemStack stack) {
        return stack.getOrDefault(ModDataComponentRegistration.SCROLL_TYPE.get(), ScrollType.BLANK);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        return switch (stack.getOrDefault(ModDataComponentRegistration.SCROLL_TYPE.get(), ScrollType.BLANK)) {
            case BLANK -> super.use(level, player, hand);
            case SUN_STRIKE -> triggerSunStrike(level, player, stack);
        };
    }

    private InteractionResult triggerSunStrike(Level level, Player player, ItemStack stack) {
        int range = 7; // each direction
        if (level.isClientSide()) return InteractionResult.SUCCESS;
        if (!level.canSeeSky(player.getOnPos().above())) return InteractionResult.FAIL;

        List<Monster> monsters = level.getEntitiesOfClass(Monster.class, player.getBoundingBox().inflate(range),
            entity -> entity.isAlive() && !entity.isSpectator());

        for (Monster monster : monsters) {
            LightningBolt bolt = EntityTypes.LIGHTNING_BOLT.create(level, EntitySpawnReason.TRIGGERED);
            if (bolt != null) {
                bolt.setPos(monster.position());
                bolt.setVisualOnly(true);
                if (player instanceof ServerPlayer serverPlayer) {
                    bolt.setCause(serverPlayer);
                }
                level.addFreshEntity(bolt);
                monster.hurtServer((ServerLevel) level, level.damageSources().playerAttack(player), 10f);
                monster.addEffect(new MobEffectInstance(MobEffects.WITHER, 100, 2));
            }
        }
        stack.consume(1, player);
        player.sendOverlayMessage(Component.literal(player.getDisplayName().getString()).append(Component.translatable(
            "message.egyptianpast.used_" + getScrollName(stack))
        ));
        player.getCooldowns().addCooldown(stack, 60);
        return InteractionResult.SUCCESS;
    }

    private String getScrollName(ItemStack stack) {
        ScrollType scrollType = stack.getOrDefault(ModDataComponentRegistration.SCROLL_TYPE.get(), ScrollType.BLANK);
        return scrollType.getSerializedName();
    }

    @Override
    public Component getName(ItemStack itemStack) {
        return Component.translatable("item.egyptianpast.scroll." + getScrollName(itemStack));
    }
}
