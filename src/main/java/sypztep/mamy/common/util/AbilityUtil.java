package sypztep.mamy.common.util;

import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import org.jetbrains.annotations.NotNull;
import sypztep.mamy.common.Item.HollowmaskItem;
import sypztep.mamy.common.init.ModDamageTypes;
import sypztep.mamy.common.init.ModEntityAttributes;
import sypztep.mamy.common.init.ModItems;

public class AbilityUtil {
    public static boolean hasvizard(PlayerEntity user) {
        if (user.getAttributes().getBaseValue(ModEntityAttributes.GENERIC_HOGYOKU) > 0)
            return true;
        return false;
    }
    public static void UseMask(PlayerEntity user) {
        boolean itemToCheck = AbilityUtil.hasAnyMask(user);
        ItemStack getHeadSlot = user.getEquippedStack(EquipmentSlot.HEAD);
        if (getHeadSlot.isEmpty())
            equipMask(user);
        else {
            if (!itemToCheck) {
                int emptySlot = user.getInventory().getEmptySlot();
                if (emptySlot >= 0)
                    user.getInventory().setStack(emptySlot, getHeadSlot);
                else
                    user.dropItem(getHeadSlot, false);
                equipMask(user);
            }
        }
    }
    public static boolean hasAnyMask(PlayerEntity player) {
        for (ItemStack stack : player.getInventory().armor) {
            for (HollowmaskItem mask : ModItems.ALL_MASK) {
                if (stack.getItem() == mask) {
                    return true;
                }
            }
        }
        return false;
    }
    public static void equipMask(PlayerEntity user) {
        int baseValue = (int) user.getAttributes().getBaseValue(ModEntityAttributes.GENERIC_HOGYOKU);
        ItemStack Hollowmask = getItemStack(baseValue);

        Hollowmask.addEnchantment(Enchantments.BINDING_CURSE, 1);
        Hollowmask.addEnchantment(Enchantments.VANISHING_CURSE, 1);
        Hollowmask.addHideFlag(ItemStack.TooltipSection.ENCHANTMENTS);
        user.equipStack(EquipmentSlot.HEAD, Hollowmask);
        HollowmaskItem.useMaskParticle(user);
        SkillUtil.ShockWaveDamage(user, 0, false,false);
        user.damage(user.getWorld().getDamageSources().create(ModDamageTypes.MASKIMPACT, user), user.getHealth() * 0.5f);
        user.playSound(SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, SoundCategory.PLAYERS, 1.0f, 2f);
    }

    @NotNull
    private static ItemStack getItemStack(int baseValue) {
        ItemStack Hollowmask;
        if (baseValue == 1)
            Hollowmask = new ItemStack(ModItems.HALF_HOLLOW_MASK);
        else if (baseValue == 2)
            Hollowmask = new ItemStack(ModItems.HOLLOW_MASK_TIER1);
        else if (baseValue == 3)
            Hollowmask = new ItemStack(ModItems.HOLLOW_MASK_TIER2);
        else if (baseValue == 4)
            Hollowmask = new ItemStack(ModItems.HOLLOW_MASK_TIER3);
        else if (baseValue == 5)
            Hollowmask = new ItemStack(ModItems.HOLLOW_MASK_TIER4);
        else Hollowmask = new ItemStack(ModItems.HALF_HOLLOW_MASK);
        return Hollowmask;
    }

}
