package sypztep.mamy.common.enchantment;

import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.server.world.ServerWorld;
import sypztep.mamy.common.MamyMod;
import sypztep.mamy.common.component.entity.BringerComponent;
import sypztep.mamy.common.init.ModDamageTypes;
import sypztep.mamy.common.init.ModParticles;
import sypztep.mamy.common.init.ModSoundEvents;
import sypztep.mamy.common.init.ModStatusEffects;

import static sypztep.mamy.common.component.entity.BringerComponent.DEFAULT_COOLDOWN;
import static sypztep.mamy.common.component.entity.BringerComponent.setDeathCooldown;

public class BringerDeathEnchantment extends EmptyEnchantment {
    private static int deathCooldown = DEFAULT_COOLDOWN , lastdeathCooldown = DEFAULT_COOLDOWN;
    static float damageamount;
    public BringerDeathEnchantment(Rarity weight, EnchantmentTarget target, EquipmentSlot... slotTypes) {
        super(weight, target, slotTypes);
    }
    @Override
    public void onTargetDamaged(LivingEntity user, Entity target, int level) {
        if (!target.getWorld().isClient && target instanceof LivingEntity enemy) {
            if (BringerComponent.getDeathCooldown() == 0 && target.distanceTo(user) <= 6) {
                float DamageDeal = enemy.getMaxHealth() * (0.04f + (0.12f - 0.04f) / 4 * (level - 1));
                target.damage(target.getWorld().getDamageSources().create(ModDamageTypes.BRINGER,user), DamageDeal + damageamount);// 4-12% + 50% of damage deal of max health
                setDeathCooldown(DEFAULT_COOLDOWN - (DEFAULT_COOLDOWN - 60) / (5 - 1) * (level - 1));
                user.heal((DamageDeal + damageamount) * 0.8f); //Heal 80% of damage deal
                enemy.addStatusEffect(new StatusEffectInstance(ModStatusEffects.GRIEVOUSWOUNDS, 180, 0, true, true, true));
                target.playSound(ModSoundEvents.ENTITY_GENERIC_BLOODHIT, 1, 1.0f);
                MamyMod.LOGGER.info(String.valueOf((DamageDeal + damageamount) * 0.8f));
                addParticles(enemy);
            }
        }
    }
    private void addParticles(Entity target) {
        ((ServerWorld) target.getWorld()).spawnParticles(ModParticles.BLOOD_BUBBLE_SPLATTER, target.getX(), target.getEyeY(), target.getZ(), 45, 0.1, 0.1, 0.1, 0.1D);
    }
    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof SwordItem || stack.getItem() == Items.BOOK;
    }
    public static int getDeathCooldown(){
        return deathCooldown;
    }
    public static int getLastdeathCooldown(){
        return lastdeathCooldown;
    }

    public static float getAmount(float amount) {
        BringerDeathEnchantment.damageamount = amount;
        return damageamount;
    }
}
