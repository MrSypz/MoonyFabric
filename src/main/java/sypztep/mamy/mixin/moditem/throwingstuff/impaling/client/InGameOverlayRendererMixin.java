package sypztep.mamy.mixin.moditem.throwingstuff.impaling.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sypztep.mamy.common.Item.HomaItem;

@Mixin(InGameOverlayRenderer.class)
public class InGameOverlayRendererMixin {
    @Inject(method = "renderFireOverlay", at = @At("HEAD"), cancellable = true)
    private static void renderFireOverlay(MinecraftClient client, MatrixStack matrices, CallbackInfo ci) {
        if (MinecraftClient.getInstance().player.isUsingRiptide() && (MinecraftClient.getInstance().player.getMainHandStack().getItem() instanceof HomaItem || MinecraftClient.getInstance().player.getOffHandStack().getItem() instanceof HomaItem)) {
            ci.cancel();
        }
    }
}
