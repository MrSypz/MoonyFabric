package sypztep.mamy.mixin.vanilla.backslot.compat;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class OnDeathItemDropCompatibility extends PlayerEntity {
    public OnDeathItemDropCompatibility(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }
    @Inject(method = "onDeath", at = @At("HEAD"))
    public void onDeathMixin(DamageSource source, CallbackInfo info) {
        if (!this.getWorld().getGameRules().getBoolean(GameRules.KEEP_INVENTORY)) {
            if (!this.getInventory().getStack(0).isEmpty()) {
                if (this.getInventory().getEmptySlot() != -1)
                    this.getInventory().main.set(this.getInventory().getEmptySlot(), this.getInventory().getStack(0));
                else
                    this.dropStack(this.getInventory().getStack(0));
                this.getInventory().removeStack(0);
            }
        }
    }
}
