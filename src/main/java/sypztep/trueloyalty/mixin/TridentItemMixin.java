package sypztep.trueloyalty.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import sypztep.trueloyalty.LoyalTrident;

@Mixin(TridentItem.class)
public class TridentItemMixin {
    @ModifyVariable(method = "onStoppedUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/TridentEntity;setVelocity(Lnet/minecraft/entity/Entity;FFFFF)V"))
    private TridentEntity setTridentReturnSlot(TridentEntity trident, ItemStack stack, World world, LivingEntity user) {
        LoyalTrident.of(trident).loyaltrident_setReturnSlot(user.getActiveHand() == Hand.OFF_HAND ? -1 : ((PlayerEntity) user).getInventory().selectedSlot);
        return trident;
    }
}
