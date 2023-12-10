package sypztep.mamy.mixin.enchantment.hollowcurse;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sypztep.mamy.common.init.ModEnchantments;
import sypztep.mamy.common.util.EnchantmentUtil;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
	@Inject(method = "hasVanishingCurse", at = @At("HEAD"), cancellable = true)
	private static void mamy$hollowvanish(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		if (EnchantmentUtil.hasEnchantment(ModEnchantments.HOLLOW_CURSE, stack)) {
			cir.setReturnValue(true);
		}
	}

	@Inject(method = "hasBindingCurse", at = @At("HEAD"), cancellable = true)
	private static void mamy$hollowbind(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		if (EnchantmentUtil.hasEnchantment(ModEnchantments.HOLLOW_CURSE, stack)) {
			cir.setReturnValue(true);
		}
	}
}
