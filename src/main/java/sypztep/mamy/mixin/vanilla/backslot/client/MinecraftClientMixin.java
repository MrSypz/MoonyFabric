package sypztep.mamy.mixin.vanilla.backslot.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sypztep.mamy.common.component.entity.BackWeaponComponent;

@Mixin({MinecraftClient.class})
public class MinecraftClientMixin {

    @Shadow @Nullable public ClientPlayerEntity player;

    public MinecraftClientMixin() {
    }

    @Inject(
            method = {"handleInputEvents"},
            at = {@At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/network/ClientPlayerEntity;getInventory()Lnet/minecraft/entity/player/PlayerInventory;"
            )}
    )
    private void arsenal$inputSlot(CallbackInfo ci) {
        if (this.player != null) {
            BackWeaponComponent.setHoldingBackWeapon(this.player, false);
        }

    }

    @Inject(
            method = {"handleInputEvents"},
            at = {@At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/network/ClientPlayerEntity;isSpectator()Z",
                    ordinal = 1
            )}
    )
    private void arsenal$swapStop(CallbackInfo ci) {
        if (this.player != null) {
            BackWeaponComponent.setHoldingBackWeapon(this.player, false);
        }

    }

    @Inject(
            method = {"doItemPick"},
            at = {@At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerInventory;isValidHotbarIndex(I)Z"
            )}
    )
    private void arsenal$pickSlot(CallbackInfo ci) {
        if (this.player != null) {
            BackWeaponComponent.setHoldingBackWeapon(this.player, false);
        }

    }
}
