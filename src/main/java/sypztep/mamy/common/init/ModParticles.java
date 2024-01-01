package sypztep.mamy.common.init;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModParticles {
    public static DefaultParticleType RED_SWEEP_ATTACK_PARTICLE;
    public static DefaultParticleType FIRE_SWEEP_ATTACK_PARTICLE;
    public static DefaultParticleType BLOOD_BUBBLE;
    public static DefaultParticleType DRAGON_FIRE;
    public static DefaultParticleType SHOCKWAVE;
    public static DefaultParticleType BLOODWAVE;
    public static DefaultParticleType BLOOD_BUBBLE_SPLATTER;
    public static DefaultParticleType DRAGON_FIRE_SPLATTER;
    public static DefaultParticleType EMPTY_PARTICLE;
    public static DefaultParticleType BACKATTACK;

    public static void init(){
        RED_SWEEP_ATTACK_PARTICLE = Registry.register(Registries.PARTICLE_TYPE, "mamy:red_sweep_attack", FabricParticleTypes.simple(true));
        FIRE_SWEEP_ATTACK_PARTICLE = Registry.register(Registries.PARTICLE_TYPE, "mamy:fire_sweep_attack", FabricParticleTypes.simple(true));

        BLOOD_BUBBLE = Registry.register(Registries.PARTICLE_TYPE, "mamy:blood_bubble", FabricParticleTypes.simple(true));
        DRAGON_FIRE = Registry.register(Registries.PARTICLE_TYPE, "mamy:dragon_fire", FabricParticleTypes.simple(true));

        SHOCKWAVE = Registry.register(Registries.PARTICLE_TYPE, "mamy:shockwave", FabricParticleTypes.simple(true));
        BLOODWAVE = Registry.register(Registries.PARTICLE_TYPE, "mamy:bloodwave", FabricParticleTypes.simple(true));
        BACKATTACK = Registry.register(Registries.PARTICLE_TYPE, "mamy:backattack", FabricParticleTypes.simple(true));

        BLOOD_BUBBLE_SPLATTER = Registry.register(Registries.PARTICLE_TYPE, "mamy:blood_bubble_splatter", FabricParticleTypes.simple(true));
        DRAGON_FIRE_SPLATTER = Registry.register(Registries.PARTICLE_TYPE, "mamy:dragon_fire_splatter", FabricParticleTypes.simple(true));
        EMPTY_PARTICLE = Registry.register(Registries.PARTICLE_TYPE, "mamy:empty", FabricParticleTypes.simple(false));
    }
}
