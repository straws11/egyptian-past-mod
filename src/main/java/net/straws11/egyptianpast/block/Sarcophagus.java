package net.straws11.egyptianpast.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EquipmentSlot;
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
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.straws11.egyptianpast.entity.ModEntities;
import net.straws11.egyptianpast.entity.Pharaoh;
import net.straws11.egyptianpast.item.ModItems;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Sarcophagus extends HorizontalDirectionalBlock {

    public static final MapCodec<Sarcophagus> CODEC = simpleCodec(Sarcophagus::new);

    public static final EnumProperty<SarcophagusPart> PART = EnumProperty.create("part", SarcophagusPart.class);
    public static final BooleanProperty OPENED = BooleanProperty.create("opened");

    public Sarcophagus(Properties properties) {
        super(properties);
        // this is default to false anyway but here's an example
        this.registerDefaultState(stateDefinition.any()
                .setValue(OPENED, true)
                .setValue(PART, SarcophagusPart.FEET)
                .setValue(FACING, Direction.NORTH)
        );
    }

//    @Override
//    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
//        if (Set.of(Direction.NORTH, Direction.SOUTH).contains(state.getValue(FACING))) {
//            return Block.box(2.0D, 0.0D, 0.0D, 14.0D, 14.0D, 16.0D);
//        } else {
//            return Block.box(0.0D, 0.0D, 2.0D, 16.0D, 14.0D, 14.0D);
//        }
//    }

    @Override
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.isClientSide()) return InteractionResult.SUCCESS;

        if (state.getValue(OPENED)) return super.useItemOn(itemStack, state, level, pos, player, hand, hitResult);

        if (itemStack.getItem() == ModItems.CRYPT_KEY.get()) {
            List<BlockPos> otherPositions = getOtherPositions(pos, state.getValue(PART), state.getValue(FACING));
            List<BlockState> otherStates = otherPositions.stream().map(level::getBlockState).toList();

            level.setBlockAndUpdate(pos, state.setValue(OPENED, true));

            for (int i = 0; i < otherPositions.size(); i++) {
                level.setBlockAndUpdate(otherPositions.get(i), otherStates.get(i).setValue(OPENED, true));
            }

            level.addAlwaysVisibleParticle(ParticleTypes.POOF, pos.getX(), pos.getY(), pos.getZ(), 0.5d, 0.5d, 0.5d);

            // find middle block position
            BlockPos middlePos = pos;
            for (int i = 0; i < 2; i++) {
                if (otherStates.get(i).getValue(PART) == SarcophagusPart.MIDDLE) {
                    middlePos = otherPositions.get(i);
                    break;
                }
            }

            spawnPharaoh((ServerLevel) level, middlePos, player);

            if (!player.isCreative()) {
                itemStack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }

        return super.useItemOn(itemStack, state, level, pos, player, hand, hitResult);
    }

    private void spawnPharaoh(ServerLevel level, BlockPos middlePos, Player player) {
        ModEntities.PHARAOH_ENTITY.get().spawn(
            (ServerLevel) level,
            pharaoh -> {
                pharaoh.moveOrInterpolateTo(player.getYRot() + 180f, 0f);
                ItemStack crown = new ItemStack(ModItems.PHARAOH_CROWN.get());
                // random damage 25 to 75%
                crown.setDamageValue((int) (crown.getMaxDamage() * (0.25 + level.getRandom().nextFloat() * 0.50)));
                pharaoh.setItemSlot(EquipmentSlot.HEAD, crown);
                pharaoh.setGuaranteedDrop(EquipmentSlot.HEAD);
                pharaoh.setPersistenceRequired();
                pharaoh.setTarget(player);
            },
            BlockPos.containing(new Vec3(middlePos).add(new Vec3(0.5, 1, 0.5))),
            EntitySpawnReason.TRIGGERED,
            false,
            false
        );
        level.playSound(null, middlePos, SoundEvents.ENDER_CHEST_OPEN,
            SoundSource.BLOCKS, 2.5f, 0f);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction facing = context.getHorizontalDirection();
        BlockPos pos = context.getClickedPos();
        List<BlockPos> otherPositions = getOtherPositions(pos, SarcophagusPart.FEET, facing);
        Level level = context.getLevel();

        // if space to place full one
        for (BlockPos otherPos : otherPositions) {
            if (!level.getBlockState(otherPos).canBeReplaced(context)
                || !level.getWorldBorder().isWithinBounds(otherPos)) return null; // cannot place
        }

        // this is the blockstate used on the place you clicked, for the foot
        return this.defaultBlockState().setValue(FACING, facing).setValue(PART, SarcophagusPart.FEET);
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
            SarcophagusPart part = state.getValue(PART);

            if (part == SarcophagusPart.FEET) {
                List<BlockPos> otherPositions = getOtherPositions(pos, SarcophagusPart.FEET, state.getValue(FACING));
                List<BlockState> otherStates = otherPositions.stream().map(level::getBlockState).toList();

                for (int i = 0; i < otherPositions.size(); i++) {
                    if (otherStates.get(i).is(this)
                        && List.of(SarcophagusPart.MIDDLE, SarcophagusPart.HEAD).contains(otherStates.get(i).getValue(PART))) {
                        level.setBlock(otherPositions.get(i), Blocks.AIR.defaultBlockState(), 35);
                        level.levelEvent(player, 2001, otherPositions.get(i), Block.getId(otherStates.get(i)));
                    }

                }
            }
        }

        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity by, ItemStack itemStack) {
        super.setPlacedBy(level, pos, state, by, itemStack);
        if (!level.isClientSide()) {
            List<BlockPos> otherPositions = getOtherPositions(pos, SarcophagusPart.FEET, state.getValue(FACING));
            level.setBlockAndUpdate(otherPositions.get(0), state.setValue(PART, SarcophagusPart.MIDDLE));
            level.setBlockAndUpdate(otherPositions.get(1), state.setValue(PART, SarcophagusPart.HEAD));
            // do I need this?
            state.updateNeighbourShapes(level, pos, 3);
        }
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos,
                                     Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState,
                                     RandomSource random) {
        SarcophagusPart currentPart = state.getValue(PART);
        Direction facing = state.getValue(FACING);

        SarcophagusPart expectedNeighborPart = getExpectedNeighborPart(currentPart, facing, directionToNeighbour);
        if (expectedNeighborPart != null) {
            if (!neighbourState.is(this) || neighbourState.getValue(PART) != expectedNeighborPart) {
                return Blocks.AIR.defaultBlockState();
            }
        }

        return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
    }

    private static List<BlockPos> getOtherPositions(BlockPos pos, SarcophagusPart part, Direction facing) {
        if (part == SarcophagusPart.HEAD) {
            return List.of(
                pos.relative(facing.getOpposite()),
                pos.relative(facing.getOpposite(), 2)
            );

        } else if (part == SarcophagusPart.MIDDLE) {
            return List.of(
                pos.relative(facing),
                pos.relative(facing.getOpposite())
            );

        } else {
            return List.of(
                pos.relative(facing),
                pos.relative(facing, 2)
            );
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(OPENED, PART, FACING);
    }

    private SarcophagusPart getExpectedNeighborPart(SarcophagusPart currentPart, Direction facing, Direction directionToNeighbor) {
        return switch (currentPart) {
            case FEET -> (directionToNeighbor == facing) ? SarcophagusPart.MIDDLE : null;
            case MIDDLE -> {
                if (directionToNeighbor == facing) yield SarcophagusPart.HEAD;
                if (directionToNeighbor == facing.getOpposite()) yield SarcophagusPart.FEET;
                yield null;
            }
            case HEAD -> (directionToNeighbor == facing.getOpposite()) ? SarcophagusPart.MIDDLE : null;
        };
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }
}
