package sypztep.mamy.mixin.statuseffect.grievouswounds;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sypztep.mamy.common.init.ModStatusEffects;


@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    private LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow
    public abstract boolean hasStatusEffect(StatusEffect effect);

    @Shadow
    public abstract void setHealth(float health);

    @Shadow
    public abstract float getHealth();


    @Inject(method = "heal", at = @At("HEAD"), cancellable = true)
    private void mamy$GrievousReduceHeal(float amount, CallbackInfo ci) {
        if (this.hasStatusEffect(ModStatusEffects.GRIEVOUSWOUNDS) && this.getHealth() > 0.0f) {
            this.setHealth(this.getHealth() + (amount *= 0.6f));
            ci.cancel();
        }
    }
}




