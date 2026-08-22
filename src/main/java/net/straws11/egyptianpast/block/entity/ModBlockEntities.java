package net.straws11.egyptianpast.block.entity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.straws11.egyptianpast.EgyptianPast;
import net.straws11.egyptianpast.block.ModBlocks;

import java.util.function.Supplier;


public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, EgyptianPast.MOD_ID);

    public static final Supplier<BlockEntityType<PedestalBlockEntity>> PEDESTAL_BE =
            BLOCK_ENTITIES.register("pedestal_be", () -> new BlockEntityType<>(
                    PedestalBlockEntity::new, ModBlocks.PEDESTAL_BLOCK.get()
            ));

    public static final Supplier<BlockEntityType<CanopicJarBlockEntity>> CANOPIC_JAR_BE =
        BLOCK_ENTITIES.register("canopic_jar_be", () -> new BlockEntityType<>(
            CanopicJarBlockEntity::new, ModBlocks.CANOPIC_JAR_BLOCK.get()
        ));

    public static final Supplier<BlockEntityType<SphinxBlockEntity>> SPHINX_BE =
        BLOCK_ENTITIES.register("sphinx_be", () -> new BlockEntityType<>(
            SphinxBlockEntity::new, ModBlocks.SPHINX_BLOCK.get()
        ));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
