package sypztep.mamy.common.interfaces;

import net.minecraft.item.ItemStack;

public interface WeaponSlotHolder {
    int mamy$getSlotHolding(ItemStack stack);

    boolean mamy$tryInsertIntoSlot(int id, ItemStack stack);
}
