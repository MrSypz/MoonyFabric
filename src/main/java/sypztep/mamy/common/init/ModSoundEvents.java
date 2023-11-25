package sypztep.mamy.common.init;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import sypztep.mamy.common.MamyMod;

public class ModSoundEvents {
	//Sound
	public static final SoundEvent ENTITY_GENERIC_BLOODHIT = SoundEvent.of(MamyMod.id("entity.generic.bloodhit"));
	public static final SoundEvent ENTITY_POISON_DART_FROG_AMBIENT = SoundEvent.of(MamyMod.id("entity.poison_dart_frog.ambient"));
	public static final SoundEvent ENTITY_POISON_DART_FROG_HURT = SoundEvent.of(MamyMod.id("entity.poison_dart_frog.hurt"));
	public static final SoundEvent ENTITY_POISON_DART_FROG_DEATH =SoundEvent.of(MamyMod.id("entity.poison_dart_frog.death"));
	public static final SoundEvent ENTITY_POISON_DART_HIT = SoundEvent.of(MamyMod.id("entity.poison_dart.hit"));
	public static final SoundEvent ENTITY_GENERIC_SONIDO = SoundEvent.of(MamyMod.id("entity.generic.sonido"));

	public static final SoundEvent ITEM_POISON_DART_FROG_BOWL_FILL = SoundEvent.of(MamyMod.id("item.poison_dart_frog_bowl.fill"));
	public static final SoundEvent ITEM_POISON_DART_FROG_BOWL_EMPTY = SoundEvent.of(MamyMod.id("item.poison_dart_frog_bowl.empty"));
	public static final SoundEvent ITEM_POISON_DART_FROG_BOWL_LICK =SoundEvent.of(MamyMod.id("item.poison_dart_frog_bowl.lick"));
	public static final SoundEvent ITEM_POISON_DART_COAT = SoundEvent.of(MamyMod.id("item.poison_dart.coat"));
	public static final SoundEvent ITEM_POISON_DART_THROW = SoundEvent.of(MamyMod.id("item.poison_dart.throw"));
	public static final SoundEvent ITEM_SPEWING = SoundEvent.of(MamyMod.id("item.spewing"));

	public static void init() {
		Registry.register(Registries.SOUND_EVENT, ENTITY_GENERIC_BLOODHIT.getId(), ENTITY_GENERIC_BLOODHIT);
		Registry.register(Registries.SOUND_EVENT, ENTITY_POISON_DART_FROG_AMBIENT.getId(), ENTITY_POISON_DART_FROG_AMBIENT);
		Registry.register(Registries.SOUND_EVENT, ENTITY_POISON_DART_FROG_HURT.getId(), ENTITY_POISON_DART_FROG_HURT);
		Registry.register(Registries.SOUND_EVENT, ENTITY_POISON_DART_FROG_DEATH.getId(), ENTITY_POISON_DART_FROG_DEATH);
		Registry.register(Registries.SOUND_EVENT, ENTITY_POISON_DART_HIT.getId(), ENTITY_POISON_DART_HIT);
		Registry.register(Registries.SOUND_EVENT, ENTITY_GENERIC_SONIDO.getId(), ENTITY_GENERIC_SONIDO);

		Registry.register(Registries.SOUND_EVENT, ITEM_POISON_DART_FROG_BOWL_FILL.getId(), ITEM_POISON_DART_FROG_BOWL_FILL);
		Registry.register(Registries.SOUND_EVENT, ITEM_POISON_DART_FROG_BOWL_EMPTY.getId(), ITEM_POISON_DART_FROG_BOWL_EMPTY);
		Registry.register(Registries.SOUND_EVENT, ITEM_POISON_DART_FROG_BOWL_LICK.getId(), ITEM_POISON_DART_FROG_BOWL_LICK);
		Registry.register(Registries.SOUND_EVENT, ITEM_POISON_DART_COAT.getId(), ITEM_POISON_DART_COAT);
		Registry.register(Registries.SOUND_EVENT, ITEM_POISON_DART_THROW.getId(), ITEM_POISON_DART_THROW);
		Registry.register(Registries.SOUND_EVENT, ITEM_SPEWING.getId(), ITEM_SPEWING);
	}
}
