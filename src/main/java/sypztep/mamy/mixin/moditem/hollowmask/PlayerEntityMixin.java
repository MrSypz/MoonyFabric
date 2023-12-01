package sypztep.mamy.mixin.moditem.hollowmask;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)

public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo ci) {
//        ItemStack headSlot = this.getEquippedStack(EquipmentSlot.HEAD);
//        boolean hasVulnerabilityEffect = this.hasStatusEffect(VULNERABILITY);
//
//        for (HollowmaskItem mask : ModItems.ALL_MASK) {
//            if (headSlot.isOf(mask)) {
//                if (!hasVulnerabilityEffect) {
//                    int amp = headSlot.isOf(ModItems.VASTO_MASK) ? 4 : headSlot.isOf(ModItems.HOLLOW_MASK_TIER4) ? 3 : headSlot.isOf(ModItems.HOLLOW_MASK_TIER3) ? 2 : headSlot.isOf(ModItems.HOLLOW_MASK_TIER2) ? 1 : 0;
//                    this.addStatusEffect(new StatusEffectInstance(VULNERABILITY, 100, 0, false, false,false));
//                    this.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 100, 0, false, false,false));
//                    this.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 100, 0, false, false,false));
//                    this.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 100, 1, false, false,false));
//                    if (!headSlot.isOf(ModItems.HALF_HOLLOW_MASK))
//                        this.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 100, amp, false, false,false));
//                    if (this.getHungerManager().getFoodLevel() <= 0) {
//                        this.addStatusEffect(new StatusEffectInstance(STIMULATION, 100, 0, false, false,false));
//                    }
//                }
//                break; // Assuming only one mask can be equipped at a time
//            }
//        }
    }

}
