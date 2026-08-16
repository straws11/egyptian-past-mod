package net.straws11.egyptianpast.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.straws11.egyptianpast.block.entity.PedestalBlockEntity;

public class PedestalBlock extends BaseEntityBlock {
    public static final VoxelShape SHAPE = Block.box(2, 0, 2, 14, 13, 14);
    public static final MapCodec<PedestalBlock> CODEC = simpleCodec(PedestalBlock::new);

    public PedestalBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
        return new PedestalBlockEntity(worldPosition, blockState);
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player,
                                       ItemStack toolStack, boolean willHarvest, FluidState fluid) {
       if (level.getBlockEntity(pos) instanceof PedestalBlockEntity pedestalBlockEntity) {
           pedestalBlockEntity.drops();
           level.updateNeighbourForOutputSignal(pos, this);
       }
        return super.onDestroyedByPlayer(state, level, pos, player, toolStack, willHarvest, fluid);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof PedestalBlockEntity pedestalBlockEntity) {
            boolean isPedestalEmpty = pedestalBlockEntity.getStoredStack().isEmpty();

            // insert into pedestal
            if (isPedestalEmpty && !itemStack.isEmpty()) {
                if (!level.isClientSide()) {
                    try (Transaction transaction = Transaction.openRoot()) {
                        ItemResource resource = ItemResource.of(itemStack);
                        int inserted = pedestalBlockEntity.getItemHandler().insert(resource, 1, transaction);
                        // successfully inserted into the blockentity
                        if (inserted > 0) {
                            transaction.commit();
                            if (!player.isCreative()) {
                                itemStack.shrink(1);
                            }
                            pedestalBlockEntity.checkSuccessfulPedestalConfiguration(level, player);
                        }
                    }
                }
                return InteractionResult.SUCCESS;
            }

            // extract from pedestal
            else if (!isPedestalEmpty) {
                if (!level.isClientSide()) {
                    ItemStack taken = pedestalBlockEntity.getStoredStack().copy();

                    try (Transaction transaction = Transaction.openRoot()) {
                        int extracted = pedestalBlockEntity.getItemHandler()
                                .extract(ItemResource.of(taken), 1, transaction);
                        if (extracted > 0) {
                            transaction.commit();
                            giveToPlayer(taken, player, hand);
                        }
                    }
                }
                return InteractionResult.SUCCESS;
            }
        }
        return super.useItemOn(itemStack, state, level, pos, player, hand, hitResult);
    }

    /**
     * Attempt to place item in player's hand, else in inventory, else drop on floor
     * @param stack ItemStack to receive
     * @param player player
     * @param hand the interaction hand
     */
    private void giveToPlayer(ItemStack stack, Player player, InteractionHand hand) {
        if (player.getItemInHand(hand).isEmpty()) {
            player.setItemInHand(hand, stack);
        } else if (!player.getInventory().add(stack)) {
            player.drop(stack, false);
        }
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
}
