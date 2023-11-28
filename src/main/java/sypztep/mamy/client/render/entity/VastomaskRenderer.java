package sypztep.mamy.client.render.entity;

import software.bernie.geckolib.renderer.GeoArmorRenderer;
import sypztep.mamy.client.render.model.VastoMaskModel;
import sypztep.mamy.common.Item.MamyMaskFuncItem;

public class VastomaskRenderer extends GeoArmorRenderer<MamyMaskFuncItem> {
    public VastomaskRenderer() {
        super(new VastoMaskModel());
    }
}
