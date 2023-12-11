package sypztep.mamy.client;

import ladysnake.satin.api.event.ShaderEffectRenderCallback;
import ladysnake.satin.api.managed.ManagedShaderEffect;
import ladysnake.satin.api.managed.ShaderEffectManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.SwordItem;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import sypztep.mamy.client.packetS2C.*;
import sypztep.mamy.client.particle.*;
import sypztep.mamy.client.registry.Itemregistry;
import sypztep.mamy.common.MamyMod;
import sypztep.mamy.common.ModConfig;
import sypztep.mamy.common.init.ModParticles;
import sypztep.mamy.common.interfaces.LivingEntityInvoker;
import sypztep.mamy.common.packetC2S.MaskPacket;
import sypztep.mamy.common.packetC2S.SyncCritFlagPacket;
import sypztep.mamy.common.util.AbilityUtil;

import static sypztep.mamy.common.component.entity.VizardComponent.dodash;

public class MamyModClient implements ClientModInitializer {
    public static final KeyBinding SONIDO_KEYBINDING = KeyBindingHelper.registerKeyBinding(new KeyBinding("key." + MamyMod.MODID + ".special", GLFW.GLFW_KEY_UNKNOWN, "key.categories." + MamyMod.MODID));
    public static final KeyBinding SPECIAL_KEYBINDING = KeyBindingHelper.registerKeyBinding(new KeyBinding("key." + MamyMod.MODID + ".special2", GLFW.GLFW_KEY_V, "key.categories." + MamyMod.MODID));
    private static final ManagedShaderEffect DASHWARP = ShaderEffectManager.getInstance().manage(MamyMod.id("shaders/post/dash.json"));
    private static final ManagedShaderEffect HOLLOW_VISION = ShaderEffectManager.getInstance().manage(MamyMod.id("shaders/post/hollowvision.json"));
    public static float distortMultiply = 0.0f;
    static final int DEFAULT_COOLDOWN = 20;
    static int cooldown = DEFAULT_COOLDOWN;
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
        ClientPlayNetworking.registerGlobalReceiver(AddSwirlingParticlePacket.ID, new AddSwirlingParticlePacket.Receiver());
        ClientPlayNetworking.registerGlobalReceiver(ResetSonidoInvPacket.ID, new ResetSonidoInvPacket.Receiver());
        ClientPlayNetworking.registerGlobalReceiver(AddMaskParticlePacket.ID, new AddMaskParticlePacket.Receiver());
        ClientPlayNetworking.registerGlobalReceiver(AddHogyokuParticlePacket.ID, new AddHogyokuParticlePacket.Receiver());


        ParticleFactoryRegistry.getInstance().register(ModParticles.RED_SWEEP_ATTACK_PARTICLE, DeathScytheAttackParticle.Factory::new);

        ParticleFactoryRegistry.getInstance().register(ModParticles.BACKATTACK, BackAttackParticle.Factory::new);

        ParticleFactoryRegistry.getInstance().register(ModParticles.SHOCKWAVE, ShockwaveParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ModParticles.BLOODWAVE, BloodwaveParticle.Factory::new);

        ParticleFactoryRegistry.getInstance().register(ModParticles.BLOOD_BUBBLE, BloodBubbleParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ModParticles.BLOOD_BUBBLE_SPLATTER, BloodBubbleSplatterParticle.Factory::new);

        ParticleFactoryRegistry.getInstance().register(ModParticles.DRAGON_FIRE, DragonFireParticle.DefaultFactory::new);
        ParticleFactoryRegistry.getInstance().register(ModParticles.DRAGON_FIRE_SPLATTER, DragonFireSplatterParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ModParticles.EMPTY_PARTICLE, EmptyParticle.Factory::new);

        Itemregistry.init();

        ShaderEffectRenderCallback.EVENT.register(tickDelta -> {
            if (MinecraftClient.getInstance().player != null && AbilityUtil.hasAnyMask(MinecraftClient.getInstance().player) && !MinecraftClient.getInstance().player.isSubmergedInWater()) {
                if (dodash)
                    MamyModClient.DASHWARP.render(tickDelta);
            }
            if (MinecraftClient.getInstance().player != null && AbilityUtil.hasAnyMask(MinecraftClient.getInstance().player))
                MamyModClient.HOLLOW_VISION.render(tickDelta);
        });


        ClientTickEvents.START_CLIENT_TICK.register( client -> {
            PlayerEntity player = client.getCameraEntity() instanceof PlayerEntity ? (PlayerEntity) client.getCameraEntity() : null;
            if (player == null || !(player instanceof LivingEntity) || !AbilityUtil.hasAnyMask(player)) {
                smoothshade = 40;
                return;
            }
            if (smoothshade > 0) {
                smoothshade--;
                setSaturation(smoothshade * 0.01f);
            }
        });
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (cooldown > 0)
                cooldown--;
            if (SPECIAL_KEYBINDING.isPressed() && cooldown == 0) {
                if (client.player != null) {
                     if (AbilityUtil.hasvizard(client.player) && !AbilityUtil.hasAnyMask(client.player)) {
                         MaskPacket.send();
                         cooldown = DEFAULT_COOLDOWN;
                     } else if ((AbilityUtil.hasvizard(client.player)) && AbilityUtil.hasAnyMask(client.player)) {
                         client.player.sendMessage(Text.translatable("vizard.already").formatted(Formatting.GRAY), true);
                         cooldown = DEFAULT_COOLDOWN;
                         client.player.playSound(SoundEvents.ENTITY_VILLAGER_NO, 1, 1.0f);
                     } else {
                         client.player.sendMessage(Text.translatable("vizard.checked").formatted(Formatting.GRAY), true);
                         client.player.playSound(SoundEvents.ENTITY_VILLAGER_NO, 1, 1.0f);
                         cooldown = DEFAULT_COOLDOWN;
                     }
                }
            }
        });
        Registries.ITEM.forEach((item) -> {
            if(item instanceof SwordItem) {
                FabricModelPredicateProviderRegistry.register(item, new Identifier("parrying"), (stack, world, entity, i) -> entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0F : 0.0F);
            }
        });

        ClientPlayNetworking.registerGlobalReceiver(SyncCritFlagPacket.PACKET, (client, handler, buf, responseSender) -> {
            SyncCritFlagPacket packet = new SyncCritFlagPacket(buf);
            if (client.world != null && client.world.getEntityById(packet.getEntityId()) instanceof LivingEntityInvoker invoker) {
                invoker.mamy$setCritical(packet.getFlag());
            }
        });
    }
}
