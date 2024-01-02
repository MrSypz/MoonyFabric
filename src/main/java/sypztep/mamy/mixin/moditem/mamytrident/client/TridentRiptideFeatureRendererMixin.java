package sypztep.mamy.mixin.moditem.mamytrident.client;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.TridentRiptideFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import sypztep.mamy.common.MamyMod;
import sypztep.mamy.common.init.ModItems;
import sypztep.mamy.common.init.ModTags;


@Mixin(TridentRiptideFeatureRenderer.class)
public abstract class TridentRiptideFeatureRendererMixin {
    @Unique
    public final Identifier HELLFORK_RIPTIDE_TEXTURE = MamyMod.id("textures/entity/homa_riptide.png");
    @Unique
    public final Identifier SOULFORK_RIPTIDE_TEXTURE = MamyMod.id("textures/entity/homasoul_riptide.png");

    @ModifyVariable(method = "render*", at = @At("STORE"))
    private VertexConsumer swapHotRiptide(VertexConsumer orig, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, LivingEntity livingEntity) {
        if (livingEntity instanceof PlayerEntity && livingEntity.isUsingRiptide() && (livingEntity.getMainHandStack().getItem() == ModItems.HOMA || (livingEntity.getOffHandStack().getItem() == ModItems.HOMA) || (livingEntity.getOffHandStack().getItem() == ModItems.HELLFORK) && !livingEntity.getMainHandStack().isIn(ModTags.Items.TRIDENTS)))
            return vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutoutNoCull(HELLFORK_RIPTIDE_TEXTURE));
         else if (livingEntity instanceof PlayerEntity && livingEntity.isUsingRiptide() && (livingEntity.getMainHandStack().getItem() == ModItems.HOMASOUL || (livingEntity.getOffHandStack().getItem() == ModItems.HOMASOUL) || (livingEntity.getOffHandStack().getItem() == ModItems.SOULFORK) && !livingEntity.getMainHandStack().isIn(ModTags.Items.TRIDENTS)))
            return vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutoutNoCull(SOULFORK_RIPTIDE_TEXTURE));
        return orig;
    }
}