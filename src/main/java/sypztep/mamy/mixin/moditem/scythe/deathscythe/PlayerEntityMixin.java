package sypztep.mamy.mixin.moditem.scythe.deathscythe;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sypztep.mamy.common.Item.HollowmaskItem;
import sypztep.mamy.common.init.ModItems;
import sypztep.mamy.common.init.ModParticles;

import static sypztep.pickyourpoison.common.init.ModStatusEffects.STIMULATION;
import static sypztep.pickyourpoison.common.init.ModStatusEffects.VULNERABILITY;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    @Shadow public abstract ItemStack getEquippedStack(EquipmentSlot slot);

    @Shadow public abstract HungerManager getHungerManager();

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }
    @ModifyArg(method = "spawnSweepAttackParticles",at = @At(value = "INVOKE",target = "Lnet/minecraft/server/world/ServerWorld;spawnParticles(Lnet/minecraft/particle/ParticleEffect;DDDIDDDD)I"))
    private <T extends ParticleEffect> T mamy$disableSweepingattack(T value) {
        return this.getStackInHand(Hand.MAIN_HAND).getItem() == ModItems.DEATH_SCYTHE ? (T) ModParticles.RED_SWEEP_ATTACK_PARTICLE : value;
    }
}
