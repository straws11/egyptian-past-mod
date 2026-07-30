package net.straws11.egyptianpast.block;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import static net.straws11.egyptianpast.EgyptianPast.MOD_ID;
import static net.straws11.egyptianpast.item.ModItemRegistration.ITEMS;

public class ModBlockRegistration {

    // Create a Deferred Register to hold Blocks which will all be registered under the "egyptianpast" namespace
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MOD_ID);

    public static final DeferredBlock<Block> LIMESTONE =
            BLOCKS.registerSimpleBlock("limestone", p -> p.strength(0.8f)
                    .overrideDescription("im like sandstone")
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE)
            );

    public static final DeferredItem<BlockItem> LIMESTONE_ITEM =
            ITEMS.registerSimpleBlockItem("limestone", LIMESTONE);

    public static final DeferredBlock<Block> PAPYRUS_REED_BLOCK =
            BLOCKS.registerBlock("papyrus_reed",
                    p -> new PapyrusReedBlock(
                            p.noCollision()
                            .instabreak()
                            .pushReaction(PushReaction.DESTROY)
            ));

    public static final DeferredItem<BlockItem> PAPYRUS_REED =
            ITEMS.registerSimpleBlockItem("papyrus_reed", PAPYRUS_REED_BLOCK);

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

    public static ResourceKey<Block> getBlockResourceKey(Block block) {
        return BuiltInRegistries.BLOCK.getResourceKey(block).get();
    }
}
