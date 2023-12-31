package sypztep.mamy.mixin.vanilla.backslot;

import net.minecraft.entity.projectile.ProjectileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import sypztep.mamy.common.interfaces.ProjectileSlotHolder;

@Mixin({ProjectileEntity.class})
public class ProjectileEntityMixin implements ProjectileSlotHolder {
    @Unique
    private int mamy$ownedSlot = -1;
    public int mamy$getOwnedSlot() {
        return this.mamy$ownedSlot;
    }
    public void mamy$setOwnedSlot(int slot) {
        this.mamy$ownedSlot = slot;
    }
}
