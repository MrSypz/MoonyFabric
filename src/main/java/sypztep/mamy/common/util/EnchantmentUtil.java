package sypztep.mamy.common.util;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import org.apache.commons.lang3.mutable.MutableInt;
import sypztep.mamy.common.Item.HollowmaskItem;
import sypztep.mamy.common.component.entity.VizardComponent;
import sypztep.mamy.common.enchantment.EmptyEnchantment;

import java.util.HashMap;
import java.util.Map;

public class EnchantmentUtil {

    public static boolean hasEnchantment(Enchantment enchantment, ItemStack stack) {
        return EnchantmentHelper.getLevel(enchantment, stack) > 0;
    }
    public static boolean hasEnchantment(Enchantment enchantment, Entity entity) {
        return entity instanceof LivingEntity living && EnchantmentHelper.getEquipmentLevel(enchantment, living) > 0;
    }
    public static boolean isMaskValid(ItemStack stack) {
        return stack != null && stack.getItem() instanceof HollowmaskItem ;
    }
    public static boolean hasMaskOnHelmet(PlayerEntity player) {
        return isMaskValid(getChestSlotItem(player));
    }
    public static boolean hasMaskEquipped(PlayerEntity player) {
        return isMaskValid(getEquippedCloakItem(player));
    }

    public static ItemStack getChestSlotItem(PlayerEntity player) {
        return player.getEquippedStack(EquipmentSlot.HEAD);
    }

    public static ItemStack getEquippedCloakItem(PlayerEntity player) {
        if (hasMaskOnHelmet(player) && VizardComponent.hasMask && VizardComponent.invisDuration != 0) {
            return getChestSlotItem(player);
        }
        return null;
    }
    private static void forEachMamyEnchantment(Consumer consumer, ItemStack stack) {
        if (stack != null && !stack.isEmpty()) {
            NbtList NbtList = stack.getEnchantments();

            for(int i = 0; i < NbtList.size(); ++i) {
                String string = NbtList.getCompound(i).getString("id");
                int j = NbtList.getCompound(i).getInt("lvl");
                Registries.ENCHANTMENT.getOrEmpty(Identifier.tryParse(string)).ifPresent((enchantment) -> {
                    if(enchantment instanceof EmptyEnchantment) {
                        consumer.accept((EmptyEnchantment) enchantment, j, stack);
                    }
                });
            }
        }
    }
    private static void forEachMamyEnchantment(Consumer consumer, Iterable<ItemStack> stacks) {
        for (ItemStack itemStack : stacks) {
            forEachMamyEnchantment(consumer, itemStack);
        }
    }
    public static void onEquipmentChange(LivingEntity livingEntity, EquipmentSlot equipmentSlot, ItemStack previousStack, ItemStack currentStack){
        Map<EmptyEnchantment, Pair<Integer,Integer>> enchantmentsToCheck = new HashMap<>();
        EnchantmentUtil.forEachMamyEnchantment((enchantment, level, itemStack) ->
                enchantmentsToCheck.put(enchantment, new Pair<>(level,0)),previousStack);
        EnchantmentUtil.forEachMamyEnchantment((enchantment, level, itemStack) -> {
            if(enchantmentsToCheck.containsKey(enchantment)) {
                enchantmentsToCheck.put(enchantment, new Pair<>(enchantmentsToCheck.get(enchantment).getLeft(),level));
            }
            else {
                enchantmentsToCheck.put(enchantment, new Pair<>(0,level));
            }
        },currentStack);
        enchantmentsToCheck.forEach((enchantment,levels) ->
                enchantment.onEquipmentChange(levels.getLeft(),levels.getRight(),previousStack,currentStack,livingEntity));
    }
    public static int getMamyEnchantmentAmountCorrectlyWorn(Iterable<ItemStack> equipment, Enchantment target, LivingEntity entity) {
        MutableInt mutableInt = new MutableInt();
        forEachMamyEnchantment((enchantment, level, itemStack) -> {
            if(enchantment == target && entity.getEquippedStack(LivingEntity.getPreferredEquipmentSlot(itemStack)) == itemStack) {
                mutableInt.add(level);
            }
        }, equipment);
        return mutableInt.intValue();
    }
    public static int countMamyEnchantmentInstancesCorrectlyWorn(Iterable<ItemStack> equipment, Enchantment target, LivingEntity entity) {
        MutableInt mutableInt = new MutableInt();
        forEachMamyEnchantment((enchantment, level, itemStack) -> {
            if(enchantment == target &&
                    doesPassPreferenceRequirement(enchantment,itemStack,entity)) {
                mutableInt.add(1);
            }
        }, equipment);
        return mutableInt.intValue();
    }
    public static boolean doesPassPreferenceRequirement(EmptyEnchantment enchantment, ItemStack itemStack, LivingEntity entity){
        if(enchantment.requiresPreferredSlot()) {
            return entity.getEquippedStack(LivingEntity.getPreferredEquipmentSlot(itemStack)) == itemStack;
        }
        return true;
    }
    @FunctionalInterface
    interface Consumer {
        void accept(EmptyEnchantment enchantment, int level, ItemStack itemStack);
    }
}
