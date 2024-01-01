package sypztep.mamy.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.TridentEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import sypztep.mamy.common.entity.projectile.MamyTridentEntity;

@Environment(EnvType.CLIENT)
public class MamyTridentEntityRenderer extends EntityRenderer<MamyTridentEntity> {
    private final TridentEntityModel model;
    private final Identifier texture;

    public MamyTridentEntityRenderer(EntityRendererFactory.Context ctx, Identifier texture, EntityModelLayer modelLayer) {
        super(ctx);
        this.model = new TridentEntityModel(ctx.getPart(modelLayer));
        this.texture = texture;
    }

    public void render(MamyTridentEntity mamyTridentEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.lerp(g, mamyTridentEntity.prevYaw, mamyTridentEntity.getYaw()) - 90.0F));
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(MathHelper.lerp(g, mamyTridentEntity.prevPitch, mamyTridentEntity.getPitch()) + 90.0F));
        VertexConsumer vertexConsumer = ItemRenderer.getDirectItemGlintConsumer(vertexConsumerProvider, this.model.getLayer(this.getTexture(mamyTridentEntity)), false, false);
        this.model.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStack.pop();
        super.render(mamyTridentEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }
    @Override
    public Identifier getTexture(MamyTridentEntity entity) {
        return this.texture;
    }
}
