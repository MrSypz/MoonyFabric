package sypztep.mamy.mixin.vanilla.newdamagesystem.critdamage;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import sypztep.mamy.common.init.ModEntityAttributes;

import java.util.function.Consumer;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntityMixin {
    @Unique
    private boolean alreadyCalculated;

    protected PlayerEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Unique
    private void forEachItemExceptOffHand(Consumer<ItemStack> itemStackConsumer) {
        if (!this.getWorld().isClient()) {
            EquipmentSlot[] slot = EquipmentSlot.values();
            int getslot = slot.length;

            for(int i = 0; i < getslot; ++i) {
                EquipmentSlot equipmentSlot = slot[i];
                if (equipmentSlot != EquipmentSlot.OFFHAND) {
                    ItemStack itemStack = this.getEquippedStack(equipmentSlot);
                    if (!itemStack.isEmpty())
                        itemStackConsumer.accept(itemStack);
                }
            }
        }
    }
    public float mamy$getCritRateFromEquipped() {
        MutableFloat additionalRate = new MutableFloat();
        this.forEachItemExceptOffHand((itemStack) -> {
            additionalRate.add(this.getAttributeValue(ModEntityAttributes.GENERIC_CRIT_CHANCE));
        });
        return additionalRate.floatValue();
    }

    public float mamy$getCritDamageFromEquipped() {
        MutableFloat additionalDamage = new MutableFloat();
        this.forEachItemExceptOffHand((itemStack) -> {
            additionalDamage.add(this.getAttributeValue(ModEntityAttributes.GENERIC_CRIT_DAMAGE));
        });
        this.forEachItemExceptOffHand((itemStack) -> {
//            additionalDamage.add(Critical.CRIT_DAMAGE.getPercent(EnchantmentHelper.getLevel(Critical.CRIT_DAMAGE, itemStack)));
        });
//        if (this.getAttributeValue(ModEntityAttributes.GENERIC_HOGYOKU) > 0 && this.isPlayer()) // IF PLAYER HAS CONSUME HOGYOKU ONLY IF PLAYER
//            return additionalDamage.floatValue() + 20f; // Add 20% Crit damage
        return additionalDamage.floatValue();
    }

    @ModifyVariable(method = {"attack"},at = @At(value = "STORE",ordinal = 1),ordinal = 0)
    private float mamy$storedamage(float f) {
        float f1 = this.calculateCritDamage(f);
        this.alreadyCalculated = f != f1;
        return f1;
    }

    @ModifyConstant(method = {"attack"},constant = {@Constant(floatValue = 1.5F)})
    private float mamy$storevanillacritdmg(float defaultcritdmg) {
        float f = this.alreadyCalculated ? 1.0F : (this.storeCrit().mamy$isCritical() ? this.getTotalCritDamage() / 100.0F + 1.0F : defaultcritdmg);
        this.alreadyCalculated = false;
        return f;
    }
}
