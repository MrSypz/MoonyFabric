package sypztep.mamy.common.Item;

import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterials;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import sypztep.mamy.common.component.entity.VizardComponent;
import sypztep.mamy.common.init.ModItems;

import java.util.List;

public class DeathScytheItem extends MamySwordItem {
    public DeathScytheItem() {
        super(ToolMaterials.NETHERITE,6, -2.8f, new Settings().fireproof());
    }
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!user.getWorld().isClient) {
            if (user.isSneaking()) {
                ItemStack getHeadSlot = user.getEquippedStack(EquipmentSlot.HEAD);
                EquipMask(user);
                    if (!getHeadSlot.isEmpty()) {
                        int emptySlot = user.getInventory().getEmptySlot();
                        if (emptySlot >= 0)
                            user.getInventory().setStack(emptySlot, getHeadSlot);
                         else
                            user.dropItem(getHeadSlot, false);
                        EquipMask(user);
                    }
            }
        }
        return super.use(world, user, hand);
    }
    public static void onUseEffect(PlayerEntity user) {
        double damageRadiusSquared = 5.0d;
        List<LivingEntity> entities = user.getWorld().getNonSpectatingEntities(LivingEntity.class, user.getBoundingBox().expand(damageRadiusSquared));

        for (LivingEntity target : entities) {
            if (target != user) {
                double distanceToEntity = target.squaredDistanceTo(user.getX(), user.getY(), user.getZ());
                double normalizedDistance = Math.sqrt(distanceToEntity) / damageRadiusSquared; // Adjust as needed for your range
                float damage = 5.0f - (float) (normalizedDistance * (5.0f - 0.1f));
                if (VizardComponent.hasMasknScythe) {
                    target.damage(target.getWorld().getDamageSources().playerAttack(user), 1f);
                } else {
                    target.damage(target.getWorld().getDamageSources().playerAttack(user), damage);
                }
            }
        }
    }
    private static void EquipMask(PlayerEntity user) {
        Item item = ModItems.DEATH_SCYTHE;
        ItemStack Hollowmask = new ItemStack(ModItems.HOLLOW_MASK);
        Hollowmask.addEnchantment(Enchantments.BINDING_CURSE, 1);
        Hollowmask.addEnchantment(Enchantments.VANISHING_CURSE, 1);
        Hollowmask.addHideFlag(ItemStack.TooltipSection.ENCHANTMENTS);
        user.equipStack(EquipmentSlot.HEAD, Hollowmask);
        HollowmaskItem.useMaskParticle(user);
        onUseEffect(user);
        user.getItemCooldownManager().set(item, 1200); // 3 min
    }
}