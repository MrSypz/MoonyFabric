package sypztep.mamy.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import sypztep.mamy.common.init.ModEntityComponents;
import sypztep.mamy.common.packetC2S.HoldWeaponPacket;

public class BackWeaponComponent implements AutoSyncedComponent {
    private final PlayerEntity obj;
    private final SimpleInventory backWeapon = new SimpleInventory(1);
    private boolean holdingBackWeapon = false;

    public BackWeaponComponent(PlayerEntity obj) {
        this.obj = obj;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        this.backWeapon.setStack(0, ItemStack.fromNbt(tag.getCompound("backWeapon")));
        this.holdingBackWeapon = tag.getBoolean("holdingBackWeapon");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.put("backWeapon", this.backWeapon.getStack(0).writeNbt(new NbtCompound()));
        tag.putBoolean("holdingBackWeapon", this.holdingBackWeapon);
    }
    public ItemStack getBackWeapon() {
        return this.backWeapon.getStack(0);
    }

    public static ItemStack getBackWeapon(PlayerEntity player) {
        return ((BackWeaponComponent) ModEntityComponents.BACK_WEAPON_COMPONENT.get(player)).getBackWeapon();
    }

    public boolean setBackWeapon(ItemStack backWeapon) {
        this.backWeapon.setStack(0, backWeapon);
        ModEntityComponents.BACK_WEAPON_COMPONENT.sync(this.obj);
        return true;
    }

    public static boolean setBackWeapon(PlayerEntity player, ItemStack backWeapon) {
        return ((BackWeaponComponent)ModEntityComponents.BACK_WEAPON_COMPONENT.get(player)).setBackWeapon(backWeapon);
    }

    public SimpleInventory getBackWeaponInventory() {
        return this.backWeapon;
    }

    public static SimpleInventory getBackWeaponInventory(PlayerEntity player) {
        return ((BackWeaponComponent)ModEntityComponents.BACK_WEAPON_COMPONENT.get(player)).getBackWeaponInventory();
    }

    public boolean isHoldingBackWeapon() {
        return this.holdingBackWeapon;
    }

    public static boolean isHoldingBackWeapon(PlayerEntity player) {
        return ((BackWeaponComponent)ModEntityComponents.BACK_WEAPON_COMPONENT.get(player)).isHoldingBackWeapon();
    }

    public void setHoldingBackWeapon(boolean holdingBackWeapon) {
        this.holdingBackWeapon = holdingBackWeapon;
        ModEntityComponents.BACK_WEAPON_COMPONENT.sync(this.obj);
    }

    public static void setHoldingBackWeapon(PlayerEntity player, boolean holdingBackWeapon) {
        if (player.getWorld().isClient) {
//            PacketByteBuf buf = PacketByteBufs.create();
//            buf.writeBoolean(holdingBackWeapon);
//            ClientPlayNetworking.send(HoldWeaponPacket.ID, buf);
            HoldWeaponPacket.send(holdingBackWeapon);
        } else {
            ((BackWeaponComponent)ModEntityComponents.BACK_WEAPON_COMPONENT.get(player)).setHoldingBackWeapon(holdingBackWeapon);
        }
    }
}
