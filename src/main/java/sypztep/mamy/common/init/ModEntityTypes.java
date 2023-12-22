package sypztep.mamy.common.init;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import sypztep.mamy.common.MamyMod;
import sypztep.mamy.common.entity.projectile.BloodLustEntity;

public class ModEntityTypes {
    public static EntityType<BloodLustEntity> BLOOD_LUST;
    public static void init() {
        BLOOD_LUST = registerEntity("bloodlust", createEntityTypeSlash(BloodLustEntity::new));
    }

    private static <T extends Entity> EntityType<T> registerEntity(String id, EntityType<T> entityType) {
        return Registry.register(Registries.ENTITY_TYPE, MamyMod.MODID + ":" + id, entityType);
    }
    private static <T extends Entity> EntityType<T> createEntityType(EntityType.EntityFactory<T> factory) {
        return FabricEntityTypeBuilder.create(SpawnGroup.MISC, factory).dimensions(EntityDimensions.changing(0.5f, 0.5f)).trackRangeBlocks(128).build();
    }
    private static <T extends Entity> EntityType<T> createEntityTypeSlash(EntityType.EntityFactory<T> factory) {
        return FabricEntityTypeBuilder.create(SpawnGroup.MISC, factory).dimensions(EntityDimensions.changing(5.0F, 0.2F)).build();
    }
}
