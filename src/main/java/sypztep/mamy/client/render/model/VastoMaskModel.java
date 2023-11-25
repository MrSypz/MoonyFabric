package sypztep.mamy.client.render.model;

import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;
import sypztep.mamy.common.Item.MamyMaskItem;
import sypztep.mamy.common.MamyMod;

import static sypztep.mamy.common.MamyMod.MODID;

public class VastoMaskModel extends GeoModel<MamyMaskItem>{
    @Override
    public Identifier getModelResource(MamyMaskItem animatable) {
        return new Identifier(MODID,"geo/vastomask.geo.json");
    }
    @Override
    public Identifier getTextureResource(MamyMaskItem animatable) {
        return MamyMod.id("textures/armor/vasto_mask_armor.png");
    }
    @Override
    public Identifier getAnimationResource(MamyMaskItem animatable) {
        return new Identifier(MODID,"animations/hollowmask.animation.json");
    }
}
