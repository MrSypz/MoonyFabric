package sypztep.mamy.common.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import sypztep.mamy.common.init.ModDamageTypes;
import sypztep.mamy.common.init.ModParticles;

import java.util.List;

public class SkillUtil {
    int counts;
    public void ShockWaveDamage(PlayerEntity user,double range, float amount, boolean useCustomDamage,boolean doEffect) {
        List<LivingEntity> entities = user.getWorld().getNonSpectatingEntities(LivingEntity.class, user.getBoundingBox().expand(range,1, range));
        counts = 0;
        for (LivingEntity target : entities) {
            if (target != user) {
                double distanceToEntity = target.squaredDistanceTo(user.getX(), user.getY(), user.getZ());
                double normalizedDistance = Math.sqrt(distanceToEntity) / range; // Adjust as needed for your range
                if (normalizedDistance > 0) {
                    counts++;
                } else counts = 0;
                if (!useCustomDamage) { // Damage when equipMask
                    float damagebyArea = (float) (32f + range - (float) (normalizedDistance * (32f + range - 1.0f)));
                    target.damage(target.getWorld().getDamageSources().create(ModDamageTypes.MASKIMPACT, user), damagebyArea);
                } else {
                    float damagebyCustom = (amount - (float) (normalizedDistance * (amount - 0.1f)));
                    target.damage(target.getWorld().getDamageSources().create(ModDamageTypes.BLOODSCYTHE, user), damagebyCustom);
                }
                if (doEffect) {
                    if (user.getWorld() instanceof ServerWorld) { // Particle
                        double xdif = target.getX() - user.getX();
                        double ydif = target.getBodyY(0.5D) - user.getBodyY(0.5D);
                        double zdif = target.getZ() - user.getZ();
                        int particleNumConstant = 20;
                        double x = 0;
                        double y = 0;
                        double z = 0;
                        while (Math.abs(x) < Math.abs(xdif)) {
                            ((ServerWorld) target.getWorld()).spawnParticles(ModParticles.BLOOD_BUBBLE, user.getX() + x,
                                    user.getBodyY(0.5D) + y, user.getZ() + z, 0, 1, 0.0D, 1, 0.1D);
                            x = x + xdif / particleNumConstant;
                            y = y + ydif / particleNumConstant;
                            z = z + zdif / particleNumConstant;
                        }
                    }
                }
            }
        }
    }
    public int getCounts() {
        return this.counts;
    }
}
