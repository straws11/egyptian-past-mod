package net.straws11.egyptianpast.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStackResourceHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.straws11.egyptianpast.block.ModBlocks;
import net.straws11.egyptianpast.block.PedestalBlock;
import net.straws11.egyptianpast.item.CanopicJarBlockItem;
import net.straws11.egyptianpast.item.ICursedItem;
import net.straws11.egyptianpast.item.OrganType;
import net.straws11.egyptianpast.recipe.ModRecipes;
import net.straws11.egyptianpast.recipe.PedestalRecipe;
import net.straws11.egyptianpast.recipe.PedestalRecipeInput;
import net.straws11.egyptianpast.stat.ModStats;
import net.straws11.egyptianpast.util.CurseUtils;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.*;

public class PedestalBlockEntity extends BlockEntity {
    // Ritual state fields
    private static final int RITUAL_DURATION = 60;
    private int ritualTicks = 0;
    private List<BlockPos> cachedChildPedestals = new ArrayList<>();
    private UUID triggeringPlayerId = null;
    private RecipeHolder<PedestalRecipe> activeRecipe;
    private PedestalRecipeInput recipeInput;

    private ItemStack storedStack = ItemStack.EMPTY;
    private final ItemStackResourceHandler itemHandler = new ItemStackResourceHandler() {

        @Override
        protected ItemStack getStack() {
            return storedStack;
        }

        @Override
        protected void setStack(ItemStack stack) {
            storedStack = stack;
            setChanged();
        }

        @Override
        protected void onRootCommit(ItemStack originalState) {
            super.onRootCommit(originalState);
            if (level != null && !level.isClientSide()) {
                BlockState state = getBlockState();
                level.sendBlockUpdated(worldPosition, state, state, PedestalBlock.UPDATE_ALL);
            }
        }

        @Override
        protected int getCapacity(@NonNull ItemResource resource) {
            return 1;
        }
    };

    public PedestalBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(ModBlockEntities.PEDESTAL_BE.get(), worldPosition, blockState);
    }

    public ItemStack getStoredStack() {
        return this.storedStack;
    }

    public ItemStackResourceHandler getItemHandler() {
        return this.itemHandler;
    }

    public void drops() {
        SimpleContainer inv = new SimpleContainer(itemHandler.size());
        // in this case we don't need a loop since there's always only one item, but leaving this for example's sake
        for (int i = 0; i < itemHandler.size(); i++) {
            ItemAccess itemAccess = ItemAccess.forHandlerIndex(itemHandler, 0);
            inv.setItem(i, itemAccess.getResource().toStack(itemAccess.getAmount()));
        }
        Containers.dropContents(this.level, this.worldPosition, inv);
    }

    // genuinely no clue if either of these will work lol
    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        this.itemHandler.serialize(output);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.itemHandler.deserialize(input);
    }

    private boolean consumeCanopicJars(List<BlockPos> childPedestals, Transaction transaction) {
        for (BlockPos pos: childPedestals) {
            assert this.level != null && !this.level.isClientSide();
            PedestalBlockEntity pedestal = (PedestalBlockEntity)level.getBlockEntity(pos);
            int extracted = pedestal.getItemHandler().extract(
                    ItemResource.of(pedestal.getStoredStack()), 1, transaction
            );
            if (extracted == 0) return false;
            pedestal.setChanged();
        }
        return true;
    }

    /**
     * Replaces item with recipe output version
     * @param resultStack the recipe result stack
     * @param root the current transaction
     */
    private void replaceCenterPedestalItem(ItemStack resultStack, Transaction root) {
        ItemStack storedStack = getStoredStack();

        try (Transaction transaction = Transaction.open(root)) {
            int extracted = this.itemHandler.extract(ItemResource.of(storedStack), 1, transaction);
            if (extracted > 0) {
                int inserted = this.itemHandler.insert(ItemResource.of(resultStack), 1, transaction);
                if (inserted > 0) {
                    transaction.commit();
                }
            }
        }
        this.setChanged();
    }

    /* BlockEntity sync methods, not sure what's happening here*/
    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Override
    public void onDataPacket(Connection net, ValueInput valueInput) {
        loadAdditional(valueInput);
    }

    // ritual stuff
    public void tryStartRitual(Player player) {
        if (this.level == null || this.level.isClientSide() || this.ritualTicks > 0) return;
        if (this.getStoredStack().isEmpty()) return;

        // gather pedestal items
        List<BlockPos> childPedestalPositions = Direction.Plane.HORIZONTAL.stream()
            .map(dir -> getBlockPos().relative(dir, 2))
            .toList();

        List<ItemStack> outerItems = new ArrayList<>();

        for (BlockPos pos : childPedestalPositions) {
            if (!(level.getBlockEntity(pos) instanceof PedestalBlockEntity childBe)) return;
            if (childBe.getStoredStack().isEmpty()) return;

            outerItems.add(childBe.getStoredStack());
        }

        // build recipe input
        this.recipeInput = new PedestalRecipeInput(this.getStoredStack(), outerItems);

        Optional<RecipeHolder<PedestalRecipe>> match = this.level.getServer().getRecipeManager()
            .getRecipeFor(ModRecipes.PEDESTAL_RITUAL_TYPE.get(), this.recipeInput, this.level);

        if (match.isEmpty()) {
            System.out.println("no recipe match!");
            return; // no matching recipes found
        }

        this.activeRecipe = match.get();
        this.ritualTicks = RITUAL_DURATION;
        this.cachedChildPedestals = new ArrayList<>(childPedestalPositions);
        this.triggeringPlayerId = player.getUUID();

        this.level.playSound(null, this.worldPosition, SoundEvents.BEACON_ACTIVATE,
            SoundSource.BLOCKS, 1f, 1.2f);
        setChanged();
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, PedestalBlockEntity be) {
        if (be.ritualTicks <= 0) return;

        be.ritualTicks--;

        if (level instanceof ServerLevel serverLevel) {
            Vec3 centerTarget = Vec3.atCenterOf(pos).add(0, 0.75, 0);
            // Stream particles from each child pedestal towards this center pedestal
            for (BlockPos childPos : be.cachedChildPedestals) {
                Vec3 start = Vec3.atCenterOf(childPos).add(0, 0.8, 0);
                Vec3 direction = centerTarget.subtract(start);

                double speed = 0.25;
                Vec3 velocity = direction.normalize().scale(speed);

                serverLevel.sendParticles(
                    ParticleTypes.SOUL_FIRE_FLAME,
                    start.x + (serverLevel.getRandom().nextDouble() - 0.5) * 0.2,
                    start.y + (serverLevel.getRandom().nextDouble() - 0.5) * 0.2,
                    start.z + (serverLevel.getRandom().nextDouble() - 0.5) * 0.2,
                    1,
                    velocity.x, velocity.y + 0.05, velocity.z,
                    speed
                );

                serverLevel.sendParticles(
                    ParticleTypes.ENCHANT,
                    start.x, start.y, start.z,
                    2,
                    velocity.x * 0.8, velocity.y * 0.8, velocity.z * 0.8,
                    0.5
                );
            }

            // Rising center particles
            serverLevel.sendParticles(
                ParticleTypes.PORTAL,
                centerTarget.x, centerTarget.y, centerTarget.z,
                3,
                0.2, 0.2, 0.2,
                0.05
            );

            // Audible pulse every 10 ticks
            if (be.ritualTicks % 10 == 0) {
                float pitch = 0.8f + (1.0f - (float) be.ritualTicks / RITUAL_DURATION) * 0.8f;
                serverLevel.playSound(null, pos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 0.7f, pitch);
            }

            // Timer complete: perform cleansing and consume jars
            if (be.ritualTicks == 0) {
                be.finishRitual(serverLevel, pos);
            }
        }
    }

    public void finishRitual(ServerLevel serverLevel, BlockPos pos) {
        if (this.activeRecipe == null) return;

        Player player = this.triggeringPlayerId != null ? serverLevel.getPlayerByUUID(this.triggeringPlayerId) : null;

        try (Transaction transaction = Transaction.openRoot()) {
            // TODO: figure out transactions better
            boolean removed = consumeCanopicJars(this.cachedChildPedestals, transaction);
            ItemStack resultStack = this.activeRecipe.value().assemble(this.recipeInput);
            replaceCenterPedestalItem(resultStack, transaction);
            if (removed) {
                transaction.commit();
                if (player != null) {
                    player.awardStat(ModStats.ITEMS_CLEANSED.get(), 1);
                }
            }
        }
        // Completion burst
        Vec3 center = Vec3.atCenterOf(pos).add(0, 0.8, 0);
        serverLevel.sendParticles(ParticleTypes.SOUL, center.x, center.y, center.z, 30, 0.5, 0.5, 0.5, 0.1);
        serverLevel.sendParticles(ParticleTypes.FLAME, center.x, center.y, center.z, 1, 0, 0, 0, 0);
        serverLevel.playSound(null, pos, SoundEvents.GENERIC_EXPLODE.value(), SoundSource.BLOCKS, 0.8f, 1.4f);
        serverLevel.playSound(null, pos, SoundEvents.TOTEM_USE, SoundSource.BLOCKS, 1.0f, 1.0f);

        this.cachedChildPedestals.clear();
        this.triggeringPlayerId = null;
        this.activeRecipe = null;
        this.recipeInput = null;
    }
}
