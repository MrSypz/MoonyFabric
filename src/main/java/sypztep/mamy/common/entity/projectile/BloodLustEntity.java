package sypztep.mamy.common.entity.projectile;

import com.google.common.collect.Sets;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import sypztep.mamy.common.init.*;

import java.util.Iterator;
import java.util.Set;

public class BloodLustEntity extends PersistentProjectileEntity {

    private final Set<StatusEffectInstance> effects = Sets.newHashSet();
    private int ticksUntilRemove = 5;
    public void addEffect(StatusEffectInstance effect) {
        this.effects.add(effect);
    }


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
            Iterator<LivingEntity> var6 = this.getWorld().getEntitiesByClass(LivingEntity.class, this.getBoundingBox(), (livingEntityx) -> this.getOwner() != livingEntityx).iterator();
            while (var6.hasNext()) {
                LivingEntity livingEntity = var6.next();
                livingEntity.damage(getWorld().getDamageSources().create(ModDamageTypes.BLOODLUST,this,getOwner()), 15F);

                for (StatusEffectInstance effect : this.effects)
                    livingEntity.addStatusEffect(effect);
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
