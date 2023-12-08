package sypztep.mamy.mixin.moditem.deathscythe;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sypztep.mamy.common.Item.DeathScytheItem;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    private Entity target;
    private CallbackInfo ci;

    @Shadow public abstract SoundCategory getSoundCategory();


    @Shadow public abstract boolean damage(DamageSource source, float amount);

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    /*
    Pull player if crit
     */
    @Inject(method = "attack",at = @At(value = "INVOKE",target = "Lnet/minecraft/entity/player/PlayerEntity;addCritParticles(Lnet/minecraft/entity/Entity;)V"))
    private void attack(Entity target, CallbackInfo ci) {
        if (this.getStackInHand(Hand.MAIN_HAND).getItem() instanceof DeathScytheItem) {
            this.getWorld().playSound((PlayerEntity) null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_FISHING_BOBBER_RETRIEVE, this.getSoundCategory(), 1.0F, 1.0F);
                target.setVelocity(this.getPos().subtract(target.getPos()).multiply(0.25));
                target.velocityModified = true;
        }
    }
}
