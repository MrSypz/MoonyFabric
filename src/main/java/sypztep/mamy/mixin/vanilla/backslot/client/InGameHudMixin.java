package sypztep.mamy.mixin.vanilla.backslot.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sypztep.mamy.common.component.entity.BackWeaponComponent;

@Environment(EnvType.CLIENT)
@Mixin({InGameHud.class})
public abstract class InGameHudMixin {
    @Shadow protected abstract PlayerEntity getCameraPlayer();

    @Shadow private int scaledWidth;

    @Shadow @Final private static Identifier WIDGETS_TEXTURE;

    @Shadow private int scaledHeight;

    @Shadow protected abstract void renderHotbarItem(DrawContext context, int x, int y, float f, PlayerEntity player, ItemStack stack, int seed);

    @Inject(
            method = {"renderHotbar"},
            at = {@At("TAIL")}
    )
    private void arsenal$renderWeaponSlot(float tickDelta, DrawContext context, CallbackInfo ci) {
        PlayerEntity player = this.getCameraPlayer();
        if (player != null) {
            ItemStack stack = BackWeaponComponent.getBackWeapon(player);
            if (!stack.isEmpty()) {
                int i = this.scaledWidth / 2;
                int n;
                if (BackWeaponComponent.isHoldingBackWeapon(player)) {
                    context.drawTexture(WIDGETS_TEXTURE, i - 12, this.scaledHeight - 23 - 70, 0, 22, 24, 24);
                    RenderSystem.enableBlend();
                    context.drawTexture(WIDGETS_TEXTURE, i - 12 + 4, this.scaledHeight - 23 - 70 + 4, 27, 26, 16, 16);
                    RenderSystem.defaultBlendFunc();
//                    this.method_25304(j);
                    n = i - 90 + 80 + 2;
                    int p = this.scaledHeight - 19 - 70;
                    this.renderHotbarItem(context,n, p, tickDelta, player, stack, 1);
                    RenderSystem.disableBlend();
                } else {
                    Arm arm = player.getMainArm().getOpposite();
                    RenderSystem.enableBlend();
                    RenderSystem.defaultBlendFunc();
                    if (arm == Arm.RIGHT) {
                        context.drawTexture(WIDGETS_TEXTURE, i - 91 - 29, this.scaledHeight - 23, 24, 22, 29, 24);
                    } else {
                        context.drawTexture(WIDGETS_TEXTURE, i + 91, this.scaledHeight - 23, 53, 22, 29, 24);
                    }

                    n = this.scaledHeight - 16 - 3;
                    if (arm == Arm.RIGHT) {
                        this.renderHotbarItem(context,i - 91 - 26, n, tickDelta, player, stack, 0);
                    } else {
                        this.renderHotbarItem(context,i + 91 + 10, n, tickDelta, player, stack, 0);
                    }

                    RenderSystem.disableBlend();
                }
            }

        }
    }

    @WrapOperation(method = "renderCrosshair", at = @At(value = "INVOKE", ordinal = 1, target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V"))
    private void mamy$selection(DrawContext instance, Identifier texture, int x, int y, int u, int v, int width, int height, Operation<Void> operation) {
        if (this.getCameraPlayer() == null || !BackWeaponComponent.isHoldingBackWeapon(this.getCameraPlayer())) {
            operation.call(instance, texture, x, y, u, v, width, height);
        }
    }

//    @ModifyExpressionValue(
//            method = {"renderHeldItemTooltip"},
//            at = {@At(
//                    value = "INVOKE",
//                    target = "Lnet/minecraft/text/MutableText;formatted(Lnet/minecraft/util/Formatting;)Lnet/minecraft/text/MutableText;",
//                    ordinal = 0
//            )}
//    )
//    private MutableText arsenal$heldTooltipChangeItemNameColor(class_5250 mutableText) {
//        class_1792 var3 = this.field_2031.method_7909();
//        if (var3 instanceof CustomColorItem colorItem) {
//            return mutableText.method_10862(mutableText.method_10866().method_36139(colorItem.getNameColor()));
//        } else {
//            return mutableText;
//        }
//    }
}
