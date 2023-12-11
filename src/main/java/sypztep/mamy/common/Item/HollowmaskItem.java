package sypztep.mamy.common.Item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import sypztep.mamy.common.init.ModDamageTypes;
import sypztep.mamy.common.init.ModEntityAttributes;
import sypztep.mamy.common.init.ModItems;

import static sypztep.mamy.common.init.ModStatusEffects.HOLLOW_POWER;
import static sypztep.pickyourpoison.common.init.ModStatusEffects.STIMULATION;

public class HollowmaskItem extends MamyMaskFuncItem {

    public HollowmaskItem(Settings settings) {
        super(ArmorMaterials.NETHERITE,Type.HELMET, settings);
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE;
    }
    @Override
    public boolean hasGlint(ItemStack stack) {
        return false;
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
                    if (player.getHungerManager().getFoodLevel() <= 0)
                        player.addStatusEffect(new StatusEffectInstance(STIMULATION, 100, 0, false, false,false));
                }
            }
            if (player.getAttributes().getBaseValue(ModEntityAttributes.GENERIC_HOGYOKU) < 1 && headSlot.isOf(this)) {
                player.equipStack(EquipmentSlot.HEAD,new ItemStack(ItemStack.EMPTY.getItem()));
                player.damage(player.getWorld().getDamageSources().create(ModDamageTypes.MASKIMPACT), + player.getHealth() * 0.99f);
            }
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }
}
