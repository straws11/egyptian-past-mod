package net.straws11.egyptianpast.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public class MummyEntity extends Monster {

    public MummyEntity(EntityType<? extends MummyEntity> type, Level level) {
        super(type, level);
    }

}
