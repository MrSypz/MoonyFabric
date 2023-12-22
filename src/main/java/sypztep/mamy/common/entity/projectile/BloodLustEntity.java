package sypztep.mamy.common.entity.projectile;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import sypztep.mamy.common.init.*;

public class BloodLustEntity extends PersistentProjectileEntity {

    private int ticksUntilRemove = 5;

    public BloodLustEntity(World world, LivingEntity owner) {
        super(ModEntityTypes.BLOOD_LUST, owner, world);
    }

    public BloodLustEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }
    @Override
    protected ItemStack asItemStack() {
        return ItemStack.EMPTY;
    }

    @Override
    public void tick() {
        super.tick();
        for (float x = -3.0F; x <= 3.0F; x = (float)((double)x+0.1))
            this.getWorld().addParticle(ModParticles.BLOOD_BUBBLE,this.getX() + (double)x * Math.cos(this.getYaw()), this.getY(), this.getZ() + (double)x * Math.sin(this.getYaw()), this.getVelocity().getX(), this.getVelocity().getY(), this.getVelocity().getZ());

        if (this.inGround || this.age > 20) {
            for(int i = 0; i < 25; ++i) {
                this.getWorld().addParticle(ModParticles.BLOOD_BUBBLE_SPLATTER, this.getX() + this.random.nextGaussian() * 2.0 * Math.cos(this.getYaw()), this.getY(), this.getZ() + this.random.nextGaussian() * 2.0 * Math.sin(this.getYaw()), this.random.nextGaussian() / 10.0, this.random.nextFloat() / 2.0F, this.random.nextGaussian() / 10.0);
            }
            --this.ticksUntilRemove;
        }

        if (this.ticksUntilRemove <= 0)
            this.discard();
        if (!this.getWorld().isClient) {

            for (LivingEntity livingEntity : this.getWorld().getEntitiesByClass(LivingEntity.class, this.getBoundingBox(), (livingEntityx) -> this.getOwner() != livingEntityx)) {
                livingEntity.addStatusEffect(new StatusEffectInstance(ModStatusEffects.GRIEVOUSWOUNDS, 60));
                livingEntity.damage(getWorld().getDamageSources().create(ModDamageTypes.BLOODLUST, this, getOwner()), 15.0f);
            }
        }
    }


    @Override
    protected float getDragInWater() {
        return 0.99f;
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }
    @Override
    protected SoundEvent getHitSound() {
        return ModSoundEvents.ENTITY_GENERIC_BLOODHIT;
    }
    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
    }
}
