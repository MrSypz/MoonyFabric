package sypztep.mamy.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import org.jetbrains.annotations.Nullable;

public class EmptyParticle extends Particle {
    protected EmptyParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        super(world, x, y, z, velocityX, velocityY, velocityZ);
        // Set the minimum lifetime to 1 tick to effectively remove the particle immediately
        this.maxAge = 1;
    }

    @Override
    public ParticleTextureSheet getType() {
        // This method defines the texture sheet. Setting it to NO_RENDER will effectively make it invisible.
        return ParticleTextureSheet.NO_RENDER;
    }

    @Override
    public void tick() {
        // You can optionally add logic here if needed
        super.tick();
    }

    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {

    }
    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider){
            this.spriteProvider = spriteProvider;
        }

        @Nullable
        @Override
        public Particle createParticle(DefaultParticleType parameters, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new EmptyParticle(clientWorld, d, e, f, g, h, i);
        }
    }
}