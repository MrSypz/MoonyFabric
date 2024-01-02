package sypztep.mamy.mixin.moditem.mamytrident.impaling;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Slice;
import sypztep.mamy.common.enchantment.BetterImpalingEnchantment;
import sypztep.mamy.common.init.ModItems;

@Mixin(TridentEntity.class)
public abstract class TridentEntityMixin {
    @Shadow
    private ItemStack tridentStack;
    @ModifyVariable(
            method = "onEntityHit",
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/util/hit/EntityHitResult;getEntity()Lnet/minecraft/entity/Entity;"),
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;getAttackDamage(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/EntityGroup;)F")
            ),
            at = @At(value = "STORE", ordinal = 0),
            ordinal = 0
    )
    /**
     * Override Vanilla Stuff and custom for my own damage trident
     * @Author MrSypz
     */
    private float getAttackDamage(float baseDamage, EntityHitResult result) {
        Entity entity = result.getEntity();
        if (tridentStack.isOf(ModItems.HOMA) || tridentStack.isOf(ModItems.HOMASOUL))
            if (entity instanceof LivingEntity living) {
                if (isFireImmune(entity))
                    return baseDamage + 8 + (living.getMaxHealth() * 0.1f); // 8 + 8 + (target 10 % MaxHp )
                return baseDamage + 5; // Modify this as needed
            } else
                return baseDamage;
        return baseDamage + BetterImpalingEnchantment.getAttackDamage(this.tridentStack, result.getEntity());
    }
    @Unique
    private static boolean isFireImmune(Entity target) {
        if (target.isFireImmune()) return true;
        if (!(target instanceof LivingEntity)) return false;
        return ((LivingEntity) target).hasStatusEffect(StatusEffects.FIRE_RESISTANCE);
    }
}
