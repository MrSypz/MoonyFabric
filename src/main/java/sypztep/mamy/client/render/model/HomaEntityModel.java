package sypztep.mamy.client.render.model;

import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;
import sypztep.mamy.common.entity.projectile.HomaEntity;

import static sypztep.mamy.common.MamyMod.MODID;

public class HomaEntityModel extends GeoModel<HomaEntity> {
    @Override
    public Identifier getModelResource(HomaEntity animatable) {
        return new Identifier(MODID,"geo/homa.geo.json");
    }

    @Override
    public Identifier getTextureResource(HomaEntity animatable) {
        return new Identifier(MODID, "textures/item/homa_handheld.png");
    }

    @Override
    public Identifier getAnimationResource(HomaEntity animatable) {
        return null;
    }

}
