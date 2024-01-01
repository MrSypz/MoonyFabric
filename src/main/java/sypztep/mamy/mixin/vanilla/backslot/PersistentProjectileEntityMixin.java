package sypztep.mamy.mixin.vanilla.backslot;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sypztep.mamy.common.interfaces.WeaponSlotHolder;

@Mixin({PersistentProjectileEntity.class})

public abstract class PersistentProjectileEntityMixin extends ProjectileEntityMixin {
    @Shadow public PersistentProjectileEntity.PickupPermission pickupType;

    @Shadow protected abstract ItemStack asItemStack();

    @Inject(
            method = {"tryPickup"},
            at = {@At("HEAD")},
            cancellable = true
    )
    private void mamy$pickupSlot(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        if (this.mamy$getOwnedSlot() != -1) {
            if (this.pickupType == PersistentProjectileEntity.PickupPermission.ALLOWED) {
                PlayerInventory inventory = player.getInventory();
                if (inventory instanceof WeaponSlotHolder holder) {
                    if (holder.mamy$tryInsertIntoSlot(this.mamy$getOwnedSlot(), this.asItemStack())) {
                        cir.setReturnValue(true);
                    }
                }
            }
        }
    }
}
