package sypztep.mamy.common.packet;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import sypztep.mamy.common.MamyMod;
import sypztep.mamy.common.util.AbilityUtil;

public class MaskPackets {
    public static final Identifier ID = MamyMod.id("mask");
    public static void send() {
        ClientPlayNetworking.send(ID, new PacketByteBuf(Unpooled.buffer()));
    }

//
    public static class Receiver implements ServerPlayNetworking.PlayChannelHandler {
        @Override
        public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
            AbilityUtil.UseMask(player);
        }
    }
}
