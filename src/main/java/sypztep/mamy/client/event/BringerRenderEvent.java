package sypztep.mamy.client.event;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import sypztep.mamy.common.MamyMod;
import sypztep.mamy.common.component.entity.BringerComponent;
import sypztep.mamy.common.init.ModEntityComponents;

@Environment(EnvType.CLIENT)
public class BringerRenderEvent implements HudRenderCallback {

    private static final Identifier BRINGER_TEXTURE = MamyMod.id("textures/gui/bringer.png");

    @Override
    public void onHudRender(DrawContext drawContext, float tickDelta) {
        ModEntityComponents.BRINGER.maybeGet(MinecraftClient.getInstance().cameraEntity).ifPresent(bringerComponent -> {
            if (bringerComponent.hasDeathBringer() && BringerComponent.getDeathCooldown() > 0) {
                RenderSystem.enableBlend();
                drawContext.drawTexture(BRINGER_TEXTURE, (int) (drawContext.getScaledWindowWidth() / 2F) - 18, (int) (drawContext.getScaledWindowHeight() / 2F) + 66, 0, 11, 37, 11, 37, 22);
                if (BringerComponent.getDeathCooldown() < BringerComponent.getLastdeathCooldown()) {
                    drawContext.drawTexture(BRINGER_TEXTURE, (int) (drawContext.getScaledWindowWidth() / 2F) - 18, (int) (drawContext.getScaledWindowHeight() / 2F) + 66, 0, 0, (int) (37 - (BringerComponent.getDeathCooldown() / (float) BringerComponent.getLastdeathCooldown()) * 37), 11, 37, 22);
                }
                RenderSystem.disableBlend();
            }
        });
    }
}
