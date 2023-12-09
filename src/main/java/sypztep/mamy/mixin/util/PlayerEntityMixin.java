package sypztep.mamy.mixin.util;
/*
    PLAYER MAIN MODIFY CRIT CHANCE AND DAMAGE
 */

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sypztep.mamy.common.Item.CustomHitParticleItem;
import sypztep.mamy.common.Item.CustomHitSoundItem;
import sypztep.mamy.common.init.ModEntityAttributes;
import sypztep.mamy.common.init.ModParticles;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity{
    @Shadow public abstract ItemStack getEquippedStack(EquipmentSlot slot);

    @Shadow public abstract float getAttackCooldownProgress(float baseTime);

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }
    @Inject(method = {"attack"},at = {@At(value = "INVOKE",target = "Lnet/minecraft/entity/player/PlayerEntity;getAttackCooldownProgress(F)F")})
    private void mamy$spawnCustomHitParticlesAndPlayCustomHitSound(Entity target, CallbackInfo ci) {
        if (this.getAttackCooldownProgress(0.5F) > 0.9F) {
            Item item = this.getMainHandStack().getItem();
            PlayerEntity player = PlayerEntity.class.cast(this);
            if (item instanceof CustomHitParticleItem) {
                CustomHitParticleItem customHitParticleItem = (CustomHitParticleItem)item;
                customHitParticleItem.spawnHitParticles(player);
            }

            item = this.getMainHandStack().getItem();
            if (item instanceof CustomHitSoundItem) {
                CustomHitSoundItem customHitSoundItem = (CustomHitSoundItem)item;
                customHitSoundItem.playHitSound(player);
            }
        }
    }

    @ModifyArg(method = "spawnSweepAttackParticles",at = @At(value = "INVOKE",target = "Lnet/minecraft/server/world/ServerWorld;spawnParticles(Lnet/minecraft/particle/ParticleEffect;DDDIDDDD)I"))
    private <T extends ParticleEffect> T mamy$disableSweepingattack(T value) {
        Item item = this.getMainHandStack().getItem();
        if (item instanceof CustomHitParticleItem) {
            return (T) ModParticles.EMPTY_PARTICLE;
        }
        return value;
    }
//    @Inject(method = "createPlayerAttributes", at = @At("RETURN"), cancellable = true)
//    private static void mamy$createplayerAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
//        DefaultAttributeContainer.Builder builder = cir.getReturnValue();
//        builder.add(ModEntityAttributes.GENERIC_HOGYOKU,0);
//        builder.add(ModEntityAttributes.GENERIC_CRIT_CHANCE,0);
//        cir.setReturnValue(builder);
//    }
    @Inject(method = "createPlayerAttributes",at = @At("RETURN"))
    private static void initAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> ci) {
        ci.getReturnValue().add(ModEntityAttributes.GENERIC_HOGYOKU);
        ci.getReturnValue().add(ModEntityAttributes.GENERIC_CRIT_CHANCE);
    }

    @ModifyVariable(method = "damage", at = @At("HEAD"), argsOnly = true)
    private float mamy$HollowCurse(float amount) {
        if (this.getAttributes().getBaseValue(ModEntityAttributes.GENERIC_HOGYOKU) > 0) { // if player have
            return amount + (amount * (0.15f)); //15% more Damage
        }
        return amount;
    }
    @ModifyVariable(method = "attack",at = @At(value = "JUMP",ordinal = 2),slice = @Slice(from = @At(value = "INVOKE",target = "Lnet/minecraft/entity/player/PlayerEntity;isSprinting()Z", ordinal = 1)),index = 8) //Crit Chance
    private boolean attack(boolean bl3) {
        float customChance = 0.0f;
        EntityAttributeInstance instance = this.getAttributeInstance(ModEntityAttributes.GENERIC_CRIT_CHANCE);
        if(instance != null) {
            for (EntityAttributeModifier modifier : instance.getModifiers()) {
                float amount = (float) modifier.getValue();
                customChance += amount;
            }
        }
        return bl3 || getWorld().random.nextDouble() < customChance;
    }
    @ModifyConstant(method = "attack", constant = @Constant(floatValue = 1.5f), require = 1) // Increase Crit Damage 20%
    private float mamy$modifyCritDamage(float originalValue) {
        if (this.getAttributes().getBaseValue(ModEntityAttributes.GENERIC_HOGYOKU) > 0) {
            return (1.5f + 0.2f);
        }
        return originalValue;
    }
}
