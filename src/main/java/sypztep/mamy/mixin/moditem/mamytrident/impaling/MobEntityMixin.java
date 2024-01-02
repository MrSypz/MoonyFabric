package sypztep.mamy.mixin.moditem.mamytrident.impaling;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Slice;
import sypztep.mamy.common.enchantment.BetterImpalingEnchantment;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity {
    protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyVariable(
            method = "tryAttack",
            slice = @Slice(
                    from = @At("HEAD"),
                    to = @At(value = "FIELD", target = "Lnet/minecraft/entity/attribute/EntityAttributes;GENERIC_ATTACK_KNOCKBACK:Lnet/minecraft/entity/attribute/EntityAttribute;")
            ),
            at = @At(value = "STORE", ordinal = 0),
            ordinal = 0
    )
    private float getAttackDamage(float baseDamage, Entity target) {
        return baseDamage + BetterImpalingEnchantment.getAttackDamage(this.getMainHandStack(), target);
    }
}
