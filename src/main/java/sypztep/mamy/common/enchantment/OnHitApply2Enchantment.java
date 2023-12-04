package sypztep.mamy.common.enchantment;

import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

public class OnHitApply2Enchantment extends OnHitApplyEnchantment {
    public OnHitApply2Enchantment(Rarity weight, EnchantmentTarget target, EquipmentSlot... slotTypes) {
        super(weight, target, slotTypes);
    }
    @Override
    public int getMaxLevel() {
        return 4;
    }
}
