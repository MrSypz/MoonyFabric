package sypztep.mamy.mixin.util.storedamage;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sypztep.mamy.common.util.EnchantmentUtil;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {


    @Shadow public abstract ItemStack getMainHandStack();

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }
    @Inject(method = "modifyAppliedDamage", at = @At("HEAD"))
    private void applyDamage(DamageSource source, float amount, CallbackInfoReturnable<Float> cir) {
        if (source.getAttacker() != null) {
            EnchantmentUtil.getDamageAmount(amount);
        }
    }
}


