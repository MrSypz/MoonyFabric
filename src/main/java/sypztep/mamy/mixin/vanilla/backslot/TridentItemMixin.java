package sypztep.mamy.mixin.vanilla.backslot;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import sypztep.mamy.common.MamyMod;
import sypztep.mamy.common.compat.MamyCompat;
import sypztep.mamy.common.interfaces.ProjectileSlotHolder;
import sypztep.mamy.common.interfaces.WeaponSlotHolder;

@Mixin({TridentItem.class})
public class TridentItemMixin {
    @WrapOperation(
            method = {"onStoppedUsing"},
            at = {@At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"
            )}
    )
    private boolean mamy$spawnEntity(World world, Entity entity, Operation<Boolean> operation, @Local(ordinal = 0) ItemStack stack, @Local(ordinal = 0) LivingEntity user) {
        if (user instanceof PlayerEntity player) {
            if (!MamyCompat.isEnhancementLoaded) {
                PlayerInventory inventory = player.getInventory();
                if (inventory instanceof WeaponSlotHolder holder) {
                    if (entity instanceof ProjectileSlotHolder slotHolder) {
                        int index = holder.mamy$getSlotHolding(stack);
                        if (index != -1) {
                            slotHolder.mamy$setOwnedSlot(index);
                            MamyMod.LOGGER.info("DO?");
                        }
                    }
                }
            }
        }
        return operation.call(world, entity);
    }
}
