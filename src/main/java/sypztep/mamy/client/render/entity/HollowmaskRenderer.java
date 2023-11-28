package sypztep.mamy.client.render.entity;

import software.bernie.geckolib.renderer.GeoArmorRenderer;
import sypztep.mamy.client.render.model.HollowMaskModel;
import sypztep.mamy.common.Item.MamyMaskFuncItem;

public class HollowmaskRenderer extends GeoArmorRenderer<MamyMaskFuncItem> {
    public HollowmaskRenderer() {
        super(new HollowMaskModel());
    }
}
