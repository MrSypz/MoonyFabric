package sypztep.mamy.mixin.enchantment.greedyang;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityGroup;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sypztep.mamy.common.init.ModEnchantments;
import sypztep.mamy.common.util.EnchantmentUtil;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
	@Inject(method = "getAttackDamage", at = @At("HEAD"), cancellable = true)
	private static void mamy$greedyang(ItemStack stack, EntityGroup group, CallbackInfoReturnable<Float> cir) {
		if (EnchantmentUtil.hasEnchantment(ModEnchantments.GREEDYANG, stack)) {
			cir.setReturnValue(1F);
		}
	}
}
