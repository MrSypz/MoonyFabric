package sypztep.mamy.common.Item;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Equipment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class MamyMaskCosmItem extends Item implements Equipment {
    public static EquipmentSlot selectslot;
    public MamyMaskCosmItem(Settings settings,EquipmentSlot slot) {
        super(settings);
        selectslot = slot;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return this.equipAndSwap(this, world, user, hand);
    }
    @Override
    public EquipmentSlot getSlotType() {
        return selectslot;
    }
}
