package sypztep.mamy.mixin.moditem.throwingstuff;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public class EnchantmentTargetMixin {
    @Unique
    @Final
    public EnchantmentTarget type;

    @Inject(method = "isAcceptableItem", at = @At(value = "RETURN"), cancellable = true)
    public void isAcceptableItem(ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue() && this.type == EnchantmentTarget.WEAPON) {
            cir.setReturnValue(true);
        }
    }
}
