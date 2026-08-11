package net.straws11.egyptianpast.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStackResourceHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.straws11.egyptianpast.block.ModBlockRegistration;
import net.straws11.egyptianpast.block.PedestalBlock;
import net.straws11.egyptianpast.item.CanopicJar;
import net.straws11.egyptianpast.item.ModItemRegistration;
import net.straws11.egyptianpast.item.OrganType;
import org.jspecify.annotations.NonNull;

import java.util.*;
import java.util.stream.Collectors;

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

        @Override
        protected boolean isValid(ItemResource resource) {
            Set<Item> validPedestalItems = Set.of(
                    ModItemRegistration.CANOPIC_JAR.get(),
                    Items.DIAMOND,
                    Items.NETHERITE_INGOT
            );
            return validPedestalItems.contains(resource.toStack().getItem());
        }

        @Override
        protected int getCapacity(@NonNull ItemResource resource) {
            return 1;
        }
    };

    public PedestalBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(ModBlockEntityRegistration.PEDESTAL_BE.get(), worldPosition, blockState);
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

    public void checkSuccessfulPedestalConfiguration(Level level, BlockPos pos) {
        ItemStack stack = this.getStoredStack();
        // early return if this isn't the main pedestal
        // NOTE: ^^ this means you have to place this item last
        if (!stack.is(Items.DIAMOND)) return;
        // TODO: make this the actual "cursed" item

        Set<OrganType> foundOrgans = new HashSet<>();

        // TODO: refactor, this sucks lol
        for (Direction direction : List.of(Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST)) {
            BlockPos blockPos = pos.relative(direction, 2);
            System.out.println(blockPos.toShortString());
            if (level.getBlockEntity(blockPos) instanceof PedestalBlockEntity pedestalBlockEntity) {
                if (pedestalBlockEntity.getStoredStack().is(ModItemRegistration.CANOPIC_JAR)) {
                    OrganType organ = CanopicJar.getOrgan(pedestalBlockEntity.getStoredStack());
                    if (organ != OrganType.EMPTY) {
                        foundOrgans.add(organ);
                    } else break;

                    continue;
                }
                break;
            }
            break;
        }

        // all but the empty one needs to be present
        if (foundOrgans.size() < OrganType.values().length - 1) return;

        try (Transaction transaction = Transaction.openRoot()) {
            int extracted = this.itemHandler.extract(ItemResource.of(Items.DIAMOND), 1, transaction);
            if (extracted > 0) {
                int inserted = this.itemHandler
                        .insert(ItemResource.of(Items.NETHERITE_INGOT), 1, transaction);
                if (inserted > 0) {
                    transaction.commit();
                    level.playSound(null, pos, SoundEvents.ANVIL_USE, SoundSource.BLOCKS, 1f, 1f);
                    // TODO: remove canopic jars from pedestals
                }
            }
        }
    }
}
