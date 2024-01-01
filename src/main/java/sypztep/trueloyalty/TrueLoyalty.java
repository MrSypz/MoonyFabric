package sypztep.trueloyalty;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import sypztep.mamy.common.MamyMod;
import sypztep.trueloyalty.storage.LoyalTridentStorage;

import java.util.UUID;

public final class TrueLoyalty implements ModInitializer {

    public static final String MOD_ID = MamyMod.MODID;
    public static final Identifier RECALL_TRIDENTS_MESSAGE_ID = id("recall_tridents");
    public static final Identifier RECALLING_MESSAGE_ID = id("recalling_tridents");

    public static Identifier id(String path) {
        return new Identifier(MOD_ID, path);
    }

    @Override
    public void onInitialize() {
        Object2IntMap<UUID> recallingPlayers = new Object2IntOpenHashMap<>();
        ServerTickEvents.START_SERVER_TICK.register(server -> recallingPlayers.object2IntEntrySet().removeIf(entry -> {
            ServerPlayerEntity player = server.getPlayerManager().getPlayer(entry.getKey());
            if (player == null) return true;

            if (entry.getIntValue() > 0) {
                entry.setValue(entry.getIntValue() - 1);
                return false;
            }

            LoyalTridentStorage loyalTridentStorage = LoyalTridentStorage.get((ServerWorld) player.getWorld());
            TridentRecaller.RecallStatus newRecallStatus;
            if (loyalTridentStorage.recallTridents(player)) {
                newRecallStatus = TridentRecaller.RecallStatus.RECALLING;
            } else {
                player.sendMessage(Text.translatable("mamy:trident_recall_fail"), true);
                // if there is no trident to recall, reset the player's animation
                newRecallStatus = TridentRecaller.RecallStatus.NONE;
            }
            ((TridentRecaller) player).updateRecallStatus(newRecallStatus);
            return true;
        }));
        ServerPlayNetworking.registerGlobalReceiver(RECALL_TRIDENTS_MESSAGE_ID, (MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) -> {
            TridentRecaller.RecallStatus requested = buf.readEnumConstant(TridentRecaller.RecallStatus.class);

            server.execute(() -> {
                LoyalTridentStorage loyalTridentStorage = LoyalTridentStorage.get((ServerWorld) player.getWorld());
                TridentRecaller.RecallStatus currentRecallStatus = ((TridentRecaller) player).getCurrentRecallStatus();
                TridentRecaller.RecallStatus newRecallStatus;

                if (loyalTridentStorage.hasTridents(player)) {
                    if (currentRecallStatus != requested && requested == TridentRecaller.RecallStatus.RECALLING) {
                        loyalTridentStorage.loadTridents(player);
                        recallingPlayers.put(player.getUuid(), 4);  // wait a few ticks to make sure the entity gets loaded
                    }
                    newRecallStatus = requested;
                } else {
                    newRecallStatus = TridentRecaller.RecallStatus.NONE;
                }

                ((TridentRecaller) player).updateRecallStatus(newRecallStatus);
            });
        });
    }
}
