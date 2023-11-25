package sypztep.mamy.common.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import sypztep.mamy.common.init.ModEnchantments;

public class FervorofBattleEnchantment extends EmptyEnchantment{
    public FervorofBattleEnchantment(Rarity weight, EnchantmentTarget target, EquipmentSlot... slotTypes) {
        super(weight, target, slotTypes);
    }
    @Override
    public void onTargetDamaged(LivingEntity user, Entity target, int level) {
    }
    @Override
    public int getMaxLevel() {
        return 4;
    }
    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof SwordItem;
    }

    @Override
    protected boolean canAccept(Enchantment other) {
        return other != ModEnchantments.LETHAL_TEMPO;
    }
}
