package net.straws11.egyptianpast.model.entity;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.straws11.egyptianpast.renderer.entity.state.PharaohRenderState;

public class PharaohModel<S extends PharaohRenderState> extends HumanoidModel<S> {
    public PharaohModel(ModelPart root) {
        super(root);
    }
}
