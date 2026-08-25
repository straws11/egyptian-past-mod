package net.straws11.egyptianpast.entity.ai.goal;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.straws11.egyptianpast.entity.Mummy;

public class MummyAttackGoal extends MeleeAttackGoal {
    private final Mummy mummy;
    private int raiseArmTicks;

    public MummyAttackGoal(Mummy mummy, double speedModifier, boolean trackTarget) {
        super(mummy, speedModifier, trackTarget);
        this.mummy = mummy;
    }

    @Override
    public void start() {
        super.start();
        this.raiseArmTicks = 0;
    }

    @Override
    public void stop() {
        super.stop();
        this.mummy.setAggressive(false);
    }

    @Override
    public void tick() {
        super.tick();
        this.raiseArmTicks++;
        this.mummy.setAggressive(this.raiseArmTicks >= 5 && this.getTicksUntilNextAttack() < this.getAttackInterval() / 2);
    }
}
