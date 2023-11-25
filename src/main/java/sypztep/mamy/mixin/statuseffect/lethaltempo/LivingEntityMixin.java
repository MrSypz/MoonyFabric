package sypztep.mamy.mixin.statuseffect.lethaltempo;

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
        int tempolevel = EnchantmentHelper.getLevel(ModEnchantments.LETHAL_TEMPO, this.getMainHandStack());
        if(tempolevel != 0) {
            int tempocount = 0;
            StatusEffectInstance fervorInstance = this.getStatusEffect(ModStatusEffects.LETHALTEMPO);
            if(fervorInstance != null) {
                tempocount = fervorInstance.getAmplifier();
                if (tempocount < tempolevel * 2 - 1 && !this.handSwinging)
                    tempocount++;
            }
            this.addStatusEffect(new StatusEffectInstance(ModStatusEffects.LETHALTEMPO,  20 + tempolevel * 12, tempocount));
        }
    }
}
