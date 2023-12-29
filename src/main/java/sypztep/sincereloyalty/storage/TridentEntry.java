package sypztep.sincereloyalty.storage;

import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public abstract class TridentEntry {
    protected final ServerWorld world;
    protected final UUID tridentUuid;

    TridentEntry(ServerWorld world, UUID tridentUuid) {
        this.world = world;
        this.tridentUuid = tridentUuid;
    }

    TridentEntry(ServerWorld world, NbtCompound nbt) {
        this(world, nbt.getUuid("trident_uuid"));
    }

    @Nullable
    public static TridentEntry fromNbt(ServerWorld world, NbtCompound tag) {
        try {
            switch (tag.getString("type")) {
                case "world":
                    return new WorldTridentEntry(world, tag);
                case "inventory":
                    return new InventoryTridentEntry(world, tag);
                default: // pass
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    public UUID getTridentUuid() {
        return tridentUuid;
    }

    public abstract void preloadTrident();

    @Nullable
    public abstract TridentEntity findTrident();

    public NbtCompound toNbt(NbtCompound nbt) {
        nbt.putUuid("trident_uuid", this.tridentUuid);
        return nbt;
    }
}
