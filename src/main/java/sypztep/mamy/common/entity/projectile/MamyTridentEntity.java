package sypztep.mamy.common.entity.projectile;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import sypztep.mamy.mixin.util.accesor.TridentEntityAccessor;

public class MamyTridentEntity extends TridentEntity {
    public MamyTridentEntity(EntityType<? extends MamyTridentEntity> entityType, World world) {
        super(entityType, world);
    }
    public void setTridentAttributes(ItemStack stack) {
        this.setTridentStack(stack.copy());
        this.dataTracker.set(TridentEntityAccessor.mamy$getLoyalty(), (byte) EnchantmentHelper.getLoyalty(stack));
        this.dataTracker.set(TridentEntityAccessor.mamy$getEnchanted(), stack.hasGlint());
    }

    protected float getDragInWater() {
        return 0.99f;
    }

    public void setTridentStack(ItemStack tridentStack) {
        ((TridentEntityAccessor) this).mamy$setTridentStack(tridentStack);
    }

    protected void setDealtDamage(boolean dealtDamage) {
        ((TridentEntityAccessor) this).mamy$setDealtDamage(dealtDamage);
    }

    protected boolean hasDealtDamage() {
        return ((TridentEntityAccessor) this).mamy$hasDealtDamage();
    }
}
