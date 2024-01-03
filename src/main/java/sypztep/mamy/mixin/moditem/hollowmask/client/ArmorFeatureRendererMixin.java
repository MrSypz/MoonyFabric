package sypztep.mamy.mixin.moditem.hollowmask.client;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sypztep.mamy.common.util.EnchantmentUtil;

@Mixin(ArmorFeatureRenderer.class)
public class ArmorFeatureRendererMixin {
    @Inject(method = "renderArmor", at = @At("HEAD"), cancellable = true)
    public void mamy$disablearmorrender(MatrixStack matrices, VertexConsumerProvider vertexConsumers,
                                        LivingEntity livingEntity, EquipmentSlot equipmentSlot, int i,
                                        BipedEntityModel<LivingEntity> bipedEntityModel, CallbackInfo ci) {
        if(livingEntity.isInvisible() && !(EnchantmentUtil.hasMaskEquipped((PlayerEntity) livingEntity))) {
            if ((EnchantmentUtil.hasMaskEquipped((PlayerEntity) livingEntity) && livingEntity.isInvisible()))
                ci.cancel();
        }
    }
}
