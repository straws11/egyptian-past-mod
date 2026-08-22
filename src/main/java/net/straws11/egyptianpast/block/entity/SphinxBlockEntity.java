package net.straws11.egyptianpast.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SphinxBlockEntity extends BlockEntity {

    public SphinxBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(ModBlockEntities.SPHINX_BE.get(), worldPosition, blockState);
    }
}
