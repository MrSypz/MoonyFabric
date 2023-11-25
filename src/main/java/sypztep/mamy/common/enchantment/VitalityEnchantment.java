package sypztep.mamy.common.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import sypztep.mamy.common.init.ModEnchantments;
import sypztep.mamy.common.util.EnchantmentUtil;

import java.util.UUID;

public class VitalityEnchantment extends EmptyEnchantment {
    private static final UUID VITALITY_ID = UUID.fromString("94e1b6fd-beb6-4163-9beb-904374c69857");

    public VitalityEnchantment(Enchantment.Rarity weight, EnchantmentTarget target) {
        super(weight, target, EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET);
    }

    @Override
    public void onEquipmentChange(int oldLevel, int newLevel, ItemStack oldItem, ItemStack newItem, LivingEntity entity) {
        EntityAttributeInstance att = entity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        float FACTOR_PER_LEVEL = .125f,FACTOR_BASE_LEVEL = .125f;
        if (att != null) {
            EntityAttributeModifier mod = new EntityAttributeModifier(VITALITY_ID, "MamyVitalityMaxHP",
                    (EnchantmentUtil.getMamyEnchantmentAmountCorrectlyWorn(entity.getItemsEquipped(), ModEnchantments.VITALITY, entity) * FACTOR_PER_LEVEL) +
                            (EnchantmentUtil.countMamyEnchantmentInstancesCorrectlyWorn(entity.getItemsEquipped(), ModEnchantments.VITALITY, entity) * FACTOR_BASE_LEVEL)
                    , EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
            ReplaceAttributeModifier(att, mod);
            if (entity.getHealth() > entity.getMaxHealth()) {
                entity.setHealth(entity.getMaxHealth());
            }
        }
    }
    private static void ReplaceAttributeModifier(EntityAttributeInstance att, EntityAttributeModifier mod) {
        att.removeModifier(mod);
        att.addPersistentModifier(mod);
    }
    @Override
    public int getMaxLevel() {
        return 5;
    }
    @Override
    public boolean isTreasure() {
        return true;
    }

    @Override
    public boolean isAvailableForEnchantedBookOffer() {
        return false;
    }

    @Override
    public boolean isAvailableForRandomSelection() {
        return false;
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof ArmorItem;
    }
}
