package sypztep.trueloyalty.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sypztep.trueloyalty.TridentRecaller;

@Mixin(PlayerEntityRenderer.class) @Environment(EnvType.CLIENT)
public abstract class PlayerEntityRendererMixin {

    @Inject(method = "getArmPose", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/entity/model/BipedEntityModel$ArmPose;EMPTY:Lnet/minecraft/client/render/entity/model/BipedEntityModel$ArmPose;"), cancellable = true)
    private static void getArmPose(AbstractClientPlayerEntity player, Hand hand, CallbackInfoReturnable<BipedEntityModel.ArmPose> cir) {
        if (((TridentRecaller) player).getCurrentRecallStatus() != TridentRecaller.RecallStatus.NONE) {
            cir.setReturnValue(BipedEntityModel.ArmPose.CROSSBOW_HOLD);
        }
    }
}
