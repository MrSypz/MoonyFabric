package sypztep.mamy.common.enchantment;

import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.Random;

public class GaleEnchantment extends BownCrossbowEnchantment {


    public GaleEnchantment(Rarity weight, EnchantmentTarget target, EquipmentSlot... slotTypes) {
        super(weight, target, slotTypes);
    }

    @Override
    public void onTargetDamaged(LivingEntity user, Entity target, int level) {
        if(target instanceof LivingEntity) {
            DamageSource damageSource = ((LivingEntity) target).getRecentDamageSource();
            if(((damageSource != null) && !damageSource.getType().msgId().equals("arrow")) ||
                    (user.getOffHandStack().getItem() instanceof BowItem) ||
                    (user.getOffHandStack().getItem() instanceof CrossbowItem))
                return;
        }
        if (target instanceof LivingEntity) {
            if (target.getWorld().raycast(new RaycastContext(target.getPos(),target.getPos().add(0,44,0),RaycastContext.ShapeType.COLLIDER,RaycastContext.FluidHandling.NONE,target)).getType() == HitResult.Type.MISS) {
                World world = user.getWorld();
                double centerX = target.getX();
                double centerY = target.getY();
                double centerZ = target.getZ();
                double height = 40 + (level * 2);

                int numberOfArrows = 16 * level;
                ArrowEntity[] arrowEntities = new ArrowEntity[numberOfArrows];
                Random random = new Random();

                for (int i = 0; i < numberOfArrows; i++) {
                    arrowEntities[i] = EntityType.ARROW.create(world);
                    double randomAngle = Math.toRadians(random.nextDouble() * 360);
                    double randomRadius = Math.sqrt(random.nextDouble()) * 6;

                    // Generating random coordinates within the circle
                    double x = centerX + randomRadius * Math.cos(randomAngle);
                    double y = centerY + height + (5 * Math.random());
                    double z = centerZ + randomRadius * Math.sin(randomAngle);

                    // Setting position for IceShardEntity
                    assert arrowEntities[i] != null;
                    arrowEntities[i].setPos(x, y, z);

                    // Set velocity or other necessary settings for the IceShardEntity
                    arrowEntities[i].setVelocity(0, -2 + (-1 * level), 0); // Example velocity adjustment

                    // Spawning Arrow in the world
                    world.spawnEntity(arrowEntities[i]);
                }
            }
        }
    }
    @Override
    public int getMaxLevel() {
        return 2;
    }

}