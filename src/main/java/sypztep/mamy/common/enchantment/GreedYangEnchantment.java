package sypztep.mamy.common.enchantment;

import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import sypztep.mamy.common.util.EnchantmentUtil;

import static sypztep.mamy.common.util.EnchantmentUtil.AmountDeal;

public class GreedYangEnchantment extends HoeEnchantment{
    public GreedYangEnchantment(Rarity weight, EnchantmentTarget target, EquipmentSlot... slotTypes) {
        super(weight, target, slotTypes);
    }

    @Override
    public void onTargetDamaged(LivingEntity user, Entity target, int level) {
        if (EnchantmentUtil.hasEnchantment(this, user.getMainHandStack()) && user.distanceTo(target) >= 6) {
            return;
        }
        if (user instanceof PlayerEntity player && !player.getItemCooldownManager().isCoolingDown(player.getMainHandStack().getItem())) {
            user.heal(AmountDeal);
            player.getItemCooldownManager().set(player.getMainHandStack().getItem(), 60);
        }
        super.onTargetDamaged(user, target, level);
    }
}
