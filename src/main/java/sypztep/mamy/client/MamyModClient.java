package sypztep.mamy.client;

import ladysnake.satin.api.event.ShaderEffectRenderCallback;
import ladysnake.satin.api.managed.ManagedShaderEffect;
import ladysnake.satin.api.managed.ShaderEffectManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.TridentItem;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;
import sypztep.mamy.client.packetS2C.*;
import sypztep.mamy.client.particle.*;
import sypztep.mamy.client.registry.Itemregistry;
import sypztep.mamy.client.render.entity.*;
import sypztep.mamy.common.MamyMod;
import sypztep.mamy.common.ModConfig;
import sypztep.mamy.common.component.entity.BackWeaponComponent;
import sypztep.mamy.common.init.ModEntityTypes;
import sypztep.mamy.common.init.ModItems;
import sypztep.mamy.common.init.ModParticles;
import sypztep.mamy.common.interfaces.LivingEntityInvoker;
import sypztep.mamy.common.interfaces.WeaponSlotCallback;
import sypztep.mamy.common.packetC2S.MaskPacket;
import sypztep.mamy.common.packetC2S.SyncCritFlagPacket;
import sypztep.mamy.common.util.AbilityUtil;
import sypztep.mamy.feature.MamyFeature;
import sypztep.mamy.feature.data.PlayerCosmeticData;

import static sypztep.mamy.common.component.entity.VizardComponent.dodash;

public class MamyModClient implements ClientModInitializer {
    public static final KeyBinding SONIDO_KEYBINDING = KeyBindingHelper.registerKeyBinding(new KeyBinding("key." + MamyMod.MODID + ".special", GLFW.GLFW_KEY_UNKNOWN, "key.categories." + MamyMod.MODID));
    public static final KeyBinding SPECIAL_KEYBINDING = KeyBindingHelper.registerKeyBinding(new KeyBinding("key." + MamyMod.MODID + ".special2", GLFW.GLFW_KEY_V, "key.categories." + MamyMod.MODID));
    public static final KeyBinding WEAPON_KEYBINDING = KeyBindingHelper.registerKeyBinding(new KeyBinding("key." + MamyMod.MODID + ".select_weapon", GLFW.GLFW_KEY_UNKNOWN, "key.categories." + MamyMod.MODID));
    public static final KeyBinding SWAP_KEYBINDING = KeyBindingHelper.registerKeyBinding(new KeyBinding("key." + MamyMod.MODID + ".swap_weapon", GLFW.GLFW_KEY_G, "key.categories." + MamyMod.MODID));
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
        ClientPlayNetworking.registerGlobalReceiver(AddAirhikeParticlesPacket.ID,new AddAirhikeParticlesPacket.Receiver());
        EntityRendererRegistry.register(ModEntityTypes.BLOOD_LUST, BloodLustEntityRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.PITCHFORK,ctx -> new MamyTridentEntityRenderer(ctx,MamyMod.id("textures/entity/pitchfork.png"), EntityModelLayers.TRIDENT));
        EntityRendererRegistry.register(ModEntityTypes.ELDER_TRIDENT,ctx -> new MamyTridentEntityRenderer(ctx,MamyMod.id("textures/entity/elder_trident.png"), EntityModelLayers.TRIDENT));
        EntityRendererRegistry.register(ModEntityTypes.GUARDIAN_TRIDENT,ctx -> new MamyTridentEntityRenderer(ctx,MamyMod.id("textures/entity/guardian_trident.png"), EntityModelLayers.TRIDENT));
        EntityRendererRegistry.register(ModEntityTypes.HELLFORK,ctx -> new MamyTridentEntityRenderer(ctx,MamyMod.id("textures/entity/hellfork.png"), EntityModelLayers.TRIDENT));
        EntityRendererRegistry.register(ModEntityTypes.SOULFORK,ctx -> new MamyTridentEntityRenderer(ctx,MamyMod.id("textures/entity/soulfork.png"), EntityModelLayers.TRIDENT));
        EntityRendererRegistry.register(ModEntityTypes.HOMA, HomaEntityRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.HOMA_SOUL, HomaSoulEntityRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.ORBITAL, OrbitalEntityRenderer::new);

        ParticleFactoryRegistry particleRegistry = ParticleFactoryRegistry.getInstance();
        particleRegistry.register(ModParticles.RED_SWEEP_ATTACK_PARTICLE, DeathScytheAttackParticle.Factory::new);
        particleRegistry.register(ModParticles.FIRE_SWEEP_ATTACK_PARTICLE, FireSweepAttackParticle.Factory::new);
        particleRegistry.register(ModParticles.BACKATTACK, BackAttackParticle.Factory::new);

        particleRegistry.register(ModParticles.SHOCKWAVE, ShockwaveParticle.Factory::new);
        particleRegistry.register(ModParticles.BLOODWAVE, BloodwaveParticle.Factory::new);

        particleRegistry.register(ModParticles.BLOOD_BUBBLE, BloodBubbleParticle.Factory::new);
        particleRegistry.register(ModParticles.BLOOD_BUBBLE_SPLATTER, BloodBubbleSplatterParticle.Factory::new);
        particleRegistry.register(ModParticles.DRAGON_FIRE, DragonFireParticle.DefaultFactory::new);
        particleRegistry.register(ModParticles.DRAGON_FIRE_SPLATTER, DragonFireSplatterParticle.Factory::new);
        particleRegistry.register(ModParticles.EMPTY_PARTICLE, EmptyParticle.Factory::new);

        Itemregistry.init();

        ShaderEffectRenderCallback.EVENT.register(tickDelta -> {
            if (MinecraftClient.getInstance().player != null && AbilityUtil.hasAnyMask(MinecraftClient.getInstance().player) && !MinecraftClient.getInstance().player.isSubmergedInWater()) {
                if (dodash)
                    MamyModClient.DASHWARP.render(tickDelta);
            }
            if (MinecraftClient.getInstance().player != null && AbilityUtil.hasAnyMask(MinecraftClient.getInstance().player))
                MamyModClient.HOLLOW_VISION.render(tickDelta);
        });
        WeaponSlotCallback.EVENT.register((player, stack) -> {
            if (stack.getItem() == ModItems.BLOODLUST || stack.getItem() == ModItems.DEATH_SCYTHE) {
                return ActionResult.FAIL;
            } else  {
                return stack.getItem() instanceof TridentItem ? ActionResult.FAIL : ActionResult.PASS;
            }
        });

        ClientTickEvents.START_CLIENT_TICK.register( client -> {
            PlayerEntity player = client.getCameraEntity() instanceof PlayerEntity ? (PlayerEntity) client.getCameraEntity() : null;
            if (!(player instanceof LivingEntity) || !AbilityUtil.hasAnyMask(player)) {
                smoothshade = 40;
                return;
            }
            if (smoothshade > 10) {
                smoothshade--;
                setSaturation(smoothshade * 0.01f);
            }
        });
        ClientTickEvents.END_CLIENT_TICK.register((client) -> {
            if (client.player != null) {
                PlayerCosmeticData cosmeticData = MamyFeature.getCosmeticData(client.player);
                if (WEAPON_KEYBINDING.wasPressed()) {
                    if (cosmeticData != null)
                        BackWeaponComponent.setHoldingBackWeapon(client.player, !BackWeaponComponent.isHoldingBackWeapon(client.player));
                    else {
                        client.player.sendMessage(Text.translatable("backslot.feature.fail").formatted(Formatting.GRAY), true);
                        client.player.playSound(SoundEvents.ENTITY_VILLAGER_NO, 1, 1.0f);
                    }
                }
                if (SWAP_KEYBINDING.wasPressed()) {
                    if (cosmeticData != null)
                        ClientPlayNetworking.send(MamyMod.swapWeaponPacketId, PacketByteBufs.empty());
                }
                if (cooldown > 0)
                    cooldown--;
                if (SPECIAL_KEYBINDING.isPressed() && cooldown == 0) {
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
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(ClientCommandManager.literal("mamy.support")
                .executes(context -> {
                    PlayerEntity player = MinecraftClient.getInstance().player;
                    if (player != null) {
                        PlayerCosmeticData cosmeticData = MamyFeature.getCosmeticData(MinecraftClient.getInstance().player);
                        if (cosmeticData != null) {
                            ClientPlayNetworking.send(MamyMod.utilPacketId, PacketByteBufs.empty());
                            player.getWorld().playSound(null,player.getBlockPos(),SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS,1f,1.2f);
                            return 1;
                        }
                    }
                    return 0;
                })));

        ClientPlayNetworking.registerGlobalReceiver(SyncCritFlagPacket.PACKET, (client, handler, buf, responseSender) -> {
            SyncCritFlagPacket packet = new SyncCritFlagPacket(buf);
            if (client.world != null && client.world.getEntityById(packet.getEntityId()) instanceof LivingEntityInvoker invoker) {
                invoker.mamy$setCritical(packet.getFlag());
            }
        });
    }
}
