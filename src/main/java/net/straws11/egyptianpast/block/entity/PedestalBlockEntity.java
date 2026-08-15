package net.straws11.egyptianpast.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStackResourceHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.straws11.egyptianpast.block.ModBlocks;
import net.straws11.egyptianpast.block.PedestalBlock;
import net.straws11.egyptianpast.data.ModDataComponentRegistration;
import net.straws11.egyptianpast.item.CanopicJarBlockItem;
import net.straws11.egyptianpast.item.ICursedItem;
import net.straws11.egyptianpast.item.OrganType;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.*;

public class PedestalBlockEntity extends BlockEntity {
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

        /*@Override
        protected boolean isValid(ItemResource resource) {
            return resource.getItem() instanceof ICursedItem
                    || resource.is(ModBlocks.CANOPIC_JAR.get());
        }*/

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

    public void checkSuccessfulPedestalConfiguration(Level level) {
        ItemStack stack = this.getStoredStack();
        // early return if this isn't the main pedestal
        // NOTE: ^^ this means you have to place this item last

        if (!stack.getOrDefault(ModDataComponentRegistration.IS_CURSED.get(), false)) return;

        Set<OrganType> foundOrgans = new HashSet<>();

        List<BlockPos> childPedestalPositions = Direction.Plane.HORIZONTAL.stream()
                .map(dir -> getBlockPos().relative(dir, 2))
                .toList();

        List<PedestalBlockEntity> childPedestals = new ArrayList<>();

        for (BlockPos blockPos : childPedestalPositions) {
            if (!(level.getBlockEntity(blockPos) instanceof PedestalBlockEntity otherPedestalBe)) break;
            if (!otherPedestalBe.getStoredStack().is(ModBlocks.CANOPIC_JAR)) break;
            childPedestals.add(otherPedestalBe);

            OrganType organ = CanopicJarBlockItem.getOrgan(otherPedestalBe.getStoredStack());
            if (organ == OrganType.EMPTY) break;
            foundOrgans.add(organ);
        }

        // all but the empty one needs to be present
        if (foundOrgans.size() < OrganType.values().length - 1) return;
        try (Transaction transaction = Transaction.openRoot()) {
            performCleansing(getBlockPos(), level);
            boolean successful = consumeCanopicJars(childPedestals, transaction);
            if (successful) {
                transaction.commit();
            }
        }
    }

    private boolean consumeCanopicJars(List<PedestalBlockEntity> childPedestals, Transaction transaction) {
        for (PedestalBlockEntity pedestal : childPedestals) {
            int extracted = pedestal.getItemHandler().extract(
                    ItemResource.of(pedestal.getStoredStack()), 1, transaction
            );
            if (extracted == 0) return false;
            pedestal.setChanged();
        }
        return true;
    }

    /**
     * Replaces item with cleansed version
     * @param pos position of the main pedestal's BlockEntity
     * @param level current level
     */
    private void performCleansing(BlockPos pos, Level level) {
        ItemStack storedStack = getStoredStack();

        if (!(storedStack.getItem() instanceof ICursedItem cursedItem)) return;
        if (!cursedItem.isCursed(storedStack)) return;

        // in place cleanse, no need to remove and insert
        cursedItem.cleanse(storedStack);
        this.setChanged();

        if (level != null && !level.isClientSide()) {
            level.sendBlockUpdated(pos, getBlockState(), getBlockState(), PedestalBlock.UPDATE_ALL);
            level.playSound(null, pos, SoundEvents.BEACON_ACTIVATE, SoundSource.BLOCKS, 1f, 1f);
        }
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
}
