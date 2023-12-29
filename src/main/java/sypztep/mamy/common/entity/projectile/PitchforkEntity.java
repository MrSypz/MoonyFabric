package sypztep.mamy.common.entity.projectile;

import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class PitchforkEntity extends MamyTridentEntity {
    public PitchforkEntity(EntityType<? extends PitchforkEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected float getDragInWater() {
        return 0.6f;
    }
}
