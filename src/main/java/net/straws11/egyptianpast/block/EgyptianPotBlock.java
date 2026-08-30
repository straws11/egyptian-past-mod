package net.straws11.egyptianpast.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class EgyptianPotBlock extends Block {
    public EgyptianPotBlock(Properties properties) {
        super(properties.sound(SoundType.DECORATED_POT));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Block.box(3.0, 0.0, 3.0, 13.0, 14.0, 13.0);
    }
}
