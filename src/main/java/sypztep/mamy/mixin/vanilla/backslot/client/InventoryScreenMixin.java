package sypztep.mamy.mixin.vanilla.backslot.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.recipebook.RecipeBookProvider;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends AbstractInventoryScreen<PlayerScreenHandler> implements RecipeBookProvider {

    public InventoryScreenMixin(PlayerScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
        super(screenHandler, playerInventory, text);
    }

    @Inject(method = "drawBackground", at = @At(value = "RETURN"))
    public void drawBackgroundMixin(DrawContext context, float delta, int mouseX, int mouseY, CallbackInfo info) {
//        PlayerEntity player = MinecraftClient.getInstance().player;
//        if (player != null) {
//            PlayerCosmeticData cosmeticData = MamyFeature.getCosmeticData(player);
//            if (cosmeticData != null) {
//                int i = this.x + 76 + 56;
//                int j = this.y + 43 + 18;
//                context.drawTexture(BACKGROUND_TEXTURE, i, j, 76, 61, 18, 18);
//                context.drawTexture(BACKGROUND_TEXTURE, i, j, 76, 61, 18, 18);
//            }
//        }
    }
}