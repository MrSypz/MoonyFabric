package sypztep.mamy.client.particle;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

public class FireSweepAttackParticle extends SpriteBillboardParticle {
    private final SpriteProvider spriteWithAge;

    private FireSweepAttackParticle(ClientWorld world, double x, double y, double z, double scale, SpriteProvider spriteWithAge) {
        super(world, x, y, z, 0.0, 0.0, 0.0);
        this.spriteWithAge = spriteWithAge;
        this.maxAge = 4;
        this.scale = 1.0F - (float)scale * 0.5F;
        this.setSpriteForAge(spriteWithAge);
    }
    @Override
    protected int getBrightness(float tint) {
        return 15728880;
    }

    @Override
    public void tick() {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;
        if (this.age++ >= this.maxAge) {
            this.markDead();
        } else {
            this.setSpriteForAge(this.spriteWithAge);
        }
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_LIT;
    }
    public static record Factory(SpriteProvider spriteSet) implements ParticleFactory<DefaultParticleType> {
        public Factory(SpriteProvider spriteSet){
            this.spriteSet = spriteSet;
        }
        public Particle createParticle(DefaultParticleType parameters, ClientWorld world, double x , double y, double z, double velocityX, double velocityY, double velocityZ){
            return new FireSweepAttackParticle(world ,x ,y ,z ,velocityX, this.spriteSet);
        }

        public SpriteProvider spriteSet(){
            return this.spriteSet;
        }
    }
}
