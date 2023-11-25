package sypztep.mamy.mixin.enchantment.missilehomming;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ProjectileEntity.class)
public interface ProjectileEntityAccessor {
    @Invoker("getOwner")
    Entity enchantment$getOwner();
}
