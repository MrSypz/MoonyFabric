package sypztep.mamy.common;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class EmptyStatusEffect extends StatusEffect {
    public EmptyStatusEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }
}