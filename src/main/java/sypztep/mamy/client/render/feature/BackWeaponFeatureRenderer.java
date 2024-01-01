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
import net.minecraft.util.ActionResult;
import sypztep.mamy.common.component.entity.BackWeaponComponent;
import sypztep.mamy.common.interfaces.WeaponSlotCallback;

public class BackWeaponFeatureRenderer extends HeldItemFeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
    private final HeldItemRenderer heldItemRenderer;

    public BackWeaponFeatureRenderer(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> context, HeldItemRenderer heldItemRenderer) {
        super(context, heldItemRenderer);
        this.heldItemRenderer = heldItemRenderer;
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, AbstractClientPlayerEntity livingEntity, float f, float g, float h, float j, float k, float l) {
        if (!BackWeaponComponent.isHoldingBackWeapon(livingEntity)) {
            ItemStack stack = BackWeaponComponent.getBackWeapon(livingEntity);
            if (!stack.isEmpty()) {
                ActionResult result = WeaponSlotCallback.EVENT.invoker().interact(livingEntity, stack);
                matrixStack.push();
                if (result != ActionResult.FAIL) {
                    matrixStack.translate(0.0, 0.35, 0.25);
                    heldItemRenderer.renderItem(livingEntity, stack, ModelTransformationMode.FIXED, false, matrixStack, vertexConsumerProvider,i);
                }
                matrixStack.pop();
            }
        }
    }
}
