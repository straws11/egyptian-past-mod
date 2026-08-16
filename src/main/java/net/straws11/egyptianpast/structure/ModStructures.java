package net.straws11.egyptianpast.structure;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;
import net.straws11.egyptianpast.EgyptianPast;
import net.straws11.egyptianpast.datagen.ModBiomes;

import java.util.List;
import java.util.Map;

public class ModStructures {

    // Keys
    public static final ResourceKey<Structure> TEST_STRUCTURE = ResourceKey.create(
        Registries.STRUCTURE,
        Identifier.fromNamespaceAndPath(EgyptianPast.MOD_ID, "test_structure")
    );

    public static final ResourceKey<StructureTemplatePool> TEST_STRUCTURE_POOL = ResourceKey.create(
        Registries.TEMPLATE_POOL,
        Identifier.fromNamespaceAndPath(EgyptianPast.MOD_ID, "test_structure_pool")
    );

    public static void bootstrapPools(BootstrapContext<StructureTemplatePool> context) {
        HolderGetter<StructureTemplatePool> poolGetter = context.lookup(Registries.TEMPLATE_POOL);
        Holder<StructureTemplatePool> empty = poolGetter.getOrThrow(Pools.EMPTY);

        context.register(
            TEST_STRUCTURE_POOL,
            new StructureTemplatePool(
                empty,
                List.of(
                    Pair.of(
                        // Points to: data/egyptianpast/structure/test_structure.nbt
                        StructurePoolElement.single(EgyptianPast.MOD_ID + ":test_structure"),
                        1
                    )
                ),
                StructureTemplatePool.Projection.RIGID
            )
        );
    }

    // Bootstrap for Registries.STRUCTURE
    public static void bootstrapStructures(BootstrapContext<Structure> context) {
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        HolderGetter<StructureTemplatePool> pools = context.lookup(Registries.TEMPLATE_POOL);

        Holder<Biome> egyptianDesertHolder = biomes.getOrThrow(ModBiomes.EGYPTIAN_DESERT);

        context.register(
            TEST_STRUCTURE,
            new JigsawStructure(
                // 1. Structure Settings (Biomes, Spawn Overrides, Generation Step)
                new Structure.StructureSettings(
                    HolderSet.direct(egyptianDesertHolder),
                    Map.of(),
                    GenerationStep.Decoration.SURFACE_STRUCTURES,
                    TerrainAdjustment.BEARD_THIN
                ),
                // 2. Start Pool (Points to the pool registered above)
                pools.getOrThrow(TEST_STRUCTURE_POOL),
                // 3. Max depth from start pool (1 for a single nbt file)
                1,
                // 4. Height provider (Places on surface terrain)
                ConstantHeight.of(VerticalAnchor.absolute(0)),
                // 5. Use expansion hack
                false,
                // 6. Heightmap projection
                Heightmap.Types.WORLD_SURFACE_WG
            )
        );
    }
}