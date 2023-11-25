package sypztep.mamy.mixin.moditem.hollowmask;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sypztep.mamy.common.Item.HollowmaskItem;
import sypztep.mamy.common.init.ModItems;

import static sypztep.pickyourpoison.common.init.ModStatusEffects.STIMULATION;
import static sypztep.pickyourpoison.common.init.ModStatusEffects.VULNERABILITY;

@Mixin(PlayerEntity.class)

public abstract class PlayerEntityMixin extends LivingEntity {
    @Shadow public abstract HungerManager getHungerManager();

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo ci) {
        ItemStack headSlot = this.getEquippedStack(EquipmentSlot.HEAD);
        boolean hasVulnerabilityEffect = this.hasStatusEffect(VULNERABILITY);

        for (HollowmaskItem mask : ModItems.ALL_MASK) {
            if (headSlot.isOf(mask)) {
                if (!hasVulnerabilityEffect) {
                    int vulnerabilityLevel = headSlot.isOf(ModItems.HOLLOW_MASK_TIER3) ? 0 : 1;
                    this.addStatusEffect(new StatusEffectInstance(VULNERABILITY, 100, vulnerabilityLevel, false, false));
                    this.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 100, 0, false, false, false));

                    if (headSlot.isOf(ModItems.HOLLOW_MASK_TIER3) && this.getHungerManager().getFoodLevel() <= 0) {
                        this.addStatusEffect(new StatusEffectInstance(STIMULATION, 100, 0, false, false));
                    }
                }
                break; // Assuming only one mask can be equipped at a time
            }
        }
    }
}
