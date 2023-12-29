package sypztep.sincereloyalty.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sypztep.mamy.common.MamyMod;
import sypztep.mamy.common.init.ModTags;
import sypztep.sincereloyalty.LoyalTrident;

import java.util.Map;

@Mixin(AnvilScreenHandler.class)
public abstract class AnvilScreenHandlerMixin extends ForgingScreenHandler {
    @Unique
    private boolean mamy$checkingRiptideCompat;

    public AnvilScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(type, syncId, playerInventory, context);
    }

    @ModifyVariable(
            method = "updateResult",
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Ljava/util/Set;iterator()Ljava/util/Iterator;", ordinal = 1),
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/Enchantment;canCombine(Lnet/minecraft/enchantment/Enchantment;)Z")
            ),
            at = @At(value = "INVOKE", target = "Ljava/util/Iterator;next()Ljava/lang/Object;"),
            ordinal = 0
    )
    private Enchantment captureSecondStackEnchant(Enchantment checkedEnchantment) {
        mamy$checkingRiptideCompat = checkedEnchantment == Enchantments.RIPTIDE;
        return checkedEnchantment;
    }
    @Inject(method = "canTakeOutput", at = @At("RETURN"), cancellable = true)
    private void canTakeResult(PlayerEntity playerEntity, boolean resultNonEmpty, CallbackInfoReturnable<Boolean> cir) {
        if (resultNonEmpty && !cir.getReturnValueZ()) {
            ItemStack item = this.input.getStack(0);
            ItemStack upgradeItem = this.input.getStack(1);
            cir.setReturnValue(item.isIn(ModTags.Items.TRIDENTS) && upgradeItem.isIn(ModTags.Items.LOYALTY_CATALYSTS));
        }
    }

    @ModifyVariable(
            method = "updateResult",
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Ljava/util/Set;iterator()Ljava/util/Iterator;", ordinal = 1),
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/Enchantment;canCombine(Lnet/minecraft/enchantment/Enchantment;)Z")
            ),
            at = @At("STORE"),
            ordinal = 1
    )
    private Enchantment allowRiptideLoyalty(Enchantment baseEnchant) {
        if (baseEnchant == Enchantments.LOYALTY && mamy$checkingRiptideCompat) {
            if (LoyalTrident.hasTrueOwner(this.input.getStack(0))) {
                // If enchantment1 == enchantment2, they are automatically considered compatible
                return Enchantments.RIPTIDE;
            }
        }
        return baseEnchant;
    }
    @ModifyArg(
            method = "updateResult",
            slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/item/ItemStack;EMPTY:Lnet/minecraft/item/ItemStack;")),
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/inventory/CraftingResultInventory;setStack(ILnet/minecraft/item/ItemStack;)V"
            )
    )
    private ItemStack updateResult(ItemStack initialResult) {
        if (initialResult.isEmpty()) {
            ItemStack item = this.input.getStack(0);
            ItemStack upgradeItem = this.input.getStack(1);
            if (item.isIn(ModTags.Items.TRIDENTS) && upgradeItem.isIn(ModTags.Items.LOYALTY_CATALYSTS)) {
                Map<Enchantment, Integer> enchantments = EnchantmentHelper.get(item);
                if (!MamyMod.isEnhancementLoaded) {
                    if (enchantments.getOrDefault(Enchantments.LOYALTY, 0) == Enchantments.LOYALTY.getMaxLevel()) {
                        ItemStack result = item.copy();
                        // we can mutate the map as it is recreated with every call to getEnchantments
                        enchantments.put(Enchantments.LOYALTY, Enchantments.LOYALTY.getMaxLevel() + 1);
                        EnchantmentHelper.set(enchantments, result);
                        NbtCompound loyaltyData = result.getOrCreateSubNbt(LoyalTrident.MOD_NBT_KEY);
                        loyaltyData.putUuid(LoyalTrident.TRIDENT_OWNER_NBT_KEY, this.player.getUuid());
                        loyaltyData.putString(LoyalTrident.OWNER_NAME_NBT_KEY, this.player.getEntityName());
                        return result;
                    }
                } else {
                    if (enchantments.getOrDefault(Enchantments.LOYALTY, 0) == Enchantments.LOYALTY.getMinLevel()) {
                        ItemStack result = item.copy();
                        // we can mutate the map as it is recreated with every call to getEnchantments
                        enchantments.put(Enchantments.LOYALTY, Enchantments.LOYALTY.getMaxLevel() + 1);
                        EnchantmentHelper.set(enchantments, result);
                        NbtCompound loyaltyData = result.getOrCreateSubNbt(LoyalTrident.MOD_NBT_KEY);
                        loyaltyData.putUuid(LoyalTrident.TRIDENT_OWNER_NBT_KEY, this.player.getUuid());
                        loyaltyData.putString(LoyalTrident.OWNER_NAME_NBT_KEY, this.player.getEntityName());
                        return result;
                    }
                }
            }
        }
        return initialResult;
    }
}
