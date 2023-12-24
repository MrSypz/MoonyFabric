package sypztep.mamy.common.enchantment;

import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import sypztep.mamy.common.init.ModDamageTypes;
import sypztep.mamy.common.init.ModStatusEffects;
import sypztep.mamy.common.util.EnchantmentUtil;

import java.util.List;

public class ElectroEnchantment extends EmptyEnchantment {
    public ElectroEnchantment(Rarity weight, EnchantmentTarget target, EquipmentSlot... slotTypes) {
        super(weight, target, slotTypes);
    }

    @Override
    public void onTargetDamaged(LivingEntity user, Entity target, int level) {
        List<LivingEntity> list = target.getWorld().getNonSpectatingEntities(LivingEntity.class, target.getBoundingBox()
                .expand(8.5D, 0.25D, 8.5D));
        if (EnchantmentUtil.hasEnchantment(this, user.getMainHandStack()) && user.distanceTo(target) >= 6) {
            return;
        }
        if (user.getWorld() instanceof ServerWorld && target instanceof LivingEntity) {
            StatusEffectInstance mark = ((LivingEntity) target).getStatusEffect(ModStatusEffects.ELECTRO);
            if(mark != null && mark.getAmplifier() > 8 - level) {
                for (LivingEntity e : list) {
                    if (!e.equals(user)) {
                        double distanceToTarget = e.squaredDistanceTo(target.getX(), target.getY(), target.getZ());
                        double normalizedDistance = Math.sqrt(distanceToTarget) / 8.5D; // Normalize distance to a range of 0 to 1
                        float damage = level - (float) (normalizedDistance * (level - 0.1f));
                        e.damage(e.getWorld().getDamageSources().create(ModDamageTypes.ELECTRO,user),  level + damage);
                        target.playSound(SoundEvents.BLOCK_DECORATED_POT_SHATTER,1f, (float) (1.0f + user.getRandom().nextGaussian() / 15f));
                        ((LivingEntity) target).removeStatusEffect(ModStatusEffects.ELECTRO);
                        if (user.getWorld() instanceof ServerWorld) { // Particle
                            double xdif = e.getX() - target.getX();
                            double ydif = e.getBodyY(0.5D) - target.getBodyY(0.5D);
                            double zdif = e.getZ() - target.getZ();
                            int particleNumConstant = 20;
                            double x = 0;
                            double y = 0;
                            double z = 0;
                            while (Math.abs(x) < Math.abs(xdif)) {
                                ((ServerWorld) target.getWorld()).spawnParticles(ParticleTypes.END_ROD, target.getX() + x,
                                        target.getBodyY(0.5D) + y, target.getZ() + z, 0, 1, 0.0D, 1, 0.1D);
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

    @Override
    public int getMaxLevel() {
        return 5;
    }
    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof SwordItem;
    }
}
