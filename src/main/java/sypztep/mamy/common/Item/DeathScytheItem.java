package sypztep.mamy.common.Item;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterials;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import sypztep.mamy.common.component.entity.VizardComponent;
import sypztep.mamy.common.init.ModDamageTypes;
import sypztep.mamy.common.init.ModItems;
import sypztep.mamy.common.init.ModParticles;
import sypztep.mamy.common.init.ModSoundEvents;

import java.util.List;

public class DeathScytheItem extends EmptySwordItem implements CustomHitSoundItem, CustomHitParticleItem{
    public DeathScytheItem() {
        super(ToolMaterials.NETHERITE,6, -3f, new Settings().fireproof());
    }
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        MinecraftClient client = MinecraftClient.getInstance();
        if (user != null && user.isSneaking()) {
            boolean itemToCheck = VizardComponent.hasAnyMask(user);
            ItemStack getHeadSlot = user.getEquippedStack(EquipmentSlot.HEAD);
            if (getHeadSlot.isEmpty())
                equipMask(user);
            else {
                if (!itemToCheck) {
                    int emptySlot = user.getInventory().getEmptySlot();
                    if (emptySlot >= 0)
                        user.getInventory().setStack(emptySlot, getHeadSlot);
                    else
                        user.dropItem(getHeadSlot, false);
                    equipMask(user);
                } else
                    client.player.sendMessage(Text.of("Your head slot is already occupied with the mod item!"), false);
            }
        }
        return TypedActionResult.pass(itemStack);
    }

    public static void ShockWaveDamage(PlayerEntity user) {
        double damageRadiusSquared = 5.0d;
        List<LivingEntity> entities = user.getWorld().getNonSpectatingEntities(LivingEntity.class, user.getBoundingBox().expand(damageRadiusSquared));

        for (LivingEntity target : entities) {
            if (target != user) {
                double distanceToEntity = target.squaredDistanceTo(user.getX(), user.getY(), user.getZ());
                double normalizedDistance = Math.sqrt(distanceToEntity) / damageRadiusSquared; // Adjust as needed for your range
                float damage = (float) (damageRadiusSquared - (float) (normalizedDistance * (damageRadiusSquared - 1.0f)));
                target.damage(target.getWorld().getDamageSources().create(ModDamageTypes.MASKIMPACT,user), damage);
            }
        }
    }
    private void equipMask(PlayerEntity user) {
        Item item = ModItems.DEATH_SCYTHE;
        ItemStack Hollowmask = new ItemStack(ModItems.HOLLOW_MASK_TIER3);
        Hollowmask.addEnchantment(Enchantments.BINDING_CURSE, 1);
        Hollowmask.addEnchantment(Enchantments.VANISHING_CURSE, 1);
        Hollowmask.addHideFlag(ItemStack.TooltipSection.ENCHANTMENTS);
        user.equipStack(EquipmentSlot.HEAD, Hollowmask);
        HollowmaskItem.useMaskParticle(user);
        ShockWaveDamage(user);
        user.damage(user.getWorld().getDamageSources().create(ModDamageTypes.MASKIMPACT,user), user.getHealth() * 0.5f);
        user.getItemCooldownManager().set(item, 60); // 3 min
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        super.appendTooltip(stack, world, tooltip, context);
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
}