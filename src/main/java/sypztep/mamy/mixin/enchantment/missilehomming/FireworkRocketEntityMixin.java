package sypztep.mamy.mixin.enchantment.missilehomming;

import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sypztep.mamy.common.init.ModEnchantments;
import sypztep.mamy.common.util.EnchantmentUtil;

@Mixin(FireworkRocketEntity.class)
public abstract class FireworkRocketEntityMixin extends Entity {
    @Shadow private int lifeTime;
    @Unique
    private static final TrackedData<? super ItemStack> STACK_SHOT_FROM;
    @Unique
    private static final TrackedData<? super Boolean> HAS_HOMING;

    public FireworkRocketEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }
    @Inject(
            method = {"<init>(Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/Entity;DDDZ)V"},
            at = {@At("TAIL")}
    )
    private void enchancement$homing(World world, ItemStack stack, Entity entity, double x, double y, double z, boolean shotAtAngle, CallbackInfo ci) {
        if (entity instanceof LivingEntity living) {
            ItemStack shotFromStack = ItemStack.EMPTY;
            if (EnchantmentUtil.hasEnchantment(ModEnchantments.MISSILE_HOMMING, living.getMainHandStack())) {
                shotFromStack = living.getMainHandStack();
            } else if (EnchantmentUtil.hasEnchantment(ModEnchantments.MISSILE_HOMMING, living.getOffHandStack())) {
                shotFromStack = living.getOffHandStack();
            }

            if (!shotFromStack.isEmpty()) {
                this.getDataTracker().set(STACK_SHOT_FROM, shotFromStack);
                this.getDataTracker().set(HAS_HOMING, true);
            }
        }

    }

    @ModifyVariable(
            method = {"tick"},
            at = @At(
                    value = "INVOKE_ASSIGN",
                    target = "Lnet/minecraft/entity/projectile/FireworkRocketEntity;getVelocity()Lnet/minecraft/util/math/Vec3d;",
                    ordinal = 1
            ),
            ordinal = 0
    )
    private Vec3d enchancement$homing(Vec3d value) {
        if ((Boolean) this.getDataTracker().get(HAS_HOMING)) {
            Entity var3 = ((ProjectileEntityAccessor)this).enchantment$getOwner();
            if (var3 instanceof LivingEntity living) {
                ItemStack stackShotFrom = (ItemStack)this.getDataTracker().get(STACK_SHOT_FROM);
                if (living.getMainHandStack() == stackShotFrom || living.getOffHandStack() == stackShotFrom) {
                    Vec3d eyePos = living.getEyePos();
                    if (this.squaredDistanceTo(eyePos) < 4000.0) {
                        BlockHitResult raycast = this.getWorld().raycast(new RaycastContext(eyePos, eyePos.add(living.getRotationVector().multiply(64.0)), RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, living));
                        if (raycast.getType() != HitResult.Type.MISS) {
                            this.lifeTime += 5;
                        }
                        Vec3d target = raycast.getPos();
                        this.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, target);
                        return value.lerp(target.subtract(this.getPos()).normalize(), (float)(this.age % 60) / 60.0F);
                    }
                }
            }
        }

        return value;
    }
    @Inject(
            method = {"initDataTracker"},
            at = {@At("TAIL")}
    )
    private void enchantment$homing(CallbackInfo ci) {
        this.getDataTracker().startTracking(STACK_SHOT_FROM, ItemStack.EMPTY);
        this.getDataTracker().startTracking(HAS_HOMING, false);
    }

    static {
        STACK_SHOT_FROM = DataTracker.registerData(FireworkRocketEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);
        HAS_HOMING = DataTracker.registerData(FireworkRocketEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    }
}
