package sypztep.mamy.common.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public class EmptyEnchantment extends Enchantment {
    protected boolean REQUIRES_PREFERRED_SLOT = true;
    public EmptyEnchantment(Rarity weight, EnchantmentTarget target, EquipmentSlot... slotTypes) {
        super(weight, target, slotTypes);
    }
    @Override
    public int getMinPower(int level) {
        return 20;
    }

    @Override
    public int getMaxPower(int level) {
        return Integer.MAX_VALUE;
    }

    public boolean requiresPreferredSlot(){
        return REQUIRES_PREFERRED_SLOT;
    }
    public void onEquipmentChange(int oldLevel, int newLevel, ItemStack oldItem, ItemStack newItem, LivingEntity entity){}
}
