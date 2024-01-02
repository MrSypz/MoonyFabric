package sypztep.mamy.common.Item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Property;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import sypztep.mamy.common.init.ModDamageTypes;
import sypztep.mamy.common.init.ModEntityAttributes;
import sypztep.mamy.common.init.ModItems;
import sypztep.mamy.common.init.ModParticles;

import java.util.UUID;

public class HomaItem extends MamyTridentItem implements CustomHitParticleItem,CustomHitSoundItem{
    private static final EntityAttributeModifier CRIT_DAMAGE_MODIFIER;
    private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;
    public HomaItem(Settings settings) {
        super(settings);
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        //Override normal one
        builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Tool modifier", 12.0, EntityAttributeModifier.Operation.ADDITION));
        builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Tool modifier", -2.9f, EntityAttributeModifier.Operation.ADDITION));
        // For critical damage
        builder.put(ModEntityAttributes.GENERIC_CRIT_DAMAGE,CRIT_DAMAGE_MODIFIER);
        this.attributeModifiers = builder.build();
    }
    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        target.setOnFireFor(8 + attacker.getRandom().nextInt(8));
        return super.postHit(stack, target, attacker);
    }
    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        if (slot == EquipmentSlot.MAINHAND) {
            return this.attributeModifiers;
        }
        return super.getAttributeModifiers(slot);
    }

    @Override
    protected boolean canRiptide(PlayerEntity playerEntity) {
        return playerEntity.isInLava() || playerEntity.isOnFire() || playerEntity.getMainHandStack().getItem() == ModItems.HOMASOUL || playerEntity.getOffHandStack().getItem() == ModItems.HOMASOUL;
    }
    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        super.onStoppedUsing(stack, world, user, remainingUseTicks);

        if (user instanceof PlayerEntity player && user.isUsingRiptide() && stack.getItem() == ModItems.HOMASOUL) {
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
        } else if (EnchantmentHelper.getRiptide(itemStack) > 0 && !user.isInLava() && !user.isOnFire() && !(itemStack.hasNbt() && itemStack.getItem() == ModItems.HOMASOUL)) {
            return TypedActionResult.fail(itemStack);
        } else {
            user.setCurrentHand(hand);
            return TypedActionResult.consume(itemStack);
        }
    }
    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if ((context.getWorld().getBlockState(context.getBlockPos()).getBlock() == Blocks.SOUL_CAMPFIRE || context.getWorld().getBlockState(context.getBlockPos()).getBlock() == Blocks.SOUL_LANTERN || context.getWorld().getBlockState(context.getBlockPos()).getBlock() == Blocks.SOUL_TORCH || context.getWorld().getBlockState(context.getBlockPos()).getBlock() == Blocks.SOUL_WALL_TORCH) && context.getStack().getItem() == ModItems.HOMA) {
            ItemStack homa_soul = new ItemStack(ModItems.HOMASOUL, context.getStack().getCount());
            homa_soul.setNbt(context.getStack().getNbt());
            context.getWorld().playSound(context.getPlayer().getX(), context.getPlayer().getY(), context.getPlayer().getZ(), SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.PLAYERS, 1.0f, 1.0f, false);
            context.getWorld().playSound(context.getPlayer().getX(), context.getPlayer().getY(), context.getPlayer().getZ(), SoundEvents.PARTICLE_SOUL_ESCAPE, SoundCategory.PLAYERS, 1.0f, 1.0f, false);
            context.getWorld().playSound(context.getPlayer().getX(), context.getPlayer().getY(), context.getPlayer().getZ(), SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.PLAYERS, 1.0f, 1.0f, false);

            context.getPlayer().setStackInHand(context.getHand(), homa_soul);

            BlockState blockState = context.getWorld().getBlockState(context.getBlockPos());
            BlockState replacedBlockState = blockState;
            if (context.getWorld().getBlockState(context.getBlockPos()).getBlock() == Blocks.SOUL_CAMPFIRE) {
                replacedBlockState = Blocks.CAMPFIRE.getDefaultState();
            } else if (context.getWorld().getBlockState(context.getBlockPos()).getBlock() == Blocks.SOUL_LANTERN) {
                replacedBlockState = Blocks.LANTERN.getDefaultState();
            } else if (context.getWorld().getBlockState(context.getBlockPos()).getBlock() == Blocks.SOUL_TORCH) {
                replacedBlockState = Blocks.TORCH.getDefaultState();
            } else if (context.getWorld().getBlockState(context.getBlockPos()).getBlock() == Blocks.SOUL_WALL_TORCH) {
                replacedBlockState = Blocks.WALL_TORCH.getDefaultState();
            }
            for (Property property : context.getWorld().getBlockState(context.getBlockPos()).getProperties()) {
                if (replacedBlockState.getProperties().contains(property)) {
                    replacedBlockState = replacedBlockState.with(property, blockState.get(property));
                }
            }
            context.getWorld().setBlockState(context.getBlockPos(), replacedBlockState);

            for (int i = 0; i < 20; i++) {
                context.getWorld().addParticle(ParticleTypes.SOUL, context.getBlockPos().getX() + .5 + context.getWorld().getRandom().nextGaussian() / 10, context.getBlockPos().getY() + .5 + context.getWorld().getRandom().nextGaussian() / 10, context.getBlockPos().getZ() + .5 + context.getWorld().getRandom().nextGaussian() / 10, 0, context.getWorld().getRandom().nextFloat() / 10, 0);
            }
            return ActionResult.SUCCESS;
        } else if ((context.getWorld().getBlockState(context.getBlockPos()).getBlock() == Blocks.CAMPFIRE || context.getWorld().getBlockState(context.getBlockPos()).getBlock() == Blocks.LANTERN || context.getWorld().getBlockState(context.getBlockPos()).getBlock() == Blocks.TORCH || context.getWorld().getBlockState(context.getBlockPos()).getBlock() == Blocks.WALL_TORCH) && context.getStack().getItem() == ModItems.HOMASOUL) {
            ItemStack homa = new ItemStack(ModItems.HOMA, context.getStack().getCount());
            homa.setNbt(context.getStack().getNbt());
            context.getWorld().playSound(context.getPlayer().getX(), context.getPlayer().getY(), context.getPlayer().getZ(), SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.PLAYERS, 1.0f, 1.0f, false);
            context.getWorld().playSound(context.getPlayer().getX(), context.getPlayer().getY(), context.getPlayer().getZ(), SoundEvents.PARTICLE_SOUL_ESCAPE, SoundCategory.PLAYERS, 1.0f, 0.8f, false);
            context.getWorld().playSound(context.getPlayer().getX(), context.getPlayer().getY(), context.getPlayer().getZ(), SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.PLAYERS, 1.0f, 0.8f, false);

            context.getPlayer().setStackInHand(context.getHand(), homa);

            BlockState blockState = context.getWorld().getBlockState(context.getBlockPos());
            BlockState replacedBlockState = blockState;
            if (context.getWorld().getBlockState(context.getBlockPos()).getBlock() == Blocks.CAMPFIRE) {
                replacedBlockState = Blocks.SOUL_CAMPFIRE.getDefaultState();
            } else if (context.getWorld().getBlockState(context.getBlockPos()).getBlock() == Blocks.LANTERN) {
                replacedBlockState = Blocks.SOUL_LANTERN.getDefaultState();
            } else if (context.getWorld().getBlockState(context.getBlockPos()).getBlock() == Blocks.TORCH) {
                replacedBlockState = Blocks.SOUL_TORCH.getDefaultState();
            } else if (context.getWorld().getBlockState(context.getBlockPos()).getBlock() == Blocks.WALL_TORCH) {
                replacedBlockState = Blocks.SOUL_WALL_TORCH.getDefaultState();
            }
            for (Property property : context.getWorld().getBlockState(context.getBlockPos()).getProperties()) {
                if (replacedBlockState.getProperties().contains(property)) {
                    replacedBlockState = replacedBlockState.with(property, blockState.get(property));
                }
            }
            context.getWorld().setBlockState(context.getBlockPos(), replacedBlockState);

            for (int i = 0; i < 20; i++) {
                context.getWorld().addParticle(ParticleTypes.SOUL_FIRE_FLAME, context.getBlockPos().getX() + .5 + context.getWorld().getRandom().nextGaussian() / 10, context.getBlockPos().getY() + .5 + context.getWorld().getRandom().nextGaussian() / 10, context.getBlockPos().getZ() + .5 + context.getWorld().getRandom().nextGaussian() / 10, 0, context.getWorld().getRandom().nextFloat() / 10, 0);
            }
            return ActionResult.SUCCESS;
        }

        return super.useOnBlock(context);
    }


    @Override
    public void spawnHitParticles(PlayerEntity user) {
        double d0 = -MathHelper.sin(user.getYaw() * 0.017453292F);
        double d1 = MathHelper.cos(user.getYaw() * 0.017453292F);
        World world = user.getWorld();
        if (world instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(ModParticles.FIRE_SWEEP_ATTACK_PARTICLE, user.getX() + d0, user.getBodyY(0.5), user.getZ() + d1, 0, d0, 0.0, d1, 0.0);
        }
    }

    @Override
    public void playHitSound(PlayerEntity user) {

    }
    static {
        CRIT_DAMAGE_MODIFIER = new EntityAttributeModifier(UUID.fromString("4cdc0e38-c037-42bf-864f-5c745ddf0a62"), "Weapon modifier", 50.0D, EntityAttributeModifier.Operation.ADDITION);
    }
}
