package sypztep.mamy.client;

import ladysnake.satin.api.event.ShaderEffectRenderCallback;
import ladysnake.satin.api.managed.ManagedShaderEffect;
import ladysnake.satin.api.managed.ShaderEffectManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.lwjgl.glfw.GLFW;
import sypztep.mamy.client.packet.AddSonidoParticlePacket;
import sypztep.mamy.client.packet.ResetSonidoInvPacket;
import sypztep.mamy.client.particle.*;
import sypztep.mamy.client.registry.Itemregistry;
import sypztep.mamy.common.MamyMod;
import sypztep.mamy.common.ModConfig;
import sypztep.mamy.common.component.entity.VizardComponent;
import sypztep.mamy.common.init.ModParticles;

import static sypztep.mamy.common.component.entity.VizardComponent.dodash;

public class MamyModClient implements ClientModInitializer {
    public static final KeyBinding SONIDO_KEYBINDING = KeyBindingHelper.registerKeyBinding(new KeyBinding("key." + MamyMod.MODID + ".special", GLFW.GLFW_KEY_UNKNOWN, "key.categories." + MamyMod.MODID));
    private static final ManagedShaderEffect DASHWARP = ShaderEffectManager.getInstance().manage(MamyMod.id("shaders/post/dash.json"));
    private static final ManagedShaderEffect HOLLOW_VISION = ShaderEffectManager.getInstance().manage(MamyMod.id("shaders/post/hollowvision.json"));
    public static float distortMultiply = 0.0f;
    private static float smoothshade = 40;

    public static void setDistortAmount(float value) {
        distortMultiply = ModConfig.distorsion;
        float distortAmount = value * 0.4f * distortMultiply;
        DASHWARP.setUniformValue("DistortAmount", distortAmount);
    }
    private static void setSaturation(float value) {
        HOLLOW_VISION.setUniformValue("Saturation", 1f-(1f-value));
    }
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(AddSonidoParticlePacket.ID, new AddSonidoParticlePacket.Receiver());
        ClientPlayNetworking.registerGlobalReceiver(ResetSonidoInvPacket.ID, new ResetSonidoInvPacket.Receiver());

        ParticleFactoryRegistry.getInstance().register(ModParticles.RED_SWEEP_ATTACK_PARTICLE, DeathScytheAttackParticle.Factory::new);

        ParticleFactoryRegistry.getInstance().register(ModParticles.SHOCKWAVE, ShockwaveParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ModParticles.BLOODWAVE, BloodwaveParticle.Factory::new);

        ParticleFactoryRegistry.getInstance().register(ModParticles.BLOOD_BUBBLE, BloodBubbleParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ModParticles.BLOOD_BUBBLE_SPLATTER, BloodBubbleSplatterParticle.Factory::new);

        ParticleFactoryRegistry.getInstance().register(ModParticles.DRAGON_FIRE, DragonFireParticle.DefaultFactory::new);
        ParticleFactoryRegistry.getInstance().register(ModParticles.DRAGON_FIRE_SPLATTER, DragonFireSplatterParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ModParticles.EMPTY_PARTICLE, EmptyParticle.Factory::new);

        Itemregistry.init();

        ShaderEffectRenderCallback.EVENT.register(tickDelta -> {
            if (MinecraftClient.getInstance().player != null && VizardComponent.hasAnyMask(MinecraftClient.getInstance().player) && !MinecraftClient.getInstance().player.isSubmergedInWater()) {
                if (dodash)
                    MamyModClient.DASHWARP.render(tickDelta);
            }
            if (MinecraftClient.getInstance().player != null && VizardComponent.hasAnyMask(MinecraftClient.getInstance().player))
                MamyModClient.HOLLOW_VISION.render(tickDelta);
        });


        ClientTickEvents.START_CLIENT_TICK.register( client -> {
            PlayerEntity player = client.getCameraEntity() instanceof PlayerEntity ? (PlayerEntity) client.getCameraEntity() : null;
            if (player == null || !(player instanceof LivingEntity) || !VizardComponent.hasAnyMask(player)) {
                smoothshade = 40;
                return;
            }
            if (smoothshade > 0) {
                smoothshade--;
                setSaturation(smoothshade * 0.01f);
            }
        });
    }
}
