package sypztep.mamy.common.packetC2S;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class SyncCritFlagPacket {
    public static final Identifier PACKET = new Identifier("synccrit", "sync_crit_flag");
    private final int entityId;
    private final boolean flag;

    public SyncCritFlagPacket(int entityId, boolean flag) {
        this.entityId = entityId;
        this.flag = flag;
    }

    public SyncCritFlagPacket(PacketByteBuf byteBuf) {
        this.entityId = byteBuf.readVarInt();
        this.flag = byteBuf.readBoolean();
    }

    public PacketByteBuf write(PacketByteBuf byteBuf) {
        byteBuf.writeVarInt(this.entityId);
        byteBuf.writeBoolean(this.flag);
        return byteBuf;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public boolean getFlag() {
        return this.flag;
    }
}
