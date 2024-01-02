package sypztep.mamy.mixin.moditem.mamytrident;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sypztep.mamy.common.Item.HomaItem;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow
    public World world;
    @Shadow
    public abstract double getX();
    @Shadow
    public abstract double getY();
    @Shadow
    public abstract double getZ();

    @Inject(method = "doesRenderOnFire", at = @At(value = "RETURN"), cancellable = true)
    public void removePlayerFireRenderDuringHellforkRiptide(CallbackInfoReturnable<Boolean> cir) {
        if (((Object) this) instanceof PlayerEntity && ((PlayerEntity) (Object) this).isUsingRiptide() && ((((PlayerEntity) (Object) this).getMainHandStack().getItem() instanceof HomaItem) || (((PlayerEntity) (Object) this).getOffHandStack().getItem() instanceof HomaItem))) {
            cir.setReturnValue(false);
        }
    }
    @Inject(method = "isOnFire", at = @At(value = "RETURN"), cancellable = true)
    public void isOnFire(CallbackInfoReturnable<Boolean> cir) {
        if (((Object) this) instanceof PlayerEntity && ((PlayerEntity) (Object) this).isUsingRiptide() && ((((PlayerEntity) (Object) this).getMainHandStack().getItem() instanceof HomaItem) || (((PlayerEntity) (Object) this).getOffHandStack().getItem() instanceof HomaItem))) {
            cir.setReturnValue(true);
        }
    }
}
