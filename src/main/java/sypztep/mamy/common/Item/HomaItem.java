package sypztep.mamy.common.Item;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import sypztep.mamy.common.init.ModDamageTypes;
import sypztep.mamy.common.init.ModItems;

public class HomaItem extends MamyTridentItem {
    public HomaItem(Settings settings) {
        super(settings);
    }
    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        target.setOnFireFor(4 + attacker.getRandom().nextInt(4));
        return super.postHit(stack, target, attacker);
    }
    @Override
    protected boolean canRiptide(PlayerEntity playerEntity) {
        return playerEntity.isInLava() || playerEntity.isOnFire() || playerEntity.getMainHandStack().getItem() == ModItems.HOMA;
    }
    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        super.onStoppedUsing(stack, world, user, remainingUseTicks);

        if (user instanceof PlayerEntity player && user.isUsingRiptide() && stack.getItem() == ModItems.HOMA) {
            if (player.experienceLevel <= 0) {
                user.damage(world.getDamageSources().create(ModDamageTypes.CINNABAR), 2F);
                user.playSound(SoundEvents.ENTITY_PLAYER_HURT, 1.0f, 1.0f);
            } else {
                player.addExperienceLevels(-1);
            }
            if (world instanceof ServerWorld serverWorld) {
                serverWorld.spawnParticles(ParticleTypes.SOUL, user.getX(), user.getY(), user.getZ(), 20, user.getRandom().nextFloat(), user.getRandom().nextGaussian(), user.getRandom().nextFloat(), user.getRandom().nextFloat() / 10f);
            }
            user.playSound(SoundEvents.PARTICLE_SOUL_ESCAPE, 5.0f, 1.0f);
        }
    }
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (itemStack.getDamage() >= itemStack.getMaxDamage() - 1) {
            return TypedActionResult.fail(itemStack);
        } else if (EnchantmentHelper.getRiptide(itemStack) > 0 && !user.isInLava() && !user.isOnFire() && !(itemStack.hasNbt() && itemStack.getItem() == ModItems.HOMA)) {
            return TypedActionResult.fail(itemStack);
        } else {
            user.setCurrentHand(hand);
            return TypedActionResult.consume(itemStack);
        }
    }
}
