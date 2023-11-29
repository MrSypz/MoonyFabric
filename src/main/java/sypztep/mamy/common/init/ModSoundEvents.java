package sypztep.mamy.common.init;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import sypztep.mamy.common.MamyMod;

import java.util.LinkedHashMap;
import java.util.Map;

public interface ModSoundEvents {
	Map<SoundEvent, Identifier> SOUND_EVENTS = new LinkedHashMap();
	//Sound
    SoundEvent ENTITY_GENERIC_BLOODHIT = createSoundEvent("entity.generic.bloodhit");
	SoundEvent ENTITY_GENERIC_SONIDO = createSoundEvent("entity.generic.sonido");
	SoundEvent ENTITY_PLAYER_ATTACK_SCYTHE = createSoundEvent("entity.player.attack.scythe");

	SoundEvent ITEM_SPEWING =createSoundEvent("item.spewing");

	static void init() {
		SOUND_EVENTS.keySet().forEach((soundEvent) -> {
			Registry.register(Registries.SOUND_EVENT, SOUND_EVENTS.get(soundEvent), soundEvent);
		});
	}
	private static SoundEvent createSoundEvent(String path) {
		SoundEvent soundEvent = SoundEvent.of(MamyMod.id(path));
		SOUND_EVENTS.put(soundEvent, MamyMod.id(path));
		return soundEvent;
	}
}
