package sypztep.mamy.client.render.entity;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import sypztep.mamy.client.render.model.HomaEntityModel;
import sypztep.mamy.common.entity.projectile.HomaEntity;

import static sypztep.mamy.common.MamyMod.MODID;

public class HomaEntityRenderer extends GeoEntityRenderer<HomaEntity> {
    public HomaEntityRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new HomaEntityModel());
    }
    @Override
    public Identifier getTextureLocation(HomaEntity animatable) {
        return new Identifier(MODID, "textures/item/homa_handheld.png");
    }

    @Override
    public void render(HomaEntity entity, float entityYaw, float partialTick, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    public void preRender(MatrixStack poseStack, HomaEntity animatable, BakedGeoModel model, VertexConsumerProvider bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.lerp(partialTick, animatable.prevYaw, animatable.getYaw()) - 90.0f));
        poseStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(MathHelper.lerp(partialTick, animatable.prevPitch, animatable.getPitch()) + 90.0f));
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
