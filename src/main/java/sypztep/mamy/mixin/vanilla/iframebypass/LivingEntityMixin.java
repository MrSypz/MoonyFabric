package sypztep.mamy.mixin.vanilla.iframebypass;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sypztep.mamy.common.interfaces.EntityHurtCallback;
import sypztep.mamy.common.interfaces.EntityKnockbackCallback;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(at = @At("TAIL"), method = "applyDamage", cancellable = true)
    private void onEntityHurt(final DamageSource source, final float amount, CallbackInfo ci) {
        ActionResult result = EntityHurtCallback.EVENT.invoker().hurtEntity((LivingEntity) (Object) this, source,
                amount);
        if (result == ActionResult.FAIL) {
            ci.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "takeKnockback", cancellable = true)
    private void onTakingKnockback(double strength, double x, double z, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;

        ActionResult result = EntityKnockbackCallback.EVENT.invoker().takeKnockback(entity, entity.getAttacker(), strength, x, z);
        if (result == ActionResult.FAIL) {
            ci.cancel();
        }

    }
}
