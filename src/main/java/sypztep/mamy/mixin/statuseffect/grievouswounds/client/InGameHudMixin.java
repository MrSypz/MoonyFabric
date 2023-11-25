package sypztep.mamy.mixin.statuseffect.grievous.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sypztep.mamy.common.MamyMod;
import sypztep.mamy.common.init.ModStatusEffects;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public class InGameHudMixin{
    @Unique
    private static final Identifier GRIEVOUS_WOUNDS_HEARTS = MamyMod.id("textures/gui/grievous_wounds_hearts.png");
    @Unique
    private static final Identifier BATRACHOTOXIN_HEARTS = MamyMod.id("textures/gui/batrachotoxin_hearts.png");
    @Unique
    private static final Identifier TORPOR_HEARTS = MamyMod.id("textures/gui/torpor_hearts.png");
    @Unique
    private static final Identifier NUMBNESS_HEARTS = MamyMod.id("textures/gui/numbness_hearts.png");

    @Inject(method = "drawHeart", at = @At("HEAD"), cancellable = true)
    private void mamy$drawCustomHeart(DrawContext context, InGameHud.HeartType type, int x, int y, int v, boolean blinking, boolean halfHeart, CallbackInfo ci) {
        if (!blinking && type == InGameHud.HeartType.NORMAL && MinecraftClient.getInstance().cameraEntity instanceof PlayerEntity player && (player.hasStatusEffect(ModStatusEffects.GRIEVOUSWOUNDS) || player.hasStatusEffect(ModStatusEffects.BATRACHOTOXIN) || player.hasStatusEffect(ModStatusEffects.TORPOR) || player.hasStatusEffect(ModStatusEffects.NUMBNESS))) {
            if (player.hasStatusEffect(ModStatusEffects.GRIEVOUSWOUNDS)) {
                context.drawTexture(GRIEVOUS_WOUNDS_HEARTS,x,y,halfHeart ? 9 : 0,v,9,9);
                RenderSystem.setShaderTexture(0, GRIEVOUS_WOUNDS_HEARTS);
            }
            else if (player.hasStatusEffect(ModStatusEffects.TORPOR)) {
                context.drawTexture(TORPOR_HEARTS,x,y,halfHeart ? 9 : 0,v,9,9);
                RenderSystem.setShaderTexture(0, TORPOR_HEARTS);
            }
            else if (player.hasStatusEffect(ModStatusEffects.BATRACHOTOXIN)) {
                context.drawTexture(BATRACHOTOXIN_HEARTS,x,y,halfHeart ? 9 : 0,v,9,9);
                RenderSystem.setShaderTexture(0, BATRACHOTOXIN_HEARTS);
            }
            else if (player.hasStatusEffect(ModStatusEffects.NUMBNESS)) {
                context.drawTexture(NUMBNESS_HEARTS,x,y,halfHeart ? 9 : 0,v,9,9);
                RenderSystem.setShaderTexture(0, NUMBNESS_HEARTS);
            }
            ci.cancel();
        }
    }
}