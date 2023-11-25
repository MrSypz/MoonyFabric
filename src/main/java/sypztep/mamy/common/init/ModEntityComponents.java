package sypztep.mamy.common.init;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.minecraft.entity.LivingEntity;
import sypztep.mamy.common.MamyMod;
import sypztep.mamy.common.component.entity.BringerComponent;
import sypztep.mamy.common.component.entity.NumbnessComponent;
import sypztep.mamy.common.component.entity.VizardComponent;

public class ModEntityComponents implements EntityComponentInitializer {

	//sword
	public static final ComponentKey<BringerComponent> BRINGER = ComponentRegistry.getOrCreate(MamyMod.id("bringer"), BringerComponent.class);
	public static final ComponentKey<NumbnessComponent> NUMBNESS_DAMAGE = ComponentRegistry.getOrCreate(MamyMod.id("numbnessdamage"), NumbnessComponent.class);
	//helmet
	public static final ComponentKey<VizardComponent> VIZARD = ComponentRegistry.getOrCreate(MamyMod.id("vizard"), VizardComponent.class);

	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.registerForPlayers(BRINGER, BringerComponent::new, RespawnCopyStrategy.LOSSLESS_ONLY);
		registry.registerForPlayers(VIZARD, VizardComponent::new, RespawnCopyStrategy.LOSSLESS_ONLY);
		registry.registerFor(LivingEntity.class, NUMBNESS_DAMAGE, NumbnessComponent::new);
	}
}
