package sypztep.mamy.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import sypztep.mamy.client.MamyModClient;
import sypztep.mamy.common.MamyMod;
import sypztep.mamy.common.init.ModEntityComponents;
import sypztep.mamy.common.util.EnchantmentUtil;

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
        return ModEntityComponents.BACK_WEAPON_COMPONENT.get(player).getBackWeapon();
    }

    public boolean setBackWeapon(ItemStack backWeapon) {
        this.backWeapon.setStack(0, backWeapon);
        ModEntityComponents.BACK_WEAPON_COMPONENT.sync(this.obj);
        return true;
    }

    public static boolean setBackWeapon(PlayerEntity player, ItemStack backWeapon) {
        return ModEntityComponents.BACK_WEAPON_COMPONENT.get(player).setBackWeapon(backWeapon);
    }

    public SimpleInventory getBackWeaponInventory() {
        return this.backWeapon;
    }

    public static SimpleInventory getBackWeaponInventory(PlayerEntity player) {
        return ModEntityComponents.BACK_WEAPON_COMPONENT.get(player).getBackWeaponInventory();
    }

    public boolean isHoldingBackWeapon() {
        return this.holdingBackWeapon;
    }

    public static boolean isHoldingBackWeapon(PlayerEntity player) {
        return ModEntityComponents.BACK_WEAPON_COMPONENT.get(player).isHoldingBackWeapon();
    }

    public void setHoldingBackWeapon(boolean holdingBackWeapon) {
        this.holdingBackWeapon = holdingBackWeapon;
        ModEntityComponents.BACK_WEAPON_COMPONENT.sync(this.obj);
    }

    public static void setHoldingBackWeapon(PlayerEntity player, boolean holdingBackWeapon) {
        if (player.getWorld().isClient()) {
            if (getBackWeapon(player).getItem() instanceof TridentItem && MamyModClient.WEAPON_KEYBINDING.isPressed()) {
                if (!EnchantmentUtil.hasEnchantment(Enchantments.RIPTIDE, getBackWeapon(player))) {
                    player.sendMessage(Text.translatable("backslot.cannot.consume").formatted(Formatting.RED));
                    return;
                }
            }
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeBoolean(holdingBackWeapon);
            ClientPlayNetworking.send(MamyMod.holdWeaponPacketId, buf);
        } else {
            if (getBackWeapon(player).getItem() instanceof TridentItem) {
                if (EnchantmentUtil.hasEnchantment(Enchantments.RIPTIDE, getBackWeapon(player)))
                    ModEntityComponents.BACK_WEAPON_COMPONENT.get(player).setHoldingBackWeapon(holdingBackWeapon);
            } else
                ModEntityComponents.BACK_WEAPON_COMPONENT.get(player).setHoldingBackWeapon(holdingBackWeapon);
        }
    }
}
