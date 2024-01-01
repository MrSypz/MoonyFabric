package sypztep.mamy.client.render.model;

import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;
import sypztep.mamy.common.entity.projectile.HomaSoulEntity;

import static sypztep.mamy.common.MamyMod.MODID;

public class HomaSoulEntityModel extends GeoModel<HomaSoulEntity> {
    @Override
    public Identifier getModelResource(HomaSoulEntity animatable) {
        return new Identifier(MODID,"geo/homa.geo.json");
    }

    @Override
    public Identifier getTextureResource(HomaSoulEntity animatable) {
        return new Identifier(MODID, "textures/item/homasoul_handheld.png");
    }

    @Override
    public Identifier getAnimationResource(HomaSoulEntity animatable) {
        return null;
    }

}
