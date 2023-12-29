package sypztep.mamy.mixin.util.accesor;

import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TridentEntity.class)
public interface TridentEntityAccessor {
    @Accessor("LOYALTY")
    static TrackedData<Byte> mamy$getLoyalty() {
        return null;
    }

    @Accessor("ENCHANTED")
    static TrackedData<Boolean> mamy$getEnchanted() {
        return null;
    }

    @Accessor("tridentStack")
    ItemStack mamy$getTridentStack();

    @Accessor("tridentStack")
    void mamy$setTridentStack(ItemStack stack);

    @Accessor("dealtDamage")
    boolean mamy$hasDealtDamage();

    @Accessor("dealtDamage")
    void mamy$setDealtDamage(boolean dealtDamage);
}
