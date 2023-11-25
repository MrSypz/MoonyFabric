package sypztep.mamy.mixin.enchantment.bringerdeath;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sypztep.mamy.common.component.entity.BringerComponent;
import sypztep.mamy.common.enchantment.BringerDeathEnchantment;

@Mixin({LivingEntity.class})
public abstract class LivingEntityMixin {
    @Inject(method = "applyDamage", at = @At("HEAD"), cancellable = true)
    protected void applyDamage(DamageSource source, float amount, CallbackInfo callbackInfo) {
        LivingEntity livingEntity = LivingEntity.class.cast(this);
        if (source.isOf(DamageTypes.PLAYER_ATTACK)) {
            if (!livingEntity.isInvulnerableTo(source) && BringerComponent.hasDeathBringer && BringerComponent.getDeathCooldown() == 0) {
                BringerDeathEnchantment.getAmount(amount);
                callbackInfo.cancel();
            }
        }
    }
}