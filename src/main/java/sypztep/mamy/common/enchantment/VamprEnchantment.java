package sypztep.mamy.common.enchantment;

import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import sypztep.mamy.common.init.ModParticles;
import sypztep.mamy.common.init.ModStatusEffects;
import sypztep.mamy.common.util.EnchantmentUtil;

import java.util.List;


public class VamprEnchantment extends EmptyEnchantment{
    public VamprEnchantment(Rarity weight, EnchantmentTarget target, EquipmentSlot... slotTypes) {
        super(weight, target, slotTypes);
    }

    @Override
    public void onTargetDamaged(LivingEntity user, Entity target, int level) {
        if (!EnchantmentUtil.hasEnchantment(this, user.getMainHandStack()) || user.distanceTo(target) >= 6 || !(target instanceof LivingEntity)){
            return;
        }
        if (user.getStatusEffect(ModStatusEffects.VAMPR_COOLDOWN) == null) {
            List<LivingEntity> list = target.getWorld().getNonSpectatingEntities(LivingEntity.class, target.getBoundingBox()
                    .expand(1 + level, 0.25D, 1 + level));
            int counter = 0;

            for (LivingEntity e : list) {
                if (!e.equals(user)) {
                    counter++;
                    e.damage(target.getWorld().getDamageSources().magic(), 1.0f);
                    target.playSound(SoundEvents.BLOCK_AMETHYST_BLOCK_STEP,1.0f,1.3f);
                    if (target.getWorld() instanceof ServerWorld) {
                        double xdif = e.getX() - user.getX();
                        double ydif = e.getBodyY(0.5D) - user.getBodyY(0.5D);
                        double zdif = e.getZ() - user.getZ();

                        int particleNumConstant = 20; //number of particles
                        double x = 0;
                        double y = 0;
                        double z = 0;
                        while(Math.abs(x) < Math.abs(xdif))
                        {
                            ((ServerWorld) target.getWorld()).spawnParticles(ModParticles.BLOOD_BUBBLE_SPLATTER, user.getX() + x,
                                    user.getBodyY(0.5D) + y, user.getZ() + z, 0, 1, 0.0D, 1, 0.0D);
                            x = x + xdif/particleNumConstant;
                            y = y + ydif/particleNumConstant;
                            z = z + zdif/particleNumConstant;
                        }
                    }
                }
            }
            user.heal(counter + (level) + (EnchantmentUtil.AmountDeal * 0.3f));
            user.addStatusEffect(new StatusEffectInstance(ModStatusEffects.VAMPR_COOLDOWN, 300 - (level * 20)));
            if(user.getWorld() instanceof ServerWorld)
                ((ServerWorld) user.getWorld()).spawnParticles(ParticleTypes.HEART, user.getX(), user.getBodyY(0.5D), user.getZ(), 3, 0.4, 0.5, 0.4, 0.0D);
        }
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }
}
