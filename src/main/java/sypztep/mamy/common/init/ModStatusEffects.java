package sypztep.mamy.common.init;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import sypztep.mamy.common.MamyMod;
import sypztep.mamy.common.statuseffects.*;

public class ModStatusEffects {

    public static StatusEffect ELECTRO_MARK = new MarkStatusEffect();

    public static StatusEffect PASSOUT = new EmptyStatusEffect(StatusEffectCategory.HARMFUL, 0);
    public static StatusEffect GRIEVOUSWOUNDS = new EmptyStatusEffect(StatusEffectCategory.HARMFUL,448536);
    public static StatusEffect VULNERABILITY = new EmptyStatusEffect(StatusEffectCategory.HARMFUL, 0xFF891C);
    public static StatusEffect NUMBNESS =new NumbnessStatusEffect();
    public static StatusEffect TORPOR = new EmptyStatusEffect(StatusEffectCategory.HARMFUL, 0xD8C0B8);
    public static StatusEffect BATRACHOTOXIN = new EmptyStatusEffect(StatusEffectCategory.HARMFUL, 0xEAD040);
    public static StatusEffect STIMULATION = new EmptyStatusEffect(StatusEffectCategory.HARMFUL, 0xD85252).addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, "91AEAA56-376B-4498-935B-2F7F68070635", 0.2f, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
    public static StatusEffect LETHALTEMPO = new TempoStatusEffect();
    public static StatusEffect FERVOR_OF_BATTLE = new TempoStatusEffect().addAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE,"28E38CA1-61D3-4E65-98CD-D15DF2E33CF3",1.0D, EntityAttributeModifier.Operation.ADDITION);

    public static void init() {
        init("electromark", ELECTRO_MARK);
        init("passout", PASSOUT);
        init("grievouswounds", GRIEVOUSWOUNDS);
        init("vulnerability", VULNERABILITY);
        init("numbness", NUMBNESS);
        init("torpor", TORPOR);
        init("batrachotoxin", BATRACHOTOXIN);
        init("stimulation", STIMULATION);
        init("lethaltempo", LETHALTEMPO);
        init("fervorofbattle", FERVOR_OF_BATTLE);
    }
    public static void init(String name,StatusEffect statusEffect){
        Registry.register(Registries.STATUS_EFFECT,MamyMod.id(name), statusEffect);
    }
}
