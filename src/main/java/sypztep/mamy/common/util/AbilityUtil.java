package sypztep.mamy.common.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import org.jetbrains.annotations.NotNull;
import sypztep.mamy.common.Item.HollowmaskItem;
import sypztep.mamy.common.entity.projectile.OrbitalEntity;
import sypztep.mamy.common.init.*;

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
        if (user.getWorld().isClient()) {
            addUseMaskParticle(user);
            return;
        }
        int baseValue = (int) user.getAttributes().getBaseValue(ModEntityAttributes.GENERIC_HOGYOKU);
        ItemStack Hollowmask = getItemStack(baseValue);
        SkillUtil maskskill = new SkillUtil();
        Hollowmask.addEnchantment(ModEnchantments.HOLLOW_CURSE, 1);
        user.equipStack(EquipmentSlot.HEAD, Hollowmask);
        maskskill.ShockWaveDamage(user, 10,0, false,false);
        user.damage(user.getWorld().getDamageSources().create(ModDamageTypes.MASKIMPACT, user), user.getHealth() * 0.5f);
        OrbitalEntity orbitalEntity = new OrbitalEntity(user.getWorld(),user);
        user.getWorld().spawnEntity(orbitalEntity);


    }

    public static void addUseMaskParticle(PlayerEntity player) { //Client Packet
        if (MinecraftClient.getInstance().gameRenderer.getCamera().isThirdPerson() || player != MinecraftClient.getInstance().cameraEntity) {
            for (int i = 0; i < 10; ++i)
                player.getWorld().addParticle(ParticleTypes.FLASH, player.getParticleX(2), player.getRandomBodyY(), player.getParticleZ(2), 0, 0, 0);
            for (int i = 0; i < 22; ++i)
                player.getWorld().addParticle(ModParticles.BLOOD_BUBBLE_SPLATTER, player.getParticleX(2), player.getRandomBodyY(), player.getParticleZ(2), 0.1, 0.1, 0.1);
            player.getWorld().addParticle(ModParticles.BLOODWAVE, player.getParticleX(2), player.getBodyY(0.2f), player.getParticleZ(2), 0.0, 0.0, 0.0);

        }
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
