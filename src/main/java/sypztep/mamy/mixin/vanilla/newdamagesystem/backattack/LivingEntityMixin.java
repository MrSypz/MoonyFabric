package sypztep.mamy.mixin.vanilla.newdamagesystem.backattack;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import sypztep.mamy.common.init.ModParticles;

import java.util.Objects;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	@Shadow
	public abstract float getHeadYaw();
	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@ModifyVariable(method = "modifyAppliedDamage", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/enchantment/EnchantmentHelper;getProtectionAmount(Ljava/lang/Iterable;Lnet/minecraft/entity/damage/DamageSource;)I"), argsOnly = true)
	private float mamy$backattack(float value, DamageSource source) {
		if (source.isIn(DamageTypeTags.IS_DROWNING) || source.isIn(DamageTypeTags.BYPASSES_EFFECTS) || source.isOf(DamageTypes.FALL) || source.isIn(DamageTypeTags.IS_PROJECTILE)) { //not apply projectile it too op
			return value;
		}
		if (source.getSource() != null) {
			if (Math.abs(MathHelper.subtractAngles(getHeadYaw(), source.getSource().getHeadYaw())) <= 75) {
				if (!Objects.requireNonNull(source.getAttacker()).getWorld().isClient() && getWorld() instanceof ServerWorld serverWorld) {
					double offsetX = -Math.sin(Math.toRadians(source.getSource().getYaw())) * 1.5; // Change 1.5 to desired distance
					double offsetZ = Math.cos(Math.toRadians(source.getSource().getYaw())) * 1.5; // Change 1.5 to desired distance
					double spawnX = source.getSource().getX() + offsetX;
					double spawnY = source.getSource().getY() + 1; // Adjust the Y position as needed
					double spawnZ = source.getSource().getZ() + offsetZ;

					serverWorld.spawnParticles(ModParticles.BACKATTACK, spawnX , spawnY, spawnZ, 1, 0, 0, 0, 0.1);
				}
				return value * 1.5F;
			}
		}
		return value;
	}
}
