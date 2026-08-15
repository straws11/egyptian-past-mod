package net.straws11.egyptianpast.renderer.entity;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.Identifier;
import net.straws11.egyptianpast.EgyptianPastClient;
import net.straws11.egyptianpast.entity.Mummy;
import net.straws11.egyptianpast.model.entity.MummyModel;
import net.straws11.egyptianpast.renderer.entity.state.MummyRenderState;

public class MummyRenderer extends HumanoidMobRenderer<Mummy, MummyRenderState, MummyModel<MummyRenderState>> {
    public MummyRenderer(EntityRendererProvider.Context context) {
        this(context, ModelLayers.ZOMBIE, ModelLayers.ZOMBIE_ARMOR);
    }

    public MummyRenderer(
            EntityRendererProvider.Context context,
            ModelLayerLocation body,
            ArmorModelSet<ModelLayerLocation> armorSet
    ) {
        super(
                context,
                new MummyModel<MummyRenderState>(context.bakeLayer(body)),
                0.5f
        );
        this.addLayer(new HumanoidArmorLayer<>(this,
                ArmorModelSet.bake(armorSet, context.getModelSet(), MummyModel::new),
                context.getEquipmentRenderer()));
    }

    @Override
    public MummyRenderState createRenderState() {
        return new MummyRenderState();
    }

    @Override
    public Identifier getTextureLocation(MummyRenderState state) {
        return EgyptianPastClient.getEntityTexture("mummy.png");
    }
}
