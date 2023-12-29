package sypztep.mamy.common.enchantment;

import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.SwordItem;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import sypztep.mamy.common.init.ModDamageTypes;
import sypztep.mamy.common.init.ModSoundEvents;
import sypztep.mamy.common.init.ModStatusEffects;
import sypztep.mamy.common.util.EnchantmentUtil;

public class LethalTempoEnchantment extends OnHitApplyEnchantment{
    public LethalTempoEnchantment(Rarity weight, EnchantmentTarget target, EquipmentSlot... slotTypes) {
        super(weight, target, slotTypes);
    }

    @Override
    public void onTargetDamaged(LivingEntity user, Entity target, int level) {
        float damage = (float) user.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) * 0.65f;
        if (EnchantmentUtil.hasEnchantment(this, user.getMainHandStack()) && user.distanceTo(target) >= 6 ) {
            return;
        }
        if (user.getWorld() instanceof ServerWorld && target instanceof LivingEntity LivingTarget && user.getMainHandStack().getItem() instanceof SwordItem) {
            StatusEffectInstance bid = ((LivingEntity) target).getStatusEffect(ModStatusEffects.BRING_IT_DOWN);
            if (bid != null && bid.getAmplifier() > 6 - level) {
                if (!LivingTarget.equals(user)) {
                    LivingTarget.damage(LivingTarget.getWorld().getDamageSources().create(ModDamageTypes.BRINGITDOWN, user), damage);
                    target.playSound(ModSoundEvents.ENTITY_PLAYER_ATTACK_LETHAL, 1f, (float) (1.0f + user.getRandom().nextGaussian() / 15f));
                    ((LivingEntity) target).removeStatusEffect(ModStatusEffects.BRING_IT_DOWN);
                    if (user.getWorld() instanceof ServerWorld) { // Particle
                        double xdif = LivingTarget.getX() - user.getX();
                        double ydif = LivingTarget.getBodyY(0.5D) - user.getBodyY(0.5D);
                        double zdif = LivingTarget.getZ() - user.getZ();

                        int particleNumConstant = 20; //number of particles
                        double x = 0;
                        double y = 0;
                        double z = 0;
                        while (Math.abs(x) < Math.abs(xdif)) {
                            ((ServerWorld) target.getWorld()).spawnParticles(ParticleTypes.CRIT, user.getX() + x,
                                    user.getBodyY(0.5D) + y, user.getZ() + z, 0, 1, 0.5D, 1, 0.0D);
                            x = x + xdif / particleNumConstant;
                            y = y + ydif / particleNumConstant;
                            z = z + zdif / particleNumConstant;
                        }
                    }
                }
            }
        }
    }
}
