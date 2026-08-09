package net.straws11.egyptianpast.worldgen;

import net.minecraft.core.HolderGetter;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.straws11.egyptianpast.datagen.ModBiomes;

public class ModSurfaceRuleManager {
    public static SurfaceRules.RuleSource makeRules(HolderGetter<Biome> biomes) {
        var biomeHolder = biomes.getOrThrow(ModBiomes.EGYPTIAN_DESERT);
        SurfaceRules.ConditionSource isEgyptianDesert = SurfaceRules.isBiome(
            biomes,
            biomeHolder.key()
        );

        SurfaceRules.RuleSource sandSurface = SurfaceRules.sequence(
            SurfaceRules.ifTrue(
                SurfaceRules.ON_FLOOR,
                SurfaceRules.state(Blocks.SAND.defaultBlockState())
            ),
            // subsurface
            SurfaceRules.ifTrue(
                SurfaceRules.UNDER_FLOOR,
                SurfaceRules.state(Blocks.SANDSTONE.defaultBlockState())
            )
        );
        return SurfaceRules.ifTrue(isEgyptianDesert, sandSurface);
    }
}
