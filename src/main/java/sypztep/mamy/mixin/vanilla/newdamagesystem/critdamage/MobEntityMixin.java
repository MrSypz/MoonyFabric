package sypztep.mamy.mixin.vanilla.newdamagesystem.critdamage;

import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({MobEntity.class})
public abstract class MobEntityMixin extends LivingEntityMixin {

    protected MobEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }
    /**
     *  This one apply mob to do crit chance and crit damage
     */
    @Inject(method = {"initialize"},at = {@At("TAIL")})
    private void init(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData, NbtCompound entityNbt, CallbackInfoReturnable<EntityData> cir) {
        float d = difficulty.getClampedLocalDifficulty();
        boolean bl = this.getWorld().getDifficulty() == Difficulty.HARD;
        d = bl ? 1.5F : d;
        this.mamy$setCritRate(this.mamy$getCritRate() + (bl ? 0.5F : 0.0F + this.random.nextFloat()) * 50.0F * d);
        this.mamy$setCritDamage(this.mamy$getCritDamage() + (bl ? 0.5F : 0.0F + this.random.nextFloat()) * 50.0F * d);
    }
}
