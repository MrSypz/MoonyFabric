package sypztep.mamy.common.Item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import sypztep.mamy.common.init.ModParticles;

public class MamySwordItem extends SwordItem {
    public MamySwordItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof PlayerEntity) {
            this.spawnSweepParticles((PlayerEntity) attacker, ModParticles.RED_SWEEP_ATTACK_PARTICLE);
            SoundEvent sound = SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP;
            attacker.getWorld().playSound(null, target.getX(), target.getY(), target.getZ(), sound, attacker.getSoundCategory(), 1.0F, 1.0F);
        }
        return super.postHit(stack, target, attacker);
    }
    private void spawnSweepParticles(PlayerEntity player, DefaultParticleType type) {
        double d0 = -MathHelper.sin(player.getYaw() * 0.017453292F);
        double d1 = MathHelper.cos(player.getYaw() * 0.017453292F);
        if (player.getWorld() instanceof ServerWorld) {
            ((ServerWorld) player.getWorld()).spawnParticles(type, player.getX() + d0, player.getBodyY(0.5), player.getZ() + d1, 0, d0, 0.0, d1, 0.0);
        }
    }
}
