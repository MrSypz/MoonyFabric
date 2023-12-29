package sypztep.sincereloyalty.storage;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import sypztep.sincereloyalty.SincereLoyalty;

import java.util.UUID;

public final class WorldTridentEntry extends TridentEntry {
    public static final ChunkTicketType<UUID> TRIDENT_RECALL_TICKET = ChunkTicketType.create(SincereLoyalty.MOD_ID + ":trident_recall", UUID::compareTo, 10);

    private UUID tridentEntityUuid;
    private BlockPos lastPos;

    public WorldTridentEntry(ServerWorld world, UUID tridentUuid, UUID tridentEntityUuid, BlockPos lastPos) {
        super(world, tridentUuid);
        this.tridentEntityUuid = tridentEntityUuid;
        this.lastPos = lastPos;
    }

    public WorldTridentEntry(ServerWorld world, NbtCompound tag) {
        super(world, tag);
        this.tridentEntityUuid = tag.getUuid("trident_entity_uuid");
        this.lastPos = NbtHelper.toBlockPos(tag.getCompound("last_pos"));
    }

    @Override
    public NbtCompound toNbt(NbtCompound nbt) {
        super.toNbt(nbt);
        nbt.putUuid("trident_entity_uuid", this.tridentEntityUuid);
        nbt.put("last_pos", NbtHelper.fromBlockPos(this.lastPos));
        return nbt;
    }

    public void updateLastPos(UUID tridentEntityUuid, BlockPos pos) {
        this.tridentEntityUuid = tridentEntityUuid;
        this.lastPos = pos;
    }

    @Override
    public void preloadTrident() {
        ChunkPos pos = new ChunkPos(this.lastPos);
        this.world.getChunk(pos.x, pos.z);  // just loading it
        this.world.getChunkManager().addTicket(TRIDENT_RECALL_TICKET, pos, 0, this.tridentEntityUuid);
    }

    @Override
    public TridentEntity findTrident() {
        Entity trident = this.world.getEntity(this.tridentEntityUuid);
        if (trident instanceof TridentEntity) {
            return (TridentEntity) trident;
        }
        return null;
    }

}
