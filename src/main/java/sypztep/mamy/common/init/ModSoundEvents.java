package sypztep.mamy.common.init;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import sypztep.mamy.common.MamyMod;

public class ModSoundEvents {
	//Sound
	public static final SoundEvent ENTITY_GENERIC_BLOODHIT = SoundEvent.of(MamyMod.id("entity.generic.bloodhit"));
	public static final SoundEvent ENTITY_GENERIC_SONIDO = SoundEvent.of(MamyMod.id("entity.generic.sonido"));

	public static final SoundEvent ITEM_SPEWING = SoundEvent.of(MamyMod.id("item.spewing"));

	public static void init() {
		Registry.register(Registries.SOUND_EVENT, ENTITY_GENERIC_BLOODHIT.getId(), ENTITY_GENERIC_BLOODHIT);
		Registry.register(Registries.SOUND_EVENT, ENTITY_GENERIC_SONIDO.getId(), ENTITY_GENERIC_SONIDO);

		Registry.register(Registries.SOUND_EVENT, ITEM_SPEWING.getId(), ITEM_SPEWING);
	}
}
