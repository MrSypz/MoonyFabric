package sypztep.mamy.client.render.entity;

import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import sypztep.mamy.client.render.model.HollowMaskModel;
import sypztep.mamy.common.Item.MamyMaskItem;
import sypztep.mamy.common.MamyMod;

public class HollowmaskRenderer extends GeoArmorRenderer<MamyMaskItem> {
    public HollowmaskRenderer() {
        super(new HollowMaskModel());
    }
}
