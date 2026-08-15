package net.straws11.egyptianpast.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import net.straws11.egyptianpast.datagen.ModEnchantments;
import net.straws11.egyptianpast.item.ModItems;

public class OrganHarvestLootModifier extends LootModifier {

    public static final MapCodec<OrganHarvestLootModifier> CODEC = RecordCodecBuilder
        .mapCodec(inst ->
        codecStart(inst).apply(inst, OrganHarvestLootModifier::new));

    public OrganHarvestLootModifier(LootItemCondition[] conditions, int priority) {
        super(conditions, priority);
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        Entity target = context.getOptionalParameter(LootContextParams.THIS_ENTITY);
        Entity attacker = context.getOptionalParameter(LootContextParams.ATTACKING_ENTITY);
        ItemInstance tool = context.getOptionalParameter(LootContextParams.TOOL);

        if (tool == null && attacker instanceof LivingEntity livingAttacker) {
            tool = livingAttacker.getMainHandItem();
        }
        if (target instanceof Zombie && tool != null) {
            Holder<Enchantment> harvestEnch = context.getLevel().registryAccess()
                .lookupOrThrow(Registries.ENCHANTMENT)
                .getOrThrow(ModEnchantments.HARVESTING);

            int level = tool.getEnchantmentLevel(harvestEnch);
            if (level > 0) {
                float chance = level * 0.1f;
                RandomSource random = context.getRandom();

                if (random.nextFloat() < chance) {
                    Item[] organs = {
                        ModItems.LIVER.get(),
                        ModItems.LUNGS.get(),
                        ModItems.STOMACH.get(),
                        ModItems.INTESTINES.get()
                    };

                    Item pickedOrgan = organs[random.nextInt(organs.length)];
                    generatedLoot.add(new ItemStack(pickedOrgan));
                }
            }
        }
        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
