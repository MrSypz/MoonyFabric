package sypztep.mamy.common.util;

import net.minecraft.item.ItemStack;

public interface WeaponSlotHolder {

    int mamy$getSlotHolding(ItemStack var1);

    boolean mamy$tryInsertIntoSlot(int var1, ItemStack var2);
}
