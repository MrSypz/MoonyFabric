package sypztep.mamy.client.render.feature;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.util.math.RotationAxis;
import sypztep.mamy.common.component.entity.BackWeaponComponent;
import sypztep.mamy.common.init.ModItems;

public class DeathScytheFeatureRenderer extends HeldItemFeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
    private final HeldItemRenderer heldItemRenderer;

    public DeathScytheFeatureRenderer(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> context, HeldItemRenderer heldItemRenderer) {
        super(context, heldItemRenderer);
        this.heldItemRenderer = heldItemRenderer;
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, AbstractClientPlayerEntity livingEntity, float f, float g, float h, float j, float k, float l) {
        if (!BackWeaponComponent.isHoldingBackWeapon(livingEntity)) {
            ItemStack stack = BackWeaponComponent.getBackWeapon(livingEntity);
            if (!stack.isEmpty()) {
                matrixStack.push();
                if (stack.getItem() == ModItems.DEATH_SCYTHE) {
                    matrixStack.translate(0.0, 0.0, 0.275);
                    matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(0.0F));
                    matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));
                    matrixStack.scale(1.65F,1.65F,1.65F);
                    heldItemRenderer.renderItem(livingEntity, stack, ModelTransformationMode.FIXED, false, matrixStack, vertexConsumerProvider, i);
                } else
                if (stack.getItem() == ModItems.BLOODLUST) {
                    matrixStack.translate(-0.1, 0.25, 0.275);
                    matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(0.0F));
                    matrixStack.scale(2.0F,2.0F,1.4F);
                    heldItemRenderer.renderItem(livingEntity, stack, ModelTransformationMode.FIXED, false, matrixStack, vertexConsumerProvider, i);
                } else
                if (stack.getItem() instanceof TridentItem) {
                    matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(52.0F));
                    matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(40.0F));
                    matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-25.0F));
                    matrixStack.translate(-0.26D, 0.0D, 0.0D);
                    matrixStack.scale(1.0F, -1.0F, -1.0F);
                    heldItemRenderer.renderItem(livingEntity, stack, ModelTransformationMode.THIRD_PERSON_RIGHT_HAND, false, matrixStack, vertexConsumerProvider, i);
                }
                matrixStack.pop();
            }
        }
    }
}
