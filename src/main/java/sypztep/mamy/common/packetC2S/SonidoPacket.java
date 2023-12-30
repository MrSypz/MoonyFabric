package sypztep.mamy.common.packetC2S;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import sypztep.mamy.client.packetS2C.AddSonidoParticlePacket;
import sypztep.mamy.common.MamyMod;
import sypztep.mamy.common.component.entity.VizardComponent;
import sypztep.mamy.common.init.ModEntityComponents;

public class SonidoPacket {
    public static final Identifier ID = MamyMod.id("sonido");

    public static void send() {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        ClientPlayNetworking.send(ID, buf);
    }

    public static class Receiver implements ServerPlayNetworking.PlayChannelHandler {
        @Override
        public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
            server.execute(() -> ModEntityComponents.VIZARD.maybeGet(player).ifPresent(vizardComponent -> {
                if (VizardComponent.hasMask) {
                    VizardComponent.handle(player, vizardComponent);
                    PlayerLookup.tracking(player).forEach(foundPlayer -> AddSonidoParticlePacket.send(foundPlayer, player.getId()));
                }
            }));
        }
    }
}