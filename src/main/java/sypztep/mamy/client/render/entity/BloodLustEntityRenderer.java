package sypztep.mamy.client.render.entity;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.joml.Quaternionf;
import sypztep.mamy.common.MamyMod;
import sypztep.mamy.common.entity.projectile.BloodLustEntity;

public class BloodLustEntityRenderer<T extends BloodLustEntity> extends EntityRenderer<T> {
    public static final Identifier TEXTURE = MamyMod.id( "textures/entity/bloodlust.png");
    public BloodLustEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public Identifier getTexture(T entity) {
        return TEXTURE;
    }


    @Override
    public void render(T bloodScythe, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.multiply((new Quaternionf()).rotateY(MathHelper.lerp(g, bloodScythe.prevYaw, bloodScythe.getYaw()) - 90.0F));
        matrixStack.multiply((new Quaternionf()).rotateZ(MathHelper.lerp(g, bloodScythe.prevPitch, bloodScythe.getPitch())));
        matrixStack.multiply((new Quaternionf()).rotateX(45.0F));
        matrixStack.scale(0.4F, 0.4F, 0.4F);
        matrixStack.translate(-4.0, 0.0, 0.0);
        vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutout(this.getTexture(bloodScythe)));

        for(int u = 0; u < 4; ++u) {
            matrixStack.multiply((new Quaternionf()).rotateX(90.0F));
        }

        matrixStack.pop();
        super.render(bloodScythe, f, g, matrixStack, vertexConsumerProvider, i);
    }
}
