package net.straws11.egyptianpast.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.straws11.egyptianpast.item.ModItems;
import org.jspecify.annotations.Nullable;

import java.util.Set;

public class Sarcophagus extends HorizontalDirectionalBlock {

    public static final MapCodec<Sarcophagus> CODEC = simpleCodec(Sarcophagus::new);

    public static final EnumProperty<BedPart> PART = BlockStateProperties.BED_PART;
    public static final BooleanProperty OPENED = BooleanProperty.create("opened");

    public Sarcophagus(Properties properties) {
        super(properties);
        // this is default to false anyway but here's an example
        this.registerDefaultState(stateDefinition.any()
                .setValue(OPENED, false)
                .setValue(PART, BedPart.FOOT)
                .setValue(FACING, Direction.NORTH)
        );
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (Set.of(Direction.NORTH, Direction.SOUTH).contains(state.getValue(FACING))) {
            return Block.box(2.0D, 0.0D, 0.0D, 14.0D, 14.0D, 16.0D);
        } else {
            return Block.box(0.0D, 0.0D, 2.0D, 16.0D, 14.0D, 14.0D);
        }
    }

    @Override
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.isClientSide()) return InteractionResult.SUCCESS;

        if (state.getValue(OPENED)) return super.useItemOn(itemStack, state, level, pos, player, hand, hitResult);

        if (itemStack.getItem() == ModItems.CRYPT_KEY.get()) {
            BlockPos otherPos = pos.relative(getNeighbourDirection(state.getValue(PART), state.getValue(FACING)));
            BlockState otherState = level.getBlockState(otherPos);

            level.setBlockAndUpdate(pos, state.setValue(OPENED, true));
            level.setBlockAndUpdate(otherPos, otherState.setValue(OPENED, true));

            level.addAlwaysVisibleParticle(ParticleTypes.POOF, pos.getX(), pos.getY(), pos.getZ(), 0.5d, 0.5d, 0.5d);

            if (!player.isCreative()) {
                itemStack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }

        return super.useItemOn(itemStack, state, level, pos, player, hand, hitResult);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction facing = context.getHorizontalDirection();
        BlockPos pos = context.getClickedPos();
        BlockPos otherPos = pos.relative(facing);
        Level level = context.getLevel();

        // if space to place full one
        if (level.getBlockState(otherPos).canBeReplaced(context) && level.getWorldBorder().isWithinBounds(otherPos)) {
            // this is the blockstate used on the place you clicked, for the foot
            return this.defaultBlockState().setValue(FACING, facing).setValue(PART, BedPart.FOOT);
        }
        return null; // cannot place
    }

    @Override
    protected float getDestroyProgress(BlockState state, Player player, BlockGetter level, BlockPos pos) {
        if (!state.getValue(OPENED)) {
            player.sendSystemMessage(Component.literal("Hmm, maybe this could be opened first..."));
            return 0.0f;
        }
        return super.getDestroyProgress(state, player, level, pos);
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        // below is fix for double block dropping an item in creative
        if (!level.isClientSide() && player.preventsBlockDrops()) {
            BedPart part = state.getValue(PART);
            if (part == BedPart.FOOT) {
                BlockPos headPos = pos.relative(getNeighbourDirection(part, state.getValue(FACING)));
                BlockState headState = level.getBlockState(headPos);
                if (headState.is(this) && headState.getValue(PART) == BedPart.HEAD) {
                    level.setBlock(headPos, Blocks.AIR.defaultBlockState(), 35);
                    level.levelEvent(player, 2001, headPos, Block.getId(headState));
                }
            }
        }

        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity by, ItemStack itemStack) {
        super.setPlacedBy(level, pos, state, by, itemStack);
        if (!level.isClientSide()) {
            BlockPos headPos = pos.relative(state.getValue(FACING));
            level.setBlockAndUpdate(headPos, state.setValue(PART, BedPart.HEAD));
            // do I need this?
            state.updateNeighbourShapes(level, pos, 3);
        }
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
        Direction connectedDirection = getNeighbourDirection(state.getValue(PART), state.getValue(FACING));
        if (directionToNeighbour == connectedDirection) {
            if (!neighbourState.is(this) || neighbourState.getValue(PART) == state.getValue(PART)) {
                return Blocks.AIR.defaultBlockState();
            }
        }
        return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
    }

    private static Direction getNeighbourDirection(BedPart part, Direction facing) {
        return part == BedPart.FOOT ? facing : facing.getOpposite();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(OPENED, PART, FACING);
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }
}
