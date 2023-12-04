package sypztep.mamy.mixin.statuseffect.carve;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sypztep.mamy.common.init.*;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity{
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(at = @At("HEAD"), method = "attack")
    private void attack(Entity target, CallbackInfo ci) {
        int carve = EnchantmentHelper.getLevel(ModEnchantments.CARVE, ((PlayerEntity)(Object) this).getMainHandStack());
        if(carve != 0 && target.isAttackable() && !target.getWorld().isClient) {
            if (!target.handleAttack((PlayerEntity) (Object) this) && target instanceof LivingEntity && !((PlayerEntity) (Object) this).handSwinging) {
                int carvecount = 0;
                if (((LivingEntity) target).getArmor() > 0) {
                    StatusEffectInstance markInstance = ((LivingEntity) target).getStatusEffect(ModStatusEffects.CARVE);
                    if (markInstance != null) {
                        carvecount = markInstance.getAmplifier();
                        if (carvecount < carve) {
                            carvecount++;
                            if (carvecount == 5)
                                target.playSound(ModSoundEvents.ITEM_CARVE, 1, (float) (1.0 + ((LivingEntity) target).getRandom().nextGaussian() / 10.0));
                        }
                    }
                    ((LivingEntity) target).addStatusEffect(new StatusEffectInstance(ModStatusEffects.CARVE, 20 + carve * 4, carvecount));
                    ((ServerWorld) ((PlayerEntity) (Object) this).getWorld()).spawnParticles(ParticleTypes.SCULK_SOUL, target.getX(), target.getBodyY(0.5D), target.getZ(), 18, 0.3, 0.6, 0.3, 0.01D);
                } else {
                    target.playSound(ModSoundEvents.ENTITY_GENERIC_BLOODHIT, 1, (float) (1.0 + ((LivingEntity) target).getRandom().nextGaussian() / 10.0));
                    ((ServerWorld) ((PlayerEntity) (Object) this).getWorld()).spawnParticles(ModParticles.BLOOD_BUBBLE_SPLATTER, target.getX(), target.getBodyY(0.5D), target.getZ(), 18, 0.3, 0.6, 0.3, 0.21D);
                    ((LivingEntity) target).addStatusEffect(new StatusEffectInstance(sypztep.pickyourpoison.common.init.ModStatusEffects.VULNERABILITY, 20 + carve * 4, 0));
                }
            }
        }
    }
}
