package sypztep.mamy.common;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import sypztep.mamy.common.init.ModStatusEffects;

public class MarkStatusEffect extends EmptyStatusEffect {
    public MarkStatusEffect() {
        super(StatusEffectCategory.HARMFUL, 0);
    }
    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {

        entity.removeStatusEffect(ModStatusEffects.ELECTRO_MARK);
        entity.addStatusEffect(new StatusEffectInstance(ModStatusEffects.ELECTRO_MARK, amplifier * 20, 0));

        super.applyUpdateEffect(entity, amplifier);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return duration <= 5 && amplifier != 0;
    }
}
