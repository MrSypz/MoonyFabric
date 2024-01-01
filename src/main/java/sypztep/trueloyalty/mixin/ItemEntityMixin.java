package sypztep.trueloyalty.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sypztep.trueloyalty.LoyalTrident;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {
    @Nullable
    @Unique
    private Boolean veryLoyalTrident;

    public ItemEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow
    public abstract ItemStack getStack();

    @Shadow
    public abstract boolean cannotPickup();

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void tickItem(CallbackInfo ci) {
        // Only spawn a trident if it has a pickup delay (usually sign of it being dropped by a player)
        if (!this.getWorld().isClient && this.cannotPickup()) {
            if (this.veryLoyalTrident == null) {
                this.veryLoyalTrident = LoyalTrident.hasTrueOwner(this.getStack());
            }
            if (this.veryLoyalTrident) {
                TridentEntity tridentEntity = LoyalTrident.spawnTridentForStack(this, this.getStack());
                if (tridentEntity != null) {
                    LoyalTrident.of(tridentEntity).loyaltrident_sit();
                    this.discard();
                    ci.cancel();
                }
            }
        }
    }
}
