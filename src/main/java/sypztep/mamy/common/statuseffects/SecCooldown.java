package sypztep.mamy.common.statuseffects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectCategory;

public class SecCooldown extends EmptyStatusEffect {
    public SecCooldown() {
        super(StatusEffectCategory.HARMFUL, 0);
    }
    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        super.applyUpdateEffect(entity, amplifier);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return duration % 20 == 0;
    }
}