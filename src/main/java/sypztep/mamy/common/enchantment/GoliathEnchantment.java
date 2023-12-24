package sypztep.mamy.common.enchantment;

import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import sypztep.mamy.common.init.ModTags;
import sypztep.mamy.common.util.EnchantmentUtil;

public class GoliathEnchantment extends EmptyEnchantment {
    public GoliathEnchantment(Rarity weight, EnchantmentTarget target, EquipmentSlot... slotTypes) {
        super(weight, target, slotTypes);
    }
    @Override
    public void onTargetDamaged(LivingEntity user, Entity target, int level) {
        if (EnchantmentUtil.hasEnchantment(this, user.getMainHandStack()) && user.distanceTo(target) >= 6) {
            return;
        }
        if (target instanceof LivingEntity living && !target.getType().isIn(ModTags.EntityTypes.CANNOT_GOLIATH))
            living.damage(living.getWorld().getDamageSources().playerAttack((PlayerEntity) user),(living.getHealth() * 0.12f));
        else if (target instanceof WardenEntity living)
            living.damage(living.getWorld().getDamageSources().playerAttack((PlayerEntity) user),(living.getHealth() * 0.03f));
        user.heal(EnchantmentUtil.AmountDeal * 0.08f); //Heal 8% of damage that deal
        super.onTargetDamaged(user, target, level);
    }
    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof SwordItem;
    }
}
