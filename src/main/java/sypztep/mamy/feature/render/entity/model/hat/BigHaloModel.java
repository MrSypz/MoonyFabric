package sypztep.mamy.feature.render.entity.model.hat;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import sypztep.mamy.feature.MamyFeature;

public class BigHaloModel extends OverheadModel {
	public static final EntityModelLayer MODEL_LAYER = new EntityModelLayer(new Identifier(MamyFeature.MODID, "big_halo"), "main");

	public BigHaloModel(EntityRendererFactory.Context ctx) {
		super(ctx, MODEL_LAYER);
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData modelPartData1 = modelPartData.addChild("head", ModelPartBuilder.create().uv(0, 7).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(-4.0f)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		modelPartData1.addChild("big_halo",  ModelPartBuilder.create().uv(0, 0).cuboid(-25.0F, -32.0F, 13.0F, 34.0F, 34.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(7.5F, 8.0F, -8.0F));
		return TexturedModelData.of(modelData, 68, 64);
	}

	@Override
	public void render(MatrixStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		head.render(matrixStack, buffer, packedLight, packedOverlay);
	}
}
