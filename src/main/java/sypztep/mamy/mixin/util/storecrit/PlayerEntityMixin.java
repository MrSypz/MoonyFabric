package sypztep.mamy.mixin.util.storecrit;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntityMixin {
    PlayerEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @ModifyVariable(method = "attack", at = @At("STORE"), ordinal = 2)
    private boolean attack$bl3(boolean bl3) {
        boolean bl = this.mamy$isCritical();
        if (bl) {
            bl3 = true;
        } else if (bl3) {
            this.mamy$setCritical(true);
        }

        return bl3;
    }
}
