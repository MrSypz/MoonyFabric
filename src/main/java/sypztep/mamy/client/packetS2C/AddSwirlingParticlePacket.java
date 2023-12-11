package sypztep.mamy.client.packetS2C;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import sypztep.mamy.common.MamyMod;
import sypztep.mamy.common.init.ModParticles;

public class AddSwirlingParticlePacket {
    public static final Identifier ID = MamyMod.id("add_swirl_particle");

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
