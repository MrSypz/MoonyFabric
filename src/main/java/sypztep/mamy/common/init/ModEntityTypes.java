package sypztep.mamy.common.init;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBiomeTags;
import net.minecraft.entity.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.Heightmap;
import sypztep.mamy.common.MamyMod;
import sypztep.mamy.common.entity.mob.PoisonDartFrogEntity;
import sypztep.mamy.common.entity.projectile.PoisonDartEntity;

public class ModEntityTypes {
    public static EntityType<PoisonDartFrogEntity> POISON_DART_FROG;
    public static EntityType<PoisonDartEntity> POISON_DART;


    public static void init(){
        POISON_DART_FROG = registerEntity("poison_dart_frog", FabricEntityTypeBuilder.createMob().entityFactory(PoisonDartFrogEntity::new).spawnGroup(SpawnGroup.CREATURE).dimensions(EntityDimensions.changing(0.5F, 0.4F)).spawnRestriction(SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING, PoisonDartFrogEntity::canMobSpawn).build());
        POISON_DART = registerEntity("poison_dart", createEntityType(PoisonDartEntity::new));
        FabricDefaultAttributeRegistry.register(ModEntityTypes.POISON_DART_FROG, PoisonDartFrogEntity.createPoisonDartFrogAttributes());
        BiomeModifications.addSpawn(
                biome -> biome.hasTag(ConventionalBiomeTags.JUNGLE),
                SpawnGroup.CREATURE, ModEntityTypes.POISON_DART_FROG, 50, 2, 5
        );
    }
    private static <T extends Entity> EntityType<T> registerEntity(String id, EntityType<T> entityType) {
        return Registry.register(Registries.ENTITY_TYPE, MamyMod.MODID + ":" + id, entityType);
    }
    private static <T extends Entity> EntityType<T> createEntityType(EntityType.EntityFactory<T> factory) {
        return FabricEntityTypeBuilder.create(SpawnGroup.MISC, factory).dimensions(EntityDimensions.changing(0.5f, 0.5f)).trackRangeBlocks(12).trackedUpdateRate(20).build();
    }
}
