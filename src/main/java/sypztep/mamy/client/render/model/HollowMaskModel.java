package sypztep.mamy.client.render.model;

import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;
import sypztep.mamy.common.Item.MamyMaskItem;

import static sypztep.mamy.common.MamyMod.MODID;

public class HollowMaskModel extends GeoModel<MamyMaskItem> {
    @Override
    public Identifier getModelResource(MamyMaskItem animatable) {
        return new Identifier(MODID,"geo/hollowmask.geo.json");
    }
    @Override
    public Identifier getTextureResource(MamyMaskItem animatable) {
        return new Identifier(MODID,"textures/armor/hollow_mask_armor.png");
    }
    @Override
    public Identifier getAnimationResource(MamyMaskItem animatable) {
        return new Identifier(MODID,"animations/hollowmask.animation.json");
    }
}
