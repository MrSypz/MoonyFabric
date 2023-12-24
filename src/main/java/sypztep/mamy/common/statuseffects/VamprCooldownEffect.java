package sypztep.mamy.common.statuseffects;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import sypztep.mamy.common.init.ModEnchantments;
import sypztep.mamy.common.init.ModStatusEffects;

public class VamprCooldownEffect extends EmptyStatusEffect {
    public VamprCooldownEffect() {
        super(StatusEffectCategory.HARMFUL, 0);
    }
    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if(EnchantmentHelper.getLevel(ModEnchantments.VAMPR, entity.getMainHandStack()) == 0) {
            int currentDuration = (entity.getStatusEffect(ModStatusEffects.VAMPR_COOLDOWN)).getDuration();
            entity.addStatusEffect(new StatusEffectInstance(ModStatusEffects.VAMPR_COOLDOWN, currentDuration + 20));
        }
        super.applyUpdateEffect(entity, amplifier);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return duration % 20 == 0;
    }
}
