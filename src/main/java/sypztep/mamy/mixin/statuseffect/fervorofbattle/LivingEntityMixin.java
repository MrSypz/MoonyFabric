package sypztep.mamy.mixin.statuseffect.fervorofbattle;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sypztep.mamy.common.init.ModEnchantments;
import sypztep.mamy.common.init.ModStatusEffects;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Shadow public abstract ItemStack getMainHandStack();

    @Shadow public abstract @Nullable StatusEffectInstance getStatusEffect(StatusEffect effect);

    @Shadow public boolean handSwinging;

    @Shadow public abstract boolean addStatusEffect(StatusEffectInstance effect);

    @Inject(at = @At("HEAD"), method = "onAttacking")
    public void onAttacking(Entity target, CallbackInfo ci) {
        int fervorlevel = EnchantmentHelper.getLevel(ModEnchantments.FERVOR_OF_BATTLE, this.getMainHandStack());
        if(fervorlevel != 0) {
            int fervorcount = 0;
            StatusEffectInstance fervorInstance = this.getStatusEffect(ModStatusEffects.FERVOR_OF_BATTLE);
            if(fervorInstance != null) {
                fervorcount = fervorInstance.getAmplifier();
                if (fervorcount < fervorlevel * 2 - 1 && !this.handSwinging)
                    fervorcount++;
            }
            this.addStatusEffect(new StatusEffectInstance(ModStatusEffects.FERVOR_OF_BATTLE,20 + fervorlevel * 12, fervorcount));
        }
    }
}
