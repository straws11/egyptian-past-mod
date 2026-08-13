package net.straws11.egyptianpast.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.straws11.egyptianpast.block.entity.CanopicJarBlockEntity;
import net.straws11.egyptianpast.data.ModDataComponentRegistration;
import net.straws11.egyptianpast.item.OrganType;
import org.jspecify.annotations.Nullable;

public class CanopicJarBlock extends BaseEntityBlock {

    // used to store the state on the block instead of getting from blockentity, is better
    public static final EnumProperty<OrganType> ORGAN = EnumProperty.create("organ", OrganType.class);

    public static final VoxelShape SHAPE = Block.box(2, 0, 2, 14, 13, 14);
    public static final MapCodec<PedestalBlock> CODEC = simpleCodec(PedestalBlock::new);

    public CanopicJarBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(getStateDefinition().any().setValue(ORGAN, OrganType.EMPTY));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(ORGAN);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity by, ItemStack itemStack) {
        super.setPlacedBy(level, pos, state, by, itemStack);
        if (!level.isClientSide()) {
            OrganType organ = itemStack.getOrDefault(ModDataComponentRegistration.ORGAN_TYPE, OrganType.EMPTY);

            level.setBlockAndUpdate(pos, state.setValue(ORGAN, organ));
        }
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide()) {
            // TODO: logic for dropping the item with the properties set?
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new CanopicJarBlockEntity(blockPos, blockState);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    /**
     * For creative mode middle-click, copy state
     */
    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData, Player player) {
        ItemStack stack = super.getCloneItemStack(level, pos, state, includeData, player);
        if (level.getBlockEntity(pos) instanceof CanopicJarBlockEntity jarBe) {
            stack.set(ModDataComponentRegistration.ORGAN_TYPE.get(), jarBe.getOrganType());
        }
        return stack;
    }
}
