package sypztep.mamy.client.render.entity;

import software.bernie.geckolib.renderer.GeoArmorRenderer;
import sypztep.mamy.client.render.model.VastoMaskModel;
import sypztep.mamy.common.Item.MamyMaskItem;

public class VastomaskRenderer extends GeoArmorRenderer<MamyMaskItem> {
    public VastomaskRenderer() {
        super(new VastoMaskModel());
    }
}
