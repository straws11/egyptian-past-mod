package net.straws11.egyptianpast.renderer.entity.state;

import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;

public class PharaohRenderState extends HumanoidRenderState {
    public boolean isAggressive;

    @Override
    public ItemStack getUseItemStackForArm(HumanoidArm arm) {
        return this.getMainHandItemStack();
    }
}
