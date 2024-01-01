package sypztep.trueloyalty.mixin;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import sypztep.trueloyalty.TrueLoyalty;
import sypztep.trueloyalty.TridentRecaller;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements TridentRecaller {
    @NotNull
    @Unique
    private RecallStatus recallingTrident = RecallStatus.NONE;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public RecallStatus getCurrentRecallStatus() {
        return this.recallingTrident;
    }

    @Override
    public void updateRecallStatus(RecallStatus recallingTrident) {
        if (this.recallingTrident != recallingTrident) {
            this.recallingTrident = recallingTrident;
            if (!this.getWorld().isClient) {
                PacketByteBuf res = PacketByteBufs.create();
                res.writeInt(this.getId());
                res.writeEnumConstant(recallingTrident);
                Packet<?> packet = ServerPlayNetworking.createS2CPacket(TrueLoyalty.RECALLING_MESSAGE_ID, res);
                ((ServerPlayerEntity) (Object) this).networkHandler.sendPacket(packet);
                for (ServerPlayerEntity player : PlayerLookup.tracking(this)) {
                    player.networkHandler.sendPacket(packet);
                }
            }
        }
    }
}
