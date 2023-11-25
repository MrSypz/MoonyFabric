package sypztep.mamy.feature.render.entity.model.pet;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import sypztep.mamy.feature.MamyFeature;
import sypztep.mamy.feature.render.entity.GlowyRenderLayer;

public class LanternModel extends Model {
    public static final EntityModelLayer MODEL_LAYER = new EntityModelLayer(new Identifier(MamyFeature.MODID, "lantern"), "main");
    private final ModelPart lantern;
    public LanternModel(ModelPart root) {
        super(GlowyRenderLayer::get);
        this.lantern = root.getChild("lantern");
    }
    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild("lantern", ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(-3.0F, -3.0F, -3.0F, 6.0F, 7.0F, 6.0F)
                        .uv(0, 13)
                        .cuboid(-2.0F, -5.0F, -2.0F, 4.0F, 2.0F, 4.0F)
                        .uv(16, 13)
                        .cuboid(-2.5F, -8.0F, 0.0F, 5.0F, 4.0F, 0.0F),
                ModelTransform.pivot(0.0F, 16.0F, 0.0F)
        );
        return TexturedModelData.of(modelData, 32, 32);
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        lantern.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
