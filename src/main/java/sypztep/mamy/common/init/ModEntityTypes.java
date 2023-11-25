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

public class ModEntityTypes {

    public static void init(){
    }
    private static <T extends Entity> EntityType<T> registerEntity(String id, EntityType<T> entityType) {
        return Registry.register(Registries.ENTITY_TYPE, MamyMod.MODID + ":" + id, entityType);
    }
    private static <T extends Entity> EntityType<T> createEntityType(EntityType.EntityFactory<T> factory) {
        return FabricEntityTypeBuilder.create(SpawnGroup.MISC, factory).dimensions(EntityDimensions.changing(0.5f, 0.5f)).trackRangeBlocks(12).trackedUpdateRate(20).build();
    }
}
