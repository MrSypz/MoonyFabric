package sypztep.mamy.mixin.statuseffect.hollowpower;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import sypztep.mamy.common.init.ModStatusEffects;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Shadow public abstract boolean hasStatusEffect(StatusEffect effect);

    @Shadow
    public abstract @Nullable StatusEffectInstance getStatusEffect(StatusEffect effect);

    @ModifyVariable(method = "damage", at = @At("HEAD"), argsOnly = true)
    private float mamy$HollowPower(float amount) {
        if (this.hasStatusEffect(ModStatusEffects.HOLLOW_POWER)) {
            return amount + (amount * (0.15f * (this.getStatusEffect(ModStatusEffects.HOLLOW_POWER).getAmplifier() + 1))); //15% more Damage per level
        }
        return amount;
    }
}
