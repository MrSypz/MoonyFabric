package sypztep.mamy.mixin.statuseffect.electrodecree;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sypztep.mamy.common.init.ModEnchantments;
import sypztep.mamy.common.init.ModStatusEffects;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Inject(at = @At("HEAD"), method = "attack")
    private void attack(Entity target, CallbackInfo ci) {
        int electro = EnchantmentHelper.getLevel(ModEnchantments.ELECTRO_DECREE, ((PlayerEntity)(Object) this).getMainHandStack());
        if(electro != 0) {
            if (target.isAttackable() && !target.getWorld().isClient) {
                if (!target.handleAttack((PlayerEntity)(Object) this) && target instanceof LivingEntity && !((PlayerEntity)(Object) this).handSwinging) {
                    int mark = 0;
                    StatusEffectInstance markInstance = ((LivingEntity) target).getStatusEffect(ModStatusEffects.ELECTRO_MARK);
                    if(markInstance!= null)
                        mark = markInstance.getAmplifier() + 2; // + (amp เท่าไรถึงจะระเบิด)
                    ((LivingEntity) target).addStatusEffect(new StatusEffectInstance(ModStatusEffects.ELECTRO_MARK, 20 + electro * 4, mark));
                    ((ServerWorld) ((PlayerEntity)(Object) this).getWorld()).spawnParticles(ParticleTypes.ELECTRIC_SPARK, target.getX(), target.getBodyY(0.5D), target.getZ(), 22, 0.4, 0.6, 0.4, 0.0D);
                }
            }
        }
    }
}
