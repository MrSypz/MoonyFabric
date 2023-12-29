package sypztep.mamy.common.Item;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import sypztep.mamy.common.entity.projectile.BloodLustEntity;
import sypztep.mamy.common.init.*;
import sypztep.mamy.common.util.EnchantmentUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class BloodlustItem extends EmptySwordItem implements CustomHitSoundItem, CustomHitParticleItem {
    private static final EntityAttributeModifier REACH_MODIFIER;
    private static final EntityAttributeModifier CRIT_DAMAGE_MODIFIER;
    public BloodlustItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (EnchantmentHelper.getEquipmentLevel(ModEnchantments.VENGEANCE, user) <= 0)
            return super.use(world, user, hand);
         else {
            float f = 1.0F;
            if (!world.isClient && user instanceof LivingEntity) {
                BloodLustEntity bloodLust = new BloodLustEntity(world, user);
                bloodLust.setOwner(user);
                bloodLust.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, f * 3.0F, 0.0F);
                bloodLust.setDamage(bloodLust.getDamage());
                user.getStackInHand(hand).damage(1, user, (p) -> {
                    p.sendToolBreakStatus(hand);
                });
                bloodLust.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
                ArrayList<StatusEffectInstance> statusEffectsHalved = new ArrayList();
                float absorption = user.getAbsorptionAmount();
                Iterator var8 = user.getStatusEffects().iterator();

                StatusEffectInstance statusEffectInstance;
                while (var8.hasNext()) {
                    statusEffectInstance = (StatusEffectInstance) var8.next();
                    StatusEffectInstance statusHalved = new StatusEffectInstance(statusEffectInstance.getEffectType(), statusEffectInstance.getDuration() / 2, statusEffectInstance.getAmplifier(), statusEffectInstance.isAmbient(), statusEffectInstance.shouldShowParticles(), statusEffectInstance.shouldShowIcon());
                    bloodLust.addEffect(statusHalved);
                    statusEffectsHalved.add(statusHalved);
                }

                user.clearStatusEffects();
                var8 = statusEffectsHalved.iterator();

                while (var8.hasNext()) {
                    statusEffectInstance = (StatusEffectInstance) var8.next();
                    user.addStatusEffect(statusEffectInstance);
                }
                user.setAbsorptionAmount(absorption);
                user.damage(world.getDamageSources().create(ModDamageTypes.BLEEDOUT), 3F);
                user.getItemCooldownManager().set(this, 30);
                world.spawnEntity(bloodLust);
            }
            world.playSound(null, user.getX(), user.getY(), user.getZ(), ModSoundEvents.ITEM_SPEWING, SoundCategory.PLAYERS, 1.0F, 1.0F);
            return TypedActionResult.success(user.getStackInHand(hand));
        }
    }
    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        Multimap<EntityAttribute, EntityAttributeModifier> map = LinkedHashMultimap.create(super.getAttributeModifiers(slot));
        if (slot == EquipmentSlot.MAINHAND) {
            map.put(ReachEntityAttributes.ATTACK_RANGE, REACH_MODIFIER);
            map.put(ModEntityAttributes.GENERIC_CRIT_DAMAGE, CRIT_DAMAGE_MODIFIER);
        }
        return map;
    }
    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (entity.age % 12 == 0 &&  (entity instanceof PlayerEntity player) && world.isClient) {
            if (EnchantmentUtil.hasEnchantment(ModEnchantments.VENGEANCE,stack)) {
                if (player.getEquippedStack(EquipmentSlot.MAINHAND) == stack || player.getEquippedStack(EquipmentSlot.OFFHAND) == stack) {
                    float randomx = (float) (Math.random() * 6);
                    float randomz = (float) (Math.random() * 6);

                    world.addParticle(ModParticles.BLOOD_BUBBLE,
                            player.getX() + player.getHandPosOffset(this).getX(),
                            player.getY() + player.getHandPosOffset(this).getY() + 1.2,
                            player.getZ() + player.getHandPosOffset(this).getZ(),
                            1 * randomx, -5, -1 * randomz);
                }
            }
        }
    }
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        itemdesc(tooltip);
        super.appendTooltip(stack, world, tooltip, context);
    }
    private void itemdesc(List<Text> list) {
        Item item = this; // Assuming this is the current item
        String registryName = item.getTranslationKey();

        MutableText passive = (Text.translatable(registryName + ".desc.passive")).formatted(Formatting.GRAY);
        MutableText info = (Text.translatable(registryName + ".desc").formatted(Formatting.GRAY));
        list.add(Text.literal(" - ").append((Text.literal("Passive : ").append(passive).formatted(Formatting.GOLD))).formatted(Formatting.GRAY));
        list.add(Text.literal(" - ").append((Text.literal("Ability : Right Click").formatted(Formatting.GOLD))).formatted(Formatting.GRAY));
        list.add((info.formatted(Formatting.GRAY)).append(((Text.literal(" 3"))).append(Text.literal(" ♥")).formatted(Formatting.RED)));
    }


    @Override
    public void playHitSound(PlayerEntity user) {
        user.playSound(ModSoundEvents.ENTITY_PLAYER_ATTACK_SCYTHE, 1.0F, (float)(1.0 + user.getRandom().nextGaussian() / 10.0));
    }
    @Override
    public void spawnHitParticles(PlayerEntity user) {
        double d0 = -MathHelper.sin(user.getYaw() * 0.017453292F);
        double d1 = MathHelper.cos(user.getYaw() * 0.017453292F);
        World var7 = user.getWorld();
        if (var7 instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(ModParticles.RED_SWEEP_ATTACK_PARTICLE, user.getX() + d0, user.getBodyY(0.5), user.getZ() + d1, 0, d0, 0.0, d1, 0.0);
        }
    }
    static {
        REACH_MODIFIER = new EntityAttributeModifier(UUID.fromString("911af262-067d-4da2-854c-20f03cc2dd8b"), "Weapon modifier", 0.5D, EntityAttributeModifier.Operation.ADDITION);
        CRIT_DAMAGE_MODIFIER = new EntityAttributeModifier(UUID.fromString("4cdc0e38-c037-42bf-864f-5c745ddf0b61"), "Weapon modifier", 15.0D, EntityAttributeModifier.Operation.ADDITION);
    }
}
