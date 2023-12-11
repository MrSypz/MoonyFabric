package sypztep.mamy.client.particle;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import sypztep.mamy.client.particle.util.Easing;

public class BackAttackParticle extends SpriteBillboardParticle {
    private final SpriteProvider spriteProvider;
    private float maxSize = 0.5F;
    protected BackAttackParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
        super(world, x, y, z, velocityX, velocityY, velocityZ);
        this.spriteProvider = spriteProvider;
        this.maxAge = 20;
        this.scale = 0.0F;
        this.setSpriteForAge(spriteProvider);
        this.setSprite(spriteProvider);
        this.maxSize = 0.5F + world.random.nextFloat() / 2.0F;
    }

    @Override
    public void tick() {
        if (this.age++ <= 10) {
            this.scale = MathHelper.lerp(Easing.ELASTIC_OUT.ease((float)this.age / 10.0F, 0.0F, 1.0F, 1.0F), 0.0F, this.maxSize);
        } else {
            this.scale = MathHelper.lerp(Easing.EXPO_IN.ease(((float)this.age - 10.0F) / 10.0F, 0.0F, 1.0F, 1.0F), this.maxSize, 0.0F);
        }

        if (this.age > this.maxAge && this.scale <= 0.0F) {
            this.markDead();
        }

    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }
    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }
        @Nullable
        @Override
        public Particle createParticle(DefaultParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new BackAttackParticle(world, x, y, z, velocityX, velocityY, velocityZ, this.spriteProvider);
        }
    }
}
