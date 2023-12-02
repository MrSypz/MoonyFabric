package sypztep.mamy.common.Item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import sypztep.mamy.common.init.ModItems;
import sypztep.mamy.common.init.ModParticles;

import static sypztep.mamy.common.init.ModStatusEffects.HOLLOW_POWER;
import static sypztep.pickyourpoison.common.init.ModStatusEffects.STIMULATION;

public class HollowmaskItem extends MamyMaskFuncItem {

    public HollowmaskItem(Settings settings) {
        super(ArmorMaterials.NETHERITE,Type.HELMET, settings);
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.BLOCK_SCULK_BREAK;
    }
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack headSlot = user.getEquippedStack(EquipmentSlot.HEAD);
        for (HollowmaskItem mask : ModItems.ALL_MASK) {
            if (!headSlot.isOf(mask)) {
                useMaskParticle(user);
            }
        }
        return super.use(world, user, hand);
    }
    //User Mask method use by DeathScythe and Hollowmask item
    public static void useMaskParticle(PlayerEntity user) {
        if (!user.getWorld().isClient) {
            ((ServerWorld) user.getWorld()).spawnParticles(ModParticles.BLOOD_BUBBLE_SPLATTER, user.getX(), user.getEyeY(), user.getZ(), 45, 0.1D, 0.1D, 0.1D, 0.1D);
            ((ServerWorld) user.getWorld()).spawnParticles(ModParticles.BLOODWAVE, user.getX(), user.getY(), user.getZ(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
        }
    }
    public static boolean HalfMask(ItemStack stack) {
        return stack.getDamage() < stack.getMaxDamage() / 2;
    }
    @Override
    public EquipmentSlot getSlotType() {
        return EquipmentSlot.HEAD;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (entity instanceof PlayerEntity player) {
            ItemStack headSlot = player.getEquippedStack(EquipmentSlot.HEAD);
            boolean hasPowerHollow = player.hasStatusEffect(HOLLOW_POWER);
            if (headSlot.isOf(this)) {
                if (!hasPowerHollow) {
                    int amp = headSlot.isOf(ModItems.VASTO_MASK) ? 4 : headSlot.isOf(ModItems.HOLLOW_MASK_TIER4) ? 3 : headSlot.isOf(ModItems.HOLLOW_MASK_TIER3) ? 2 : headSlot.isOf(ModItems.HOLLOW_MASK_TIER2) ? 1 : 0;
                    int amp2 = headSlot.isOf(ModItems.VASTO_MASK) ? 4 : headSlot.isOf(ModItems.HOLLOW_MASK_TIER4) ? 3 : headSlot.isOf(ModItems.HOLLOW_MASK_TIER3) ? 2 : headSlot.isOf(ModItems.HOLLOW_MASK_TIER2) ? 1 : 0;
                    player.addStatusEffect(new StatusEffectInstance(HOLLOW_POWER, 100, amp2, false, false, false));
                    if (!headSlot.isOf(ModItems.HALF_HOLLOW_MASK))
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 100, amp, false, false, false));
                    if (player.getHungerManager().getFoodLevel() <= 0) {
                        player.addStatusEffect(new StatusEffectInstance(STIMULATION, 100, 0, false, false,false));
                    }
                }
            }
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }
}
