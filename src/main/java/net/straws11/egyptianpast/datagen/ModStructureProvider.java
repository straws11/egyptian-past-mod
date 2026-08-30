package net.straws11.egyptianpast.datagen;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.straws11.egyptianpast.EgyptianPast;

public class ModStructureProvider {

    public static final ResourceKey<Structure> ANCIENT_PYRAMID_STRUCTURE = ResourceKey.create(
        Registries.STRUCTURE, Identifier.fromNamespaceAndPath(EgyptianPast.MOD_ID, "ancient_pyramid_structure")
    );

    public static final ResourceKey<StructureSet> ANCIENT_PYRAMID_STRUCTURE_SET = ResourceKey.create(
        Registries.STRUCTURE_SET, Identifier.fromNamespaceAndPath(EgyptianPast.MOD_ID, "ancient_pyramid_structure_set")
    );

    public static final ResourceKey<StructureTemplatePool> ANCIENT_PYRAMID_START_POOL = ResourceKey.create(
        Registries.TEMPLATE_POOL, Identifier.fromNamespaceAndPath(EgyptianPast.MOD_ID, "ancient_pyramid/start_pool")
    );
}
