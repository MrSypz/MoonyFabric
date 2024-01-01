package sypztep.mamy.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;
import org.jetbrains.annotations.Nullable;

public class DragonFireParticle extends SpriteBillboardParticle {
    private final SpriteProvider spriteProvider;
    protected DragonFireParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
        super(world, x, y, z, velocityX, velocityY, velocityZ);
        this.spriteProvider = spriteProvider;
        this.setSpriteForAge(spriteProvider);
        this.scale *= 1.75F + this.random.nextFloat() * 0.7F;

    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    protected int getBrightness(float tint) {
        return 240;
    }

    @Override
    public void tick() {
        super.tick();
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;
        this.setSpriteForAge(this.spriteProvider);
        if (this.age++ >= this.maxAge && this.random.nextFloat() <= .35F){
            this.markDead();
            this.world.addParticle(ParticleTypes.SMOKE, this.x, this.y, this.z, this.velocityX, this.velocityY, this.velocityZ);
        } else {
            this.velocityY -= 0.03;
            this.move(this.velocityX, this.velocityY, this.velocityZ);
            if (this.ascending && this.y == this.prevPosY) {
                this.velocityX *= 0.5;
                this.velocityZ *= 0.5;
            }
        }
    }
    @Environment(EnvType.CLIENT)
    public static class DefaultFactory implements ParticleFactory<DefaultParticleType>{
        private final SpriteProvider spriteProvider;

        public DefaultFactory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Nullable
        @Override
        public Particle createParticle(DefaultParticleType parameters, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new DragonFireParticle(clientWorld, d, e, f, g, h, i, this.spriteProvider);
        }
    }
}
