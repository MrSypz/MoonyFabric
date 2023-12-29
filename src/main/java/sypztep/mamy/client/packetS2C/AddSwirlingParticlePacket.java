package sypztep.mamy.client.packetS2C;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import sypztep.mamy.common.Item.DeathScytheItem;
import sypztep.mamy.common.MamyMod;

public class AddSwirlingParticlePacket {
    public static final Identifier ID = MamyMod.id("add_swirl_particle");
    public static void send(ServerPlayerEntity player,int id) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeInt(id);
        PlayerLookup.tracking(player.getServerWorld(), player.getChunkPos())
                .forEach(pl -> ServerPlayNetworking.send(pl, ID, buf));
    }
    @Environment(EnvType.CLIENT)
    public static class Receiver implements ClientPlayNetworking.PlayChannelHandler {
        @Override
        public void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
            int id = buf.readInt();
            client.execute(() -> {
                LivingEntity entity = (LivingEntity) handler.getWorld().getEntityById(id);
                if (entity != null) {
                    DeathScytheItem.addDeathScytheParticles(entity);
                    entity.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1, (float) (1.5f + entity.getRandom().nextGaussian() / 10));

                }
            });
        }
    }
}
