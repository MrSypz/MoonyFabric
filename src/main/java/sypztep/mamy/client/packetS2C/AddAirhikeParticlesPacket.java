package sypztep.mamy.client.packetS2C;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import sypztep.mamy.common.MamyMod;
import sypztep.mamy.common.component.entity.VizardComponent;

public class AddAirhikeParticlesPacket {
	public static final Identifier ID = MamyMod.id("add_airhike_particles");

	public static void send(ServerPlayerEntity player, int id) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeInt(id);
		ServerPlayNetworking.send(player, ID, buf);
	}

	@Environment(EnvType.CLIENT)
	public static class Receiver implements ClientPlayNetworking.PlayChannelHandler {
		@Override
		public void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
			int id = buf.readInt();
			client.execute(() -> {
				Entity entity = handler.getWorld().getEntityById(id);
				if (entity != null) {
					VizardComponent.addAirhikeParticles(entity);
				}
			});
		}
	}
}
