package net.straws11.egyptianpast.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.straws11.egyptianpast.data.ModDataComponentRegistration;
import net.straws11.egyptianpast.item.OrganType;

public class CanopicJarBlockEntity extends BlockEntity {
    private OrganType organType = OrganType.EMPTY;

    public CanopicJarBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(ModBlockEntityRegistration.CANOPIC_JAR_BE.get(), worldPosition, blockState);
    }

    public OrganType getOrganType() {
        return organType;
    }

    public void setOrganType(OrganType organType) {
        this.organType = organType;
        setChanged();
    }

    /**
     * When CanopicJarBlockItem is placed, the CanopicJarBlock is placed, CanopicJarBlockEntity is spawned,
     * and this is called
     * @param components accessor for the data components on the blockitem
     */
    @Override
    protected void applyImplicitComponents(DataComponentGetter components) {
        super.applyImplicitComponents(components);
        this.organType = components.getOrDefault(ModDataComponentRegistration.ORGAN_TYPE, OrganType.EMPTY);
    }

    /**
     * When the block is broken, this gets run on the blockentity to set the datacomponents for the blockitem
     * @param components builder to set datacomponents that will be applied to the dropped itemstack
     */
    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components) {
        super.collectImplicitComponents(components);
        components.set(ModDataComponentRegistration.ORGAN_TYPE, this.organType);
    }

    // TODO: don't know what to do
    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putString("organType", organType.name());
    }

    // TODO: don't know what to do
    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        organType = OrganType.valueOf(input.getStringOr("organType", "EMPTY"));
    }
}
