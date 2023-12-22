package sypztep.mamy.common;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.EntityElytraEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sypztep.mamy.common.init.*;
import sypztep.mamy.common.packetC2S.AirhikePacket;
import sypztep.mamy.common.packetC2S.MaskPacket;
import sypztep.mamy.common.packetC2S.SonidoClearPacket;
import sypztep.mamy.common.packetC2S.SonidoPacket;
import sypztep.mamy.common.util.HogyokuState;

import java.util.Objects;

public class MamyMod implements ModInitializer {
    public static final String MODID = "mamy";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
    public static Identifier id(String id) {
        return new Identifier(MODID, id);
    }


    @Override
    public void onInitialize() {
        LOGGER.info("Moony pls don't wet yourself for Minecraft 1.20.1 Fabric Edition.");


        //C2S Packet
        ServerPlayNetworking.registerGlobalReceiver(SonidoClearPacket.ID, new SonidoClearPacket.Receiver());
        ServerPlayNetworking.registerGlobalReceiver(SonidoPacket.ID, new SonidoPacket.Receiver());
        ServerPlayNetworking.registerGlobalReceiver(MaskPacket.ID, new MaskPacket.Receiver());
        ServerPlayNetworking.registerGlobalReceiver(AirhikePacket.ID,new AirhikePacket.Receiver());

        ModEnchantments.init();
        ModSoundEvents.init();
        ModParticles.init();
        ModStatusEffects.init();
        ModEntityAttributes.init();
        ModIframe.registerHandlers();
        ModItems.init();
        ModEntityTypes.init();
        ModItemGroup.init();
        ModLootableModifiers.LootTable();
        ModConfig.init(MODID, ModConfig.class);

        EntityElytraEvents.ALLOW.register((entity -> false));

        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if (!(entity instanceof PlayerEntity player))
                return;
            final HogyokuState state = HogyokuState.get(world.getServer());
            final String s = player.getEntityName();
            if (!state.map.containsKey(s))
                setupNewPlayer(s, state, player);
        });

        ServerPlayerEvents.COPY_FROM.register(((oldPlayer, newPlayer, alive) -> {
            final HogyokuState state = HogyokuState.get(newPlayer.server);
            final String s = newPlayer.getEntityName();
            final int v;
            if (!state.map.containsKey(s))
                setupNewPlayer(s, state, newPlayer);
            v = Math.max(0, state.map.get(s));
            state.map.put(s, v);

            Objects.requireNonNull(newPlayer.getAttributeInstance(ModEntityAttributes.GENERIC_HOGYOKU)).setBaseValue(v);
        }));


    }

    void setupNewPlayer(String name, HogyokuState state, PlayerEntity player) {
        state.map.put(name, 0);
        state.markDirty();
        Objects.requireNonNull(player.getAttributeInstance(ModEntityAttributes.GENERIC_HOGYOKU)).setBaseValue(0);
    }
}
