package net.straws11.egyptianpast.events;

import net.minecraft.advancements.triggers.CriteriaTriggers;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.straws11.egyptianpast.EgyptianPast;
import net.straws11.egyptianpast.data.ModDataComponentRegistration;
import net.straws11.egyptianpast.item.AnkhOfLifeItem;
import net.straws11.egyptianpast.item.ModItems;
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

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        if (event.getSource().is(DamageTypes.FELL_OUT_OF_WORLD) || event.getSource().is(DamageTypes.GENERIC_KILL)) {
            return;
        }

        Inventory inv = player.getInventory();
        ItemStack foundAnkh = ItemStack.EMPTY;

        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.is(ModItems.ANKH_OF_LIFE.get())) {
                foundAnkh = stack;
                break;
            }
        }

        if (foundAnkh.isEmpty()) {
            return;
        }

        if (foundAnkh.getOrDefault(ModDataComponentRegistration.STORED_TOTEMS.get(), 0) <= 0) return;
        AnkhOfLifeItem.setCharges(foundAnkh, AnkhOfLifeItem.getCharges(foundAnkh) - 1);

        event.setCanceled(true);

        player.setHealth(1.0f);
        player.removeAllEffects();

        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, 1));
        player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));
        player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 800, 0));

        if (player.level() instanceof ServerLevel serverLevel) {
            serverLevel.playSound(null, player.getOnPos(), SoundEvents.TOTEM_USE,
                SoundSource.PLAYERS, 1f, 1f);

            serverLevel.sendParticles(ParticleTypes.TOTEM_OF_UNDYING,
                player.getX(), player.getY() + 1, player.getZ(),
                60, 0.5, 0.5, 0.5, 0.15
            );

            CriteriaTriggers.USED_TOTEM.trigger(player, foundAnkh);
            player.awardStat(Stats.ITEM_USED.get(ModItems.ANKH_OF_LIFE.get()));
        }


    }

}
