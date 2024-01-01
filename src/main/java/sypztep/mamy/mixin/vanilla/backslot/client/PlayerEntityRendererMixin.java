package sypztep.mamy.mixin.vanilla.backslot.client;

import net.minecraft.client.render.entity.EntityRendererFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.spongepowered.asm.mixin.injection.At;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import sypztep.mamy.client.render.feature.BackWeaponFeatureRenderer;
import sypztep.mamy.client.render.feature.DeathScytheFeatureRenderer;

@Environment(EnvType.CLIENT)
@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    public PlayerEntityRendererMixin(EntityRendererFactory.Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    @Inject(method = "<init>", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void initMixin(EntityRendererFactory.Context ctx, boolean slim, CallbackInfo info) {
//        this.addFeature(new BackToolFeatureRenderer(this, ctx.getHeldItemRenderer()));
//        this.addFeature(new BeltSlotFeatureRenderer(this, ctx.getHeldItemRenderer()));
        this.addFeature(new BackWeaponFeatureRenderer(this,ctx.getHeldItemRenderer()));
        this.addFeature(new DeathScytheFeatureRenderer(this,ctx.getHeldItemRenderer()));
    }

}