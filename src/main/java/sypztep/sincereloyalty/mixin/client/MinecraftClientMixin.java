package sypztep.sincereloyalty.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sypztep.sincereloyalty.SincereLoyaltyClient;

@Mixin(MinecraftClient.class) @Environment(EnvType.CLIENT)
public abstract class MinecraftClientMixin {
    @Shadow
    private int itemUseCooldown;

    @Inject(method = "doItemUse", at = @At("TAIL"))
    private void onUseFail(CallbackInfo ci) {
        SincereLoyaltyClient.INSTANCE.setFailedUse(this.itemUseCooldown);
    }
}
