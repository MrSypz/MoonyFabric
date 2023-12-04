package sypztep.mamy.common.init;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import sypztep.mamy.common.MamyMod;
import sypztep.mamy.common.statuseffects.EmptyStatusEffect;
import sypztep.mamy.common.statuseffects.MarkStatusEffect;
import sypztep.mamy.common.statuseffects.TempoStatusEffect;

public class ModStatusEffects {

    public static StatusEffect ELECTRO_MARK = new MarkStatusEffect();
    public static StatusEffect GRIEVOUSWOUNDS = new EmptyStatusEffect(StatusEffectCategory.HARMFUL,448536);
    public static StatusEffect CARVE = new EmptyStatusEffect(StatusEffectCategory.HARMFUL,0).addAttributeModifier(EntityAttributes.GENERIC_ARMOR,"9f66ca04-e8c5-4225-952c-665ccb332fe7",-0.05D, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
    public static StatusEffect LETHALTEMPO = new TempoStatusEffect();
    public static StatusEffect FERVOR_OF_BATTLE = new TempoStatusEffect().addAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE,"28E38CA1-61D3-4E65-98CD-D15DF2E33CF3",1.0D, EntityAttributeModifier.Operation.ADDITION);
    public static StatusEffect HOLLOW_POWER = new EmptyStatusEffect(StatusEffectCategory.BENEFICIAL,0).addAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE,"52f38a34-b1a7-4a65-928e-8a30608b7432",0.08D, EntityAttributeModifier.Operation.MULTIPLY_BASE)
            .addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED,"90c7a8a9-2b36-43ce-a28d-08d29c1ebead",0.02D, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);

    public static void init() {
        init("electromark", ELECTRO_MARK);
        init("grievouswounds", GRIEVOUSWOUNDS);
        init("carve", CARVE);
        init("lethaltempo", LETHALTEMPO);
        init("fervorofbattle", FERVOR_OF_BATTLE);
        init("hollowpower", HOLLOW_POWER);
    }
    public static void init(String name,StatusEffect statusEffect){
        Registry.register(Registries.STATUS_EFFECT,MamyMod.id(name), statusEffect);
    }
}
