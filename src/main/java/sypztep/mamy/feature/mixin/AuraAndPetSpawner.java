package sypztep.mamy.feature.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sypztep.mamy.common.ModConfig;
import sypztep.mamy.feature.MamyFeature;
import sypztep.mamy.feature.data.PlayerCosmeticData;

@Mixin(PlayerEntity.class)
public abstract class AuraAndPetSpawner extends LivingEntity {
	protected AuraAndPetSpawner(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "tick", at = @At("RETURN"))
	public void tick(CallbackInfo callbackInfo) {
		PlayerCosmeticData cosmeticData = MamyFeature.getCosmeticData((PlayerEntity) (Object) this);
		// if player has cosmetics
		if (cosmeticData != null) {
			// player pet
			String playerPet = cosmeticData.getPet();
			if (ModConfig.shouldDisplayCosmetics() && playerPet != null && MamyFeature.PETS_DATA.containsKey(playerPet)) {
				// do not render in first person or if the player is invisible
				//noinspection ConstantConditions
				if (((ModConfig.cosmetics == ModConfig.CosmeticsOptions.FIRST_PERSON || MinecraftClient.getInstance().gameRenderer.getCamera().isThirdPerson()) || MinecraftClient.getInstance().player != (Object) this) && !this.isInvisible()) {
					if (MamyFeature.PETS_DATA.containsKey(playerPet)) {
						DefaultParticleType overhead = MamyFeature.PETS_DATA.get(playerPet);
						if (this.age % 20 == 0) {
							getWorld().addParticle(overhead, this.getX() + Math.cos(this.bodyYaw / 50) * 0.5, this.getY() + this.getHeight() + 0.5f + Math.sin(this.age / 12f) / 12f, this.getZ() - Math.cos(this.bodyYaw / 50) * 0.5, 0, 0, 0);
						}
					}
				}
			}
		}
	}
}
