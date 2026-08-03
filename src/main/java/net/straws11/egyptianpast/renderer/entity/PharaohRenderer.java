package net.straws11.egyptianpast.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.straws11.egyptianpast.EgyptianPastClient;
import net.straws11.egyptianpast.entity.Pharaoh;
import net.straws11.egyptianpast.model.entity.PharaohModel;
import net.straws11.egyptianpast.renderer.entity.state.PharaohRenderState;

public class PharaohRenderer extends HumanoidMobRenderer<Pharaoh, PharaohRenderState, PharaohModel<PharaohRenderState>> {
    public PharaohRenderer(EntityRendererProvider.Context context) {
        this(context, ModelLayers.ZOMBIE, ModelLayers.ZOMBIE_ARMOR);
    }

    public PharaohRenderer(
            EntityRendererProvider.Context context,
            ModelLayerLocation body,
            ArmorModelSet<ModelLayerLocation> armorSet
    ) {
        super(
                context,
                new PharaohModel<PharaohRenderState>(context.bakeLayer(body)),
                0.5f
        );
        this.addLayer(new HumanoidArmorLayer<>(this,
                ArmorModelSet.bake(armorSet, context.getModelSet(), PharaohModel::new),
                context.getEquipmentRenderer()));
    }

    @Override
    public Identifier getTextureLocation(PharaohRenderState state) {
        return EgyptianPastClient.getEntityTexture("pharoah.png");
    }

    @Override
    public void submit(PharaohRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.scale(1.2f, 1.2f, 1.2f);
        super.submit(state, poseStack, submitNodeCollector, camera);
    }

    @Override
    public PharaohRenderState createRenderState() {
        return new PharaohRenderState();
    }
}
