package sypztep.mamy.common.enchantment;

import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import sypztep.mamy.common.init.ModItems;

public class VengeanceEnchantment extends EmptyEnchantment{
    public VengeanceEnchantment(Rarity weight, EquipmentSlot... slots) {
        super(weight,EnchantmentTarget.WEAPON, slots);
    }

    @Override
    public int getMinPower(int level) {
        return 50;
    }

    @Override
    public int getMaxPower(int level) {
        return super.getMinPower(level) + 50;
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.isOf(ModItems.BLOODLUST) || stack.isOf(ModItems.DEATH_SCYTHE) || stack.isOf(Items.BOOK) || stack.isOf(Items.ENCHANTED_BOOK);
    }
}
