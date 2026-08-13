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
import net.straws11.egyptianpast.data.ModDataComponentRegistration;
import net.straws11.egyptianpast.item.CanopicJarBlockItem;
import net.straws11.egyptianpast.item.OrganType;

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

    public static final DeferredBlock<Sarcophagus> SARCOPHAGUS =
            BLOCKS.registerBlock("sarcophagus", p -> new Sarcophagus(
                    p.strength(1.5F, 6.0F)
            ));

    public static final DeferredBlock<Block> EGYPTIAN_STONE =
            BLOCKS.registerSimpleBlock("egyptian_stone",p ->
                    p.strength(0.8f, 0.5f)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.STONE)
            );

    public static final DeferredItem<BlockItem> EGYPTIAN_STONE_ITEM =
            ITEMS.registerSimpleBlockItem("egyptian_stone", EGYPTIAN_STONE);

    public static final DeferredBlock<Block> EGYPTIAN_COBBLESTONE =
            BLOCKS.registerSimpleBlock("egyptian_cobblestone",p ->
                    p.strength(0.8f, 0.5f)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.STONE)
            );

    public static final DeferredItem<BlockItem> EGYPTIAN_COBBLESTONE_ITEM =
            ITEMS.registerSimpleBlockItem("egyptian_cobblestone", EGYPTIAN_COBBLESTONE);

    public static final DeferredItem<BlockItem> SARCOPHAGUS_ITEM =
            ITEMS.registerSimpleBlockItem("sarcophagus", SARCOPHAGUS);

    public static final DeferredBlock<PedestalBlock> PEDESTAL_BLOCK =
            BLOCKS.registerBlock("pedestal", p ->
                    new PedestalBlock(p.strength(2f).requiresCorrectToolForDrops())
            );

    public static final DeferredItem<BlockItem> PEDESTAL_ITEM =
            ITEMS.registerSimpleBlockItem("pedestal", PEDESTAL_BLOCK);

    public static final DeferredBlock<CanopicJarBlock> CANOPIC_JAR_BLOCK =
        BLOCKS.registerBlock("canopic_jar", p ->
            new CanopicJarBlock(p.strength(1f).requiresCorrectToolForDrops()));

    public static final DeferredItem<CanopicJarBlockItem> CANOPIC_JAR =
        ITEMS.registerItem("canopic_jar", p ->
            new CanopicJarBlockItem(CANOPIC_JAR_BLOCK.get(), p));


    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

    public static ResourceKey<Block> getBlockResourceKey(Block block) {
        return BuiltInRegistries.BLOCK.getResourceKey(block).get();
    }
}
