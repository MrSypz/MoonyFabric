/*
 * All Rights Reserved (c) MoriyaShiine
 */

package sypztep.mamy.mixin.enchantment.hollowcurse;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import sypztep.mamy.common.component.entity.VizardComponent;
import sypztep.mamy.common.init.ModEntityComponents;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@ModifyArg(method = "applyMovementInput", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;updateVelocity(FLnet/minecraft/util/math/Vec3d;)V"))
	private float enchancement$strafe(float value) {
		if (!isOnGround()) {
			VizardComponent vizardComponent = ModEntityComponents.VIZARD.getNullable(this);
			if (vizardComponent != null && vizardComponent.getTicksInAir() > 5) {
				return value * 2.0f;
			}
		}
		return value;
	}
	@ModifyVariable(method = "handleFallDamage", at = @At("HEAD"), ordinal = 0, argsOnly = true)
	private float mamy$airhike(float value) {
		VizardComponent vizardComponent = ModEntityComponents.VIZARD.getNullable(this);
		if (vizardComponent != null && VizardComponent.hasMask) {
			value = Math.max(0, value - 8);
		}
		return value;
	}
}