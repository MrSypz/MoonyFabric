package sypztep.mamy.feature.particle.pet;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.Model;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import sypztep.mamy.feature.render.entity.GlowyRenderLayer;
import sypztep.mamy.feature.render.entity.model.pet.SquareModel;

public class PlayerSquareParticle extends Particle {
	public final Identifier texture;
	private static final float SINE_45_DEGREES = (float)Math.sin(0.7853981633974483);
	final RenderLayer layer;
	public float yaw;
	public float pitch;
	public float prevYaw;
	public float prevPitch;
	protected PlayerEntity owner;
	Model model;

	protected PlayerSquareParticle(ClientWorld world, double x, double y, double z, Identifier texture, float red, float green, float blue) {
		super(world, x, y, z);
		this.texture = texture;
		this.model = new SquareModel(MinecraftClient.getInstance().getEntityModelLoader().getModelPart(SquareModel.MODEL_LAYER));
		this.layer = RenderLayer.getEntityTranslucent(texture);
		this.gravityStrength = 0.0F;

		this.maxAge = 35;
		this.owner = world.getClosestPlayer((TargetPredicate.createNonAttackable()).setBaseMaxDistance(1D), this.x, this.y, this.z);

		if (this.owner == null) {
			this.markDead();
		}

		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = 0;
	}

	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.CUSTOM;
	}

	@Override
	public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
		Vec3d vec3d = camera.getPos();
		float f = (float) (MathHelper.lerp(tickDelta, this.prevPosX, this.x) - vec3d.getX());
		float g = (float) (MathHelper.lerp(tickDelta, this.prevPosY, this.y) - vec3d.getY());
		float h = (float) (MathHelper.lerp(tickDelta, this.prevPosZ, this.z) - vec3d.getZ());

		MatrixStack matrixStack = new MatrixStack();
		matrixStack.translate(f, g, h);
		matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.lerp(g, this.prevYaw, this.yaw) - 180));
		matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.lerp(g, this.prevPitch, this.pitch)));
		matrixStack.translate(0.0F, 0.0F, 0.0F);
		matrixStack.multiply((new Quaternionf()).setAngleAxis(1.0471976F, SINE_45_DEGREES, 0.0F, SINE_45_DEGREES));
		matrixStack.scale(0.875F, 0.875F, 0.875F);
		matrixStack.multiply((new Quaternionf()).setAngleAxis(1.0471976F, SINE_45_DEGREES, 0.0F, SINE_45_DEGREES));
		matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.lerp(g, this.prevYaw, this.yaw) - 180));
		matrixStack.scale(0.875F, 0.875F, 0.875F);
//		matrixStack.multiply((new Quaternionf()).setAngleAxis(1.0471976F, SINE_45_DEGREES, 0.0F, SINE_45_DEGREES));
//		matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.lerp(g, this.prevPitch, this.pitch)));
		VertexConsumerProvider.Immediate immediate = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
		VertexConsumer vertexConsumer2 = immediate.getBuffer(GlowyRenderLayer.get(texture));
		if (this.alpha > 0) {
			this.model.render(matrixStack, vertexConsumer2, 15728880, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0f);
		}
		immediate.draw();
	}

	@Override
	public void tick() {
		if (this.age > 10) {
			this.alpha = 1f;
		} else {
			this.alpha = 0;
		}

		if (owner != null) {
			this.prevPosX = this.x;
			this.prevPosY = this.y;
			this.prevPosZ = this.z;

			// die if old enough
			if (this.age++ >= this.maxAge) {
				this.markDead();
			}

			this.setPos(owner.getX() + Math.cos(owner.bodyYaw / 50) * 0.5, owner.getY() + owner.getHeight() + 0.5f + Math.sin(owner.age / 12f) / 12f, owner.getZ() - Math.cos(owner.bodyYaw / 50) * 0.5);

			this.prevYaw = this.yaw;
			this.yaw = owner.age * 2;
		} else {
			this.markDead();
		}
	}

	public static class DefaultFactory implements ParticleFactory<DefaultParticleType> {
		private final Identifier texture;
		private final float red;
		private final float green;
		private final float blue;

		public DefaultFactory(SpriteProvider spriteProvider, Identifier texture, float red, float green, float blue) {
			this.texture = texture;
			this.red = red;
			this.green = green;
			this.blue = blue;
		}

		@Nullable
		@Override
		public Particle createParticle(DefaultParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
			return new PlayerSquareParticle(world, x, y, z, this.texture, this.red, this.green, this.blue);
		}
	}
}
