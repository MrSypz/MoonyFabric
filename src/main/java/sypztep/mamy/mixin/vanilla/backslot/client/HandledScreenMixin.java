package sypztep.mamy.mixin.vanilla.backslot.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sypztep.mamy.client.MamyModClient;
import sypztep.mamy.common.MamyMod;

@Mixin({HandledScreen.class})
public class HandledScreenMixin <T extends ScreenHandler>{

    @Shadow @Nullable protected Slot focusedSlot;

    @Shadow @Final protected T handler;

    @Inject(
            method = {"onMouseClick(I)V"},
            at = {@At("HEAD")},
            cancellable = true
    )
    private void arsenal$onMouseClick(int button, CallbackInfo ci) {
        if (this.focusedSlot != null && this.handler.getCursorStack().isEmpty() && MamyModClient.SWAP_KEYBINDING.matchesMouse(button)) {
            int slotId = this.focusedSlot.id;
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeInt(slotId);
            ClientPlayNetworking.send(MamyMod.swapInventoryPacketId, buf);
            ci.cancel();
        }

    }

    @Inject(
            method = {"handleHotbarKeyPressed"},
            at = {@At("HEAD")},
            cancellable = true
    )
    private void arsenal$handleHotbarKeyPressed(int keyCode, int scanCode, CallbackInfoReturnable<Boolean> cir) {
        if (this.focusedSlot != null && this.handler.getCursorStack().isEmpty() && MamyModClient.SWAP_KEYBINDING.matchesKey(keyCode, scanCode)) {
            int slotId = this.focusedSlot.id;
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeInt(slotId);
            ClientPlayNetworking.send(MamyMod.swapInventoryPacketId, buf);
            cir.setReturnValue(true);
        }

    }
}
