package sypztep.mamy.client.packetS2C;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import sypztep.mamy.common.MamyMod;
import sypztep.mamy.common.init.ModParticles;

public class AddSwirlingParticlePacket {
    public static final Identifier ID = MamyMod.id("add_swirl_particle");
    public static void send(ServerPlayerEntity player) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeDouble(player.getX());
        buf.writeDouble(player.getBodyY(0.2f));
        buf.writeDouble(player.getZ());
        for (ServerPlayerEntity pl : PlayerLookup.tracking(player.getServerWorld(), player.getChunkPos())) {
            ServerPlayNetworking.send(pl, AddSwirlingParticlePacket.ID, buf);
        }
    }

    @Environment(EnvType.CLIENT)
    public static class Receiver implements ClientPlayNetworking.PlayChannelHandler {
        @Override
        public void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
            ClientPlayerEntity player = client.player;
            if(player != null){
                World world = player.getWorld();
                double x = buf.readDouble();
                double y = buf.readDouble();
                double z = buf.readDouble();
                world.addParticle(ModParticles.BLOODWAVE, x, y, z, 0, 0, 0);
            }
        }
    }
}
