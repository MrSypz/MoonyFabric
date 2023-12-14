package sypztep.mamy.mixin.vanilla.newdamagesystem.critsystem;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sypztep.mamy.common.interfaces.LivingEntityInvoker;

import java.util.Random;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements LivingEntityInvoker {
    @Unique
    private static final TrackedData<Float> CRIT_RATE;
    @Unique
    private static final TrackedData<Float> CRIT_DMG;
    @Unique
    private final Random critRateRandom = new Random();


    @Shadow public abstract ItemStack getEquippedStack(EquipmentSlot var1);

    @Shadow public abstract double getAttributeValue(EntityAttribute attribute);

    protected LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }


    @Inject(method = {"initDataTracker"},at = {@At("TAIL")})
    protected void initDataTracker(CallbackInfo ci) {
        this.dataTracker.startTracking(CRIT_RATE, 0.0F); //Start With 0% that was default vanilla
        this.dataTracker.startTracking(CRIT_DMG, 50.0F); //Start With 50% that was default vanilla
    }

    @Inject(method = {"writeCustomDataToNbt"},at = {@At("TAIL")})
    private void write(NbtCompound nbt, CallbackInfo ci) {
        nbt.putFloat("CritRate", this.mamy$getCritRate());
        nbt.putFloat("CritDamage", this.mamy$getCritDamage());
    }

    @Inject(method = {"readCustomDataFromNbt"},at = {@At("TAIL")})
    private void read(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("CritRate"))
            this.mamy$setCritRate(nbt.getFloat("CritRate"));
        if (nbt.contains("CritDamage"))
            this.mamy$setCritDamage(nbt.getFloat("CritDamage"));
    }

    @ModifyVariable(method = {"applyDamage"},at = @At("HEAD"),ordinal = 0,argsOnly = true)
    private float applyDamageFirst(float amount, DamageSource source) {
        if (!this.getWorld().isClient()) {
            Entity entity;
            entity = source.getAttacker();
            if (entity instanceof LivingEntityInvoker) {
                LivingEntityInvoker invoker = (LivingEntityInvoker)entity;
                entity = source.getSource();
                if (entity instanceof PersistentProjectileEntity) {
                    PersistentProjectileEntity projectile = (PersistentProjectileEntity) entity;
                    invoker.storeCrit().mamy$setCritical(projectile.isCritical());
                    amount = invoker.calculateCritDamage(amount);
                    return amount;
                }
            }

            if (!(source.getAttacker() instanceof PlayerEntity)) {
                entity = source.getAttacker();
                if (entity instanceof LivingEntityInvoker) {
                    LivingEntityInvoker invoker = (LivingEntityInvoker)entity;
                    amount = invoker.calculateCritDamage(amount);
                }
            }
        }

        return amount;
    }

    public Random getRand() {
        return this.critRateRandom;
    }

    public void mamy$setCritRate(float critRate) {
        this.dataTracker.set(CRIT_RATE, critRate);
    }

    public void mamy$setCritDamage(float critDamage) {
        this.dataTracker.set(CRIT_DMG, critDamage);
    }

    public float mamy$getCritRate() {
        return this.dataTracker.get(CRIT_RATE);
    }
    public float mamy$getCritDamage() {
        return this.dataTracker.get(CRIT_DMG);
    }


    // Method to check if the item is equipped

    static {
        CRIT_RATE = DataTracker.registerData(LivingEntityMixin.class, TrackedDataHandlerRegistry.FLOAT);
        CRIT_DMG = DataTracker.registerData(LivingEntityMixin.class, TrackedDataHandlerRegistry.FLOAT);
    }
}
