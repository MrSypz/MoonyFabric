package sypztep.mamy.common.enchantment;

import moriyashiine.enchancement.common.entity.projectile.IceShardEntity;
import moriyashiine.enchancement.common.init.ModEntityTypes;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.SwordItem;
import net.minecraft.world.World;

import java.util.Random;

public class GaleEnchantment extends TridentEnchantment {

    public GaleEnchantment(Rarity weight, EnchantmentTarget target, EquipmentSlot... slotTypes) {
        super(weight, target, slotTypes);
    }
    @Override
    public void onTargetDamaged(LivingEntity user, Entity target, int level) {
        if(target instanceof LivingEntity) {
            DamageSource damageSource = ((LivingEntity) target).getRecentDamageSource();
            if(damageSource != null && !damageSource.getType().msgId().equals("trident") || (user.getOffHandStack().getItem() instanceof SwordItem))
                return;
        }
        if (target instanceof LivingEntity) {
            World world = user.getWorld();
            double centerX = target.getX(); // x-coordinate of the target
            double centerY = target.getY(); // y-coordinate of the target
            double centerZ = target.getZ(); // z-coordinate of the target
            double height = 40 + (level * 2); // height above the target

            int numberOfShards = 128; // number of IceShardEntity objects to spawn
            IceShardEntity[] shardEntities = new IceShardEntity[numberOfShards];
            Random random = new Random();

            for (int i = 0; i < numberOfShards; i++) {
                shardEntities[i] = ModEntityTypes.ICE_SHARD.create(world);
                double randomAngle = Math.toRadians(random.nextDouble() * 360); // random angle in radians
                double randomRadius = Math.sqrt(random.nextDouble()) * 4; // random radius within the circle's radius

                // Generating random coordinates within the circle
                double x = centerX + randomRadius * Math.cos(randomAngle);
                double y = centerY + height + (5 * Math.random()); // Adjusting for height
                double z = centerZ + randomRadius * Math.sin(randomAngle);

                // Setting position for IceShardEntity
                assert shardEntities[i] != null;
                shardEntities[i].setPos(x, y, z);

                // Set velocity or other necessary settings for the IceShardEntity
                shardEntities[i].setVelocity(0, -2 + (-1 * level), 0); // Example velocity adjustment

                // Spawning IceShardEntity in the world
                world.spawnEntity(shardEntities[i]);
            }
        }
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }
}