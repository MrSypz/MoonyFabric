package sypztep.mamy.mixin.statuseffect.lethaltempo;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sypztep.mamy.common.init.ModEnchantments;
import sypztep.mamy.common.init.ModStatusEffects;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Inject(at = @At("HEAD"), method = "getAttackCooldownProgressPerTick", cancellable = true)
    public void getAttackCooldownProgressPerTick(CallbackInfoReturnable<Float> cir) {
        PlayerEntity entity = ((PlayerEntity)(Object) this);
        StatusEffectInstance tempoInstance = entity.getStatusEffect(ModStatusEffects.LETHALTEMPO);
        if(tempoInstance != null) {
            int tempCount = tempoInstance.getAmplifier();
            cir.setReturnValue((float)(1.0D / entity.getAttributeValue(EntityAttributes.GENERIC_ATTACK_SPEED) * (14.5D - (tempCount))));
        }
    }
    @Inject(at = @At("HEAD"), method = "attack")
    private void attack(Entity target, CallbackInfo ci) {
        int lethal = EnchantmentHelper.getLevel(ModEnchantments.LETHAL_TEMPO, ((PlayerEntity)(Object) this).getMainHandStack());
        if(lethal != 0 && target.isAttackable() && !target.getWorld().isClient) {
            if (target.isAttackable() && !target.getWorld().isClient) {
                if (!target.handleAttack((PlayerEntity)(Object) this) && target instanceof LivingEntity && !((PlayerEntity)(Object) this).handSwinging) {
                    int bid = 0;
                    StatusEffectInstance markInstance = ((LivingEntity) target).getStatusEffect(ModStatusEffects.BRING_IT_DOWN);
                    if(markInstance!= null)
                        bid = markInstance.getAmplifier() + 1; // + (amp เท่าไรถึงจะระเบิด)
                    ((LivingEntity) target).addStatusEffect(new StatusEffectInstance(ModStatusEffects.BRING_IT_DOWN, 20 + lethal * 4, bid));
                    ((ServerWorld) ((PlayerEntity)(Object) this).getWorld()).spawnParticles(ParticleTypes.ENCHANTED_HIT, target.getX(), target.getBodyY(0.5D), target.getZ(), 22, 0.4, 0.6, 0.4, 0.0D);
                }
            }
        }
    }
}
