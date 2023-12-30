package sypztep.mamy.common.packetC2S;

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
import sypztep.mamy.common.component.entity.BackWeaponComponent;

public class HoldWeaponPacket {
    public static final Identifier ID = MamyMod.id("hold_packet");

    public static void send(boolean holdingBackWeapon) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeBoolean(holdingBackWeapon);
        ClientPlayNetworking.send(ID, buf);
    }
    public static class Receiver implements ServerPlayNetworking.PlayChannelHandler {
        @Override
        public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
            boolean hold = buf.readBoolean();
            BackWeaponComponent.setHoldingBackWeapon(player, hold);
        }
    }
}
