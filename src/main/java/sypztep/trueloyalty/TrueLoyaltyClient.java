package sypztep.trueloyalty;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class TrueLoyaltyClient implements ClientModInitializer {
    public static final TrueLoyaltyClient INSTANCE = new TrueLoyaltyClient();
    public static final int RECALL_ANIMATION_START = 10;
    public static final int RECALL_TIME = 35;   // + count 5 forced ticks serverside to load chunks

    private int useTime = 0;
    private int failedUseCountdown = 0;

    public void setFailedUse(int itemUseCooldown) {
        this.failedUseCountdown = itemUseCooldown + 1;
    }

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(mc -> {
            TridentRecaller.RecallStatus recalling = tickTridentRecalling(mc);
            if (recalling != null) {
                PacketByteBuf buf = PacketByteBufs.create();
                buf.writeEnumConstant(recalling);
                ClientPlayNetworking.send(TrueLoyalty.RECALL_TRIDENTS_MESSAGE_ID, buf);
            }
        });
        ClientPlayNetworking.registerGlobalReceiver(TrueLoyalty.RECALLING_MESSAGE_ID, (MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) -> {
            int playerId = buf.readInt();
            TridentRecaller.RecallStatus recalling = buf.readEnumConstant(TridentRecaller.RecallStatus.class);
            client.execute(() -> {
                Entity player = Objects.requireNonNull(client.world).getEntityById(playerId);
                if (player instanceof TridentRecaller) {
                    ((TridentRecaller) player).updateRecallStatus(recalling);
                }
            });
        });
    }

    @Nullable
    private TridentRecaller.RecallStatus tickTridentRecalling(MinecraftClient mc) {
        if (this.failedUseCountdown > 0) {
            PlayerEntity player = mc.player;

            if (player != null && player.getMainHandStack().isEmpty()) {
                ++this.useTime;

                if (this.useTime == RECALL_ANIMATION_START) {
                    return TridentRecaller.RecallStatus.CHARGING;
                } else if (this.useTime == RECALL_TIME) {
                    this.useTime = 0;
                    return TridentRecaller.RecallStatus.RECALLING;
                }
            }
            this.failedUseCountdown--;
        } else if (this.useTime > 0) {
            this.useTime = 0;
            return TridentRecaller.RecallStatus.NONE;
        }
        return null;
    }
}
