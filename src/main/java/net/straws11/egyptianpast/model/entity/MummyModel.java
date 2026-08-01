package net.straws11.egyptianpast.model.entity;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwingAnimationType;
import net.straws11.egyptianpast.renderer.entity.state.MummyRenderState;

public class MummyModel<S extends MummyRenderState> extends HumanoidModel<S> /*extends HumanoidModel<MummyRenderState>*/ {

    public static final ModelLayerLocation MY_LAYER = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath("egyptianpast", "mummy"),
            "main"
    );

    public MummyModel(ModelPart root) {
        super(root);
    }

    public void setupAnim(S state) {
        super.setupAnim(state);
        animateZombieArms(this.leftArm, this.rightArm, state.isAggressive, state);
    }

    private static <T extends HumanoidRenderState> void animateZombieArms(ModelPart leftArm, ModelPart rightArm, boolean aggressive, T state) {
        boolean animateAttack = state.swingAnimationType != SwingAnimationType.STAB;
        if (animateAttack) {
            boolean raiseArms = !state.isBaby || state.getMainHandItemStack() == ItemStack.EMPTY;
            float armDrop = raiseArms ? (float) -Math.PI / (aggressive ? 1.5F : 2.25F) : 0.0F;
            animateAttackArms(leftArm, rightArm, state.attackTime, false, armDrop);
        }

        bobArms(rightArm, leftArm, state.ageInTicks);
    }

    private static void animateAttackArms(ModelPart leftArm, ModelPart rightArm, float attackTime, boolean negateArmRotation, float armDrop) {
        float attackYRotModifier = (negateArmRotation ? 1.0F : -1.0F) * Mth.sin(attackTime * (float) Math.PI);
        float attackXRotModifier = Mth.sin((1.0F - (1.0F - attackTime) * (1.0F - attackTime)) * (float) Math.PI);
        float xRot = armDrop + attackYRotModifier * 1.2F - attackXRotModifier * 0.4F;
        float yRot = 0.1F - attackYRotModifier * 0.6F;
        rightArm.xRot = xRot;
        rightArm.yRot = negateArmRotation ? -yRot : yRot;
        rightArm.zRot = 0.0F;
        leftArm.xRot = xRot;
        leftArm.yRot = negateArmRotation ? yRot : -yRot;
        leftArm.zRot = 0.0F;
    }

    public static void bobModelPart(ModelPart modelPart, float ageInTicks, float scale) {
        modelPart.zRot = modelPart.zRot + (0.3f * scale) * (Mth.cos(ageInTicks * 0.22F) * 0.08F + 0.05F);
        modelPart.xRot = modelPart.xRot + scale * (Mth.sin(ageInTicks * 0.15F) * 0.08F);
    }

    public static void bobArms(ModelPart rightArm, ModelPart leftArm, float ageInTicks) {
        bobModelPart(rightArm, ageInTicks, 3.0F);
        bobModelPart(leftArm, ageInTicks, -3.0F);
    }
}
