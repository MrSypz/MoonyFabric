package sypztep.trueloyalty.storage;

import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class OwnedTridents implements Iterable<TridentEntry> {
    static final OwnedTridents EMPTY = new OwnedTridents();

    private final LoyalTridentStorage parentStorage;
    private final Map<UUID, TridentEntry> ownedTridents;

    private OwnedTridents() {
        this.parentStorage = null;
        this.ownedTridents = Collections.emptyMap();
    }

    OwnedTridents(LoyalTridentStorage parentStorage) {
        this.parentStorage = parentStorage;
        this.ownedTridents = new HashMap<>();
    }

    public void storeTridentPosition(UUID tridentUuid, UUID tridentEntityUuid, BlockPos lastPos) {
        TridentEntry entry = this.ownedTridents.get(tridentUuid);
        if (entry instanceof WorldTridentEntry) {
            ((WorldTridentEntry) entry).updateLastPos(tridentEntityUuid, lastPos);
        } else {
            this.ownedTridents.put(tridentUuid, new WorldTridentEntry(this.parentStorage.world, tridentUuid, tridentUuid, lastPos));
        }
    }

    public void storeTridentHolder(UUID tridentUuid, PlayerEntity holder) {
        TridentEntry entry = this.ownedTridents.get(tridentUuid);
        if (!(entry instanceof InventoryTridentEntry) || !((InventoryTridentEntry) entry).isHolder(holder)) {
            this.addEntry(new InventoryTridentEntry(this.parentStorage.world, tridentUuid, holder.getUuid()));
        }
    }

    private void addEntry(@NotNull TridentEntry entry) {
        this.ownedTridents.put(entry.getTridentUuid(), entry);
    }

    public void clearTridentPosition(UUID tridentUuid) {
        this.ownedTridents.remove(tridentUuid);
    }

    @NotNull
    @Override
    public Iterator<TridentEntry> iterator() {
        return this.ownedTridents.values().iterator();
    }

    public boolean isEmpty() {
        return this.ownedTridents.isEmpty();
    }

    public void fromTag(NbtCompound ownerNbt) {
        NbtList tridentsNbt = ownerNbt.getList("tridents", NbtType.COMPOUND);
        for (int j = 0; j < tridentsNbt.size(); j++) {
            TridentEntry trident = TridentEntry.fromNbt(this.parentStorage.world, tridentsNbt.getCompound(j));
            if (trident != null) {
                this.addEntry(trident);
            }
        }
    }

    public void toTag(NbtCompound ownerNbt) {
        NbtList tridentsNbt = new NbtList();
        for (TridentEntry trident : this.ownedTridents.values()) {
            tridentsNbt.add(trident.toNbt(new NbtCompound()));
        }
        ownerNbt.put("tridents", tridentsNbt);
    }
}
