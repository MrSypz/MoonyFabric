package sypztep.mamy.feature.render.entity.model.pet;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import sypztep.mamy.feature.MamyFeature;
import sypztep.mamy.feature.render.entity.GlowyRenderLayer;

public class SquareModel extends Model {
    public static final EntityModelLayer MODEL_LAYER = new EntityModelLayer(new Identifier(MamyFeature.MODID, "square"), "main");
    private final ModelPart square;
    public SquareModel(ModelPart root) {
        super(GlowyRenderLayer::get);
        this.square = root.getChild("square");
    }
    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild("square", ModelPartBuilder.create()
                .uv(32, 0)
                .cuboid(-4.0f, -4.0f, -4.0f, 8.0f, 8.0f, 8.0f),
                ModelTransform.NONE);
        modelPartData.getChild("square").addChild("frame",ModelPartBuilder.create()
                .uv(0, 0)
                .cuboid(-4.0f, -4.0f, -4.0f, 8.0f, 8.0f, 8.0f),
                ModelTransform.rotation(0.0F, 0.0F, 0.7854F));
        return TexturedModelData.of(modelData, 64, 32);
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        square.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
