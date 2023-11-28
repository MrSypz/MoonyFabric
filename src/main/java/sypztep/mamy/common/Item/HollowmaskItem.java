package sypztep.mamy.common.Item;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import sypztep.mamy.common.init.ModParticles;

public class HollowmaskItem extends MamyMaskFuncItem {

    public HollowmaskItem(Settings settings) {
        super(ArmorMaterials.NETHERITE,Type.HELMET, settings);
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.BLOCK_SCULK_BREAK;
    }
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
            useMaskParticle(user);
        return super.use(world, user, hand);
    }
    //User Mask method use by DeathScythe and Hollowmask item
    public static void useMaskParticle(PlayerEntity user) {
        if (!user.getWorld().isClient) {
            ((ServerWorld) user.getWorld()).spawnParticles(ModParticles.BLOOD_BUBBLE_SPLATTER, user.getX(), user.getEyeY(), user.getZ(), 45, 0.1D, 0.1D, 0.1D, 0.1D);
            ((ServerWorld) user.getWorld()).spawnParticles(ModParticles.BLOODWAVE, user.getX(), user.getY(), user.getZ(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
        }
    }
    public static boolean HalfMask(ItemStack stack) {
        return stack.getDamage() < stack.getMaxDamage() / 2;
    }
    @Override
    public EquipmentSlot getSlotType() {
        return EquipmentSlot.HEAD;
    }
}
