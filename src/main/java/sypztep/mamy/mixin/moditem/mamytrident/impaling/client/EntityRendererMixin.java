package sypztep.mamy.mixin.moditem.mamytrident.impaling.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sypztep.mamy.common.Item.HomaItem;

@Mixin(EntityRenderer.class) @Environment(EnvType.CLIENT)
public class EntityRendererMixin<T extends Entity> {
    @Inject(method = "getBlockLight", at = @At("HEAD"), cancellable = true)
    protected void getBlockLight(T entity, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        if (entity instanceof LivingEntity livingEntity && livingEntity.isUsingRiptide() && (livingEntity.getMainHandStack().getItem() instanceof HomaItem)) {
            cir.setReturnValue(15);
        }
    }
}
