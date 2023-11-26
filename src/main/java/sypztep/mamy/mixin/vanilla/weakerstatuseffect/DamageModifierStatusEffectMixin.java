package sypztep.mamy.mixin.vanilla.weakerstatuseffect;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.DamageModifierStatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sypztep.mamy.common.ModConfig;

@Mixin(DamageModifierStatusEffect.class)
public class DamageModifierStatusEffectMixin {
	@SuppressWarnings("ConstantConditions")
	@Inject(method = "adjustModifierAmount", at = @At("RETURN"), cancellable = true)
	private void mamy$weakerPotions(int amplifier, EntityAttributeModifier modifier, CallbackInfoReturnable<Double> cir) {
		if (!ModConfig.weakerPotions) {
			cir.setReturnValue(cir.getReturnValueD());
		}
		if (DamageModifierStatusEffect.class.cast(this) == StatusEffects.STRENGTH) {
			cir.setReturnValue((cir.getReturnValueD() * 1.0f / 2.0f)); // from 3 to 1.5 heart
		}
//			else if (DamageModifierStatusEffect.class.cast(this) == StatusEffects.WEAKNESS) {
//				cir.setReturnValue(cir.getReturnValueD() * 1F / 4F);
//			}
	}
}
