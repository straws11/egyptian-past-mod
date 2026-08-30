package net.straws11.egyptianpast.structure;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.straws11.egyptianpast.EgyptianPast;

public class ModStructureSets {
    public static final ResourceKey<StructureSet> ANCIENT_PYRAMID_STRUCTURE_SET =
        ResourceKey.create(Registries.STRUCTURE_SET, Identifier.fromNamespaceAndPath(
            EgyptianPast.MOD_ID, "ancient_pyramid_structures")
        );

    public static void bootstrap(BootstrapContext<StructureSet> context) {
        HolderGetter<Structure> structures = context.lookup(Registries.STRUCTURE);

        context.register(
            ANCIENT_PYRAMID_STRUCTURE_SET,
            new StructureSet(
                structures.getOrThrow(ModStructures.ANCIENT_PYRAMID),
                new RandomSpreadStructurePlacement(
                    32,
                    8,
                    RandomSpreadType.LINEAR,
                    19862986
                )
            )
        );
    }

}
