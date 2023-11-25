package sypztep.mamy.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import sypztep.mamy.client.event.BringerRenderEvent;
import sypztep.mamy.client.packet.AddSonidoParticlePacket;
import sypztep.mamy.client.packet.ResetSonidoInvPacket;
import sypztep.mamy.client.particle.*;
import sypztep.mamy.client.registry.Itemregistry;
import sypztep.mamy.common.Item.HollowmaskItem;
import sypztep.mamy.common.MamyMod;
import sypztep.mamy.common.init.ModItems;
import sypztep.mamy.common.init.ModParticles;

public class MamyModClient implements ClientModInitializer {
    public static final KeyBinding SONIDO_KEYBINDING = KeyBindingHelper.registerKeyBinding(new KeyBinding("key." + MamyMod.MODID + ".special", GLFW.GLFW_KEY_UNKNOWN, "key.categories." + MamyMod.MODID));

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

        HudRenderCallback.EVENT.register(new BringerRenderEvent());

        Itemregistry.init();

        ModelPredicateProviderRegistry.register(ModItems.HOLLOW_MASK_TIER3, new Identifier("breaking"), ((stack, world, entity, seed) -> HollowmaskItem.HalfMask(stack) ? 0.0f : 1.0f));

    }
}
