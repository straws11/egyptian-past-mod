package net.straws11.egyptianpast.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import static net.straws11.egyptianpast.EgyptianPast.MOD_ID;
import static net.straws11.egyptianpast.item.EgyptianPastItemRegistration.ITEMS;

public class EgyptianPastBlockRegistration {

    // Create a Deferred Register to hold Blocks which will all be registered under the "egyptianpast" namespace
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MOD_ID);

    // Creates a new Block with the id "egyptianpast:example_block", combining the namespace and path
    public static final DeferredBlock<Block> EXAMPLE_BLOCK =
            BLOCKS.registerSimpleBlock("example_block", p -> p.mapColor(MapColor.STONE));

    // Creates a new BlockItem with the id "egyptianpast:example_block", combining the namespace and path
    public static final DeferredItem<BlockItem> EXAMPLE_BLOCK_ITEM =
            ITEMS.registerSimpleBlockItem("example_block", EXAMPLE_BLOCK);

    public static final DeferredBlock<Block> LIMESTONE =
            BLOCKS.registerSimpleBlock("limestone", p -> p.strength(0.8f)
                    .overrideDescription("im like sandstone")
                    .sound(SoundType.STONE)
            );

    public static final DeferredItem<BlockItem> LIMESTONE_ITEM =
            ITEMS.registerSimpleBlockItem("limestone", LIMESTONE);

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

}
