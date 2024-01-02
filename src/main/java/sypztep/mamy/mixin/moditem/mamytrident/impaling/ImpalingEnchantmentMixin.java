package sypztep.mamy.mixin.moditem.mamytrident.impaling;

import net.minecraft.enchantment.ImpalingEnchantment;
import net.minecraft.entity.EntityGroup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ImpalingEnchantment.class)
public abstract class ImpalingEnchantmentMixin {
    /**
     * @author Sypztep
     * @reason To remove vanilla damage function
     */
    @Overwrite
    public float getAttackDamage(int level, EntityGroup group) {
        return 0.0F;
    }
}
