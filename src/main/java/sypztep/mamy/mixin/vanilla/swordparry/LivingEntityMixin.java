package sypztep.mamy.mixin.vanilla.swordparry;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sypztep.mamy.common.ModConfig;


@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    @Shadow protected ItemStack activeItemStack;
    @Shadow public abstract boolean isUsingItem();
    @Shadow public abstract boolean blockedByShield(DamageSource source);

    private DamageSource mamy$cachedSource;
    private boolean mamy$appearBlocking = false;

    @Inject(at = @At(value = "HEAD"), method = "isBlocking", cancellable = true)
    public void parry$fakeShieldBlocking(CallbackInfoReturnable<Boolean> cir) {
        var item = this.activeItemStack.getItem();
        if(item instanceof SwordItem) {
            cir.setReturnValue(mamy$appearBlocking);
        }
    }

    @Inject(at = @At(value = "HEAD"), method = "damage", cancellable = true)
    public void parry$cacheDamageSource(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        this.mamy$cachedSource = source;
    }

    @ModifyVariable(method = "damage", at = @At("HEAD"), index = 2)
    private float parry$applySwordBlockProtection(float old) {
        var item = this.activeItemStack.getItem();
        mamy$appearBlocking = true;
        if(item instanceof SwordItem && this.isUsingItem() && this.blockedByShield(mamy$cachedSource)) {
            double multiplier = ModConfig.default_multiplier;
            System.out.println(old +" into "+old * multiplier);
            old *= multiplier;
        }
        mamy$appearBlocking = false;
        return old;
    }

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }
}
