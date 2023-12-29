package sypztep.sincereloyalty.storage;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import sypztep.sincereloyalty.LoyalTrident;

import java.util.UUID;

public class InventoryTridentEntry extends TridentEntry {
    private final UUID playerUuid;

    InventoryTridentEntry(ServerWorld world, UUID tridentUuid, UUID playerUuid) {
        super(world, tridentUuid);
        this.playerUuid = playerUuid;
    }

    InventoryTridentEntry(ServerWorld world, NbtCompound tag) {
        super(world, tag);
        this.playerUuid = tag.getUuid("player_uuid");
    }

    @Override
    public NbtCompound toNbt(NbtCompound nbt) {
        super.toNbt(nbt);
        nbt.putUuid("player_uuid", this.playerUuid);
        return nbt;
    }

    @Override
    public void preloadTrident() {
        // NO-OP players should load themselves
        // TODO maybe load fake players ?
    }

    @Override
    public TridentEntity findTrident() {
        PlayerEntity player = this.world.getPlayerByUuid(this.playerUuid);
        if (player != null) {
            for (int slot = 0; slot < player.getInventory().size(); slot++) {
                ItemStack stack = player.getInventory().getStack(slot);
                NbtCompound loyaltyData = stack.getSubNbt(LoyalTrident.MOD_NBT_KEY);
                if (loyaltyData != null && loyaltyData.containsUuid(LoyalTrident.TRIDENT_UUID_NBT_KEY)) {
                    if (loyaltyData.getUuid(LoyalTrident.TRIDENT_UUID_NBT_KEY).equals(this.tridentUuid)) {
                        TridentEntity tridentEntity = LoyalTrident.spawnTridentForStack(player, stack);
                        if (tridentEntity != null) {
                            player.getInventory().removeStack(slot);
                            return tridentEntity;
                        }
                    }
                }
            }
        }
        return null;
    }

    public boolean isHolder(PlayerEntity holder) {
        return this.playerUuid.equals(holder.getUuid());
    }
}
