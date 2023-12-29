package sypztep.sincereloyalty.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sypztep.mamy.common.MamyMod;
import sypztep.sincereloyalty.LoyalTrident;

import java.util.List;

@Mixin(ItemStack.class) @Environment(EnvType.CLIENT)
public abstract class ItemStackMixin {

    @Nullable
    @Unique
    private static String mamy$trueOwnerName;
    @Unique
    private static boolean mamy$riptide;

    // inject into the lambda in appendEnchantments
    @Dynamic("Lambda method")
    @Inject(method = "method_17869", at = @At("RETURN"))
    private static void editTooltip(List<Text> lines, NbtCompound enchantmentNbt, Enchantment enchantment, CallbackInfo info) {
        if (enchantment == Enchantments.LOYALTY && mamy$trueOwnerName != null) {
            if (!lines.isEmpty()) {
                if (mamy$riptide) {
                    // If there is riptide, we present as if there was only one level possible
                    lines.set(lines.size() - 1, Text.translatable(enchantment.getTranslationKey()).formatted(Formatting.GRAY));
                }

                MutableText line = (MutableText) lines.get(lines.size() - 1);

                line.append(Text.literal(" ")).append(Text.translatable(MamyMod.MODID+ ":" +"tooltip.owned_by", mamy$trueOwnerName).formatted(Formatting.DARK_GRAY));
            }
            mamy$trueOwnerName = null;
        }
    }

    @Shadow
    public abstract NbtCompound getSubNbt(String key);

    @Inject(method = "getTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;appendEnchantments(Ljava/util/List;Lnet/minecraft/nbt/NbtList;)V"))
    private void captureThis(PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> cir) {
        NbtCompound loyaltyNbt = this.getSubNbt(LoyalTrident.MOD_NBT_KEY);
        if (loyaltyNbt != null && loyaltyNbt.contains(LoyalTrident.OWNER_NAME_NBT_KEY)) {
            mamy$trueOwnerName = loyaltyNbt.getString(LoyalTrident.OWNER_NAME_NBT_KEY);
            mamy$riptide = EnchantmentHelper.getRiptide((ItemStack) (Object) this) > 0;
        }
    }
}
