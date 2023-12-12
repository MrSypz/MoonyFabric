package sypztep.mamy.mixin.moditem.hollowmask;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import sypztep.mamy.common.init.ModEntityAttributes;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {


    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyConstant(method = "attack", constant = @Constant(floatValue = 1.5f))
    private float modifyCritDamage(float originalValue) {
        if (this.getAttributeValue(ModEntityAttributes.GENERIC_HOGYOKU) > 0) {
            return originalValue + 0.2f; // Increase Crit Damage by 20%
        }
        return originalValue; // Return the original value if condition doesn't match
    }
}
