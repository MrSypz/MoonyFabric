package sypztep.mamy.common.init;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import sypztep.mamy.common.MamyMod;
import sypztep.mamy.common.component.entity.VizardComponent;

public class ModEntityComponents implements EntityComponentInitializer {

	//sword
	public static final ComponentKey<VizardComponent> VIZARD = ComponentRegistry.getOrCreate(MamyMod.id("vizard"), VizardComponent.class);


	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.registerForPlayers(VIZARD, VizardComponent::new, RespawnCopyStrategy.LOSSLESS_ONLY);
	}
}
