package sypztep.mamy.client.render.entity;

import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

public class MamyItemRenderer implements BuiltinItemRendererRegistry.DynamicItemRenderer, SimpleSynchronousResourceReloadListener {
    private final Identifier scytheId;
    private ItemRenderer itemRenderer;
    private BakedModel inventoryScytheModel;
    private BakedModel worldScytheModel;
    public MamyItemRenderer (Identifier scytheId) {
        this.scytheId = scytheId;
    }
    @Override
    public void render(ItemStack stack, ModelTransformationMode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.pop();
        matrices.push();
        if (mode != ModelTransformationMode.FIRST_PERSON_LEFT_HAND && mode != ModelTransformationMode.FIRST_PERSON_RIGHT_HAND && mode != ModelTransformationMode.THIRD_PERSON_LEFT_HAND && mode != ModelTransformationMode.THIRD_PERSON_RIGHT_HAND) {
            this.itemRenderer.renderItem(stack, mode, false, matrices, vertexConsumers, light, overlay, this.inventoryScytheModel);
        } else {
            boolean leftHanded;
            switch (mode) {
                case FIRST_PERSON_LEFT_HAND:
                case THIRD_PERSON_LEFT_HAND:
                    leftHanded = true;
                    break;
                default:
                    leftHanded = false;
            }

            this.itemRenderer.renderItem(stack, mode, leftHanded, matrices, vertexConsumers, light, overlay, this.worldScytheModel);
        }
    }

    @Override
    public Identifier getFabricId() {
        return null;
    }

    @Override
    public void reload(ResourceManager manager) {
        MinecraftClient client = MinecraftClient.getInstance();
        this.itemRenderer = client.getItemRenderer();
        this.inventoryScytheModel = client.getBakedModelManager().getModel(new ModelIdentifier(this.scytheId.getNamespace(),this.scytheId.getPath() + "_gui", "inventory"));
        this.worldScytheModel = client.getBakedModelManager().getModel(new ModelIdentifier(this.scytheId.getNamespace(),this.scytheId.getPath() + "_handheld", "inventory"));
    }
}
