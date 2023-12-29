package sypztep.sincereloyalty.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sypztep.sincereloyalty.LoyalTrident;
import sypztep.sincereloyalty.storage.LoyalTridentStorage;

import java.util.Objects;
import java.util.UUID;

@Mixin(Item.class)
public abstract class ItemMixin {
    @Inject(method = "inventoryTick", at = @At("RETURN"))
    private void updateTridentInInventory(ItemStack stack, World world, Entity entity, int slot, boolean selected, CallbackInfo ci) {
        if (entity.age % 10 == 0 && !entity.getWorld().isClient && entity instanceof PlayerEntity) {
            UUID trueOwner = LoyalTrident.getTrueOwner(stack);
            if (Objects.equals(trueOwner, entity.getUuid())) {
                NbtCompound loyaltyData = Objects.requireNonNull(stack.getSubNbt(LoyalTrident.MOD_NBT_KEY));
                if (!Objects.equals(entity.getEntityName(), loyaltyData.getString(LoyalTrident.OWNER_NAME_NBT_KEY))) {
                    loyaltyData.putString(LoyalTrident.OWNER_NAME_NBT_KEY, entity.getEntityName());
                }
            } else if (trueOwner != null) {
                LoyalTridentStorage.get((ServerWorld) world).memorizeTrident(trueOwner, LoyalTrident.getTridentUuid(stack), (PlayerEntity) entity);
            }
        }
    }
}
