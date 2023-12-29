package sypztep.sincereloyalty.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import sypztep.sincereloyalty.LoyalTrident;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {
    @ModifyVariable(method = "onCreativeInventoryAction", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/network/packet/c2s/play/CreativeInventoryActionC2SPacket;getItemStack()Lnet/minecraft/item/ItemStack;"))
    private ItemStack removeTridentUuid(ItemStack copiedStack) {
        NbtCompound NbtCompound = copiedStack.getSubNbt(LoyalTrident.MOD_NBT_KEY);
        if (NbtCompound != null) {
            NbtCompound.remove(LoyalTrident.TRIDENT_UUID_NBT_KEY);  // prevent stupid copies of the exact same trident
        }
        return copiedStack;
    }
}
