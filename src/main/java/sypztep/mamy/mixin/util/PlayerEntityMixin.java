package sypztep.mamy.mixin.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sypztep.mamy.common.Item.CustomHitParticleItem;
import sypztep.mamy.common.Item.CustomHitSoundItem;
import sypztep.mamy.common.init.ModItems;
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
//        return this.getStackInHand(Hand.MAIN_HAND).getItem() == ModItems.DEATH_SCYTHE ? (T) ModParticles.RED_SWEEP_ATTACK_PARTICLE : value;
    }
}
