package sypztep.mamy.common.init;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import sypztep.mamy.common.MamyMod;
import sypztep.mamy.common.entity.projectile.*;

public class ModEntityTypes {
    public static EntityType<ElderTridentEntity> ELDER_TRIDENT;
    public static EntityType<ElderTridentEntity> GUARDIAN_TRIDENT;
    public static EntityType<BloodLustEntity> BLOOD_LUST;
    public static EntityType<PitchforkEntity> PITCHFORK;
    public static EntityType<HellforkEntity> HELLFORK;
    public static EntityType<HellforkEntity> SOULFORK;
    public static EntityType<HomaEntity> HOMA;
    public static EntityType<HomaSoulEntity> HOMA_SOUL;
    public static EntityType<OrbitalEntity> ORBITAL;

    public static void init() {
        BLOOD_LUST = registerEntity("bloodlust", createEntityTypeSlash(BloodLustEntity::new));
        PITCHFORK = registerEntity("pitchfork", createEntityType(PitchforkEntity::new));
        ELDER_TRIDENT = registerEntity("elder_trident", createEntityType(ElderTridentEntity::new));
        GUARDIAN_TRIDENT = registerEntity("guardian_trident", createEntityType(GuardianTridentEntity::new));
        HOMA = registerEntity("homa", createEntityType(HomaEntity::new));
        HOMA_SOUL = registerEntity("homasoul", createEntityType(HomaSoulEntity::new));
        HELLFORK = registerEntity("hellfork", createEntityType(HellforkEntity::new));
        SOULFORK = registerEntity("soulfork", createEntityType(HellforkEntity::new));
        ORBITAL = registerEntity("orbital", createNoHitbock(OrbitalEntity::new));
    }

    private static <T extends Entity> EntityType<T> registerEntity(String name, EntityType<T> entityType) {
        return Registry.register(Registries.ENTITY_TYPE, MamyMod.id(name), entityType);
    }
    private static <T extends Entity> EntityType<T> createEntityType(EntityType.EntityFactory<T> factory) {
        return FabricEntityTypeBuilder.create(SpawnGroup.MISC, factory).dimensions(EntityDimensions.changing(0.5f, 0.5f)).trackRangeBlocks(126).trackedUpdateRate(20).build();
    }
    private static <T extends Entity> EntityType<T> createNoHitbock(EntityType.EntityFactory<T> factory) {
        return FabricEntityTypeBuilder.create(SpawnGroup.MISC, factory).dimensions(EntityDimensions.changing(0.1f, 0.1f)).trackRangeBlocks(512).trackedUpdateRate(4).build();
    }
    private static <T extends Entity> EntityType<T> createEntityTypeSlash(EntityType.EntityFactory<T> factory) {
        return FabricEntityTypeBuilder.create(SpawnGroup.MISC, factory).dimensions(EntityDimensions.changing(5.0F, 0.2F)).build();
    }
}
