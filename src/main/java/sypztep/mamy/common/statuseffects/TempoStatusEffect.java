package sypztep.mamy.common.statuseffects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectCategory;

public class TempoStatusEffect extends EmptyStatusEffect{
    public TempoStatusEffect() {
        super(StatusEffectCategory.BENEFICIAL, 0);
    }
    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
    }
    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return duration <= 1;
    }
}
