package sypztep.mamy.common;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.EntityElytraEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sypztep.mamy.common.compat.MamyCompat;
import sypztep.mamy.common.component.entity.BackWeaponComponent;
import sypztep.mamy.common.init.*;
import sypztep.mamy.common.packetC2S.*;
import sypztep.mamy.common.util.HogyokuState;

import java.util.Objects;

public class MamyMod implements ModInitializer {
    public static final String MODID = "mamy";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
    public static Identifier id(String id) {
        return new Identifier(MODID, id);
    }
    public static final Identifier holdWeaponPacketId = id("hold_packet");
    public static final Identifier swapWeaponPacketId = id("swap_packet");
    public static final Identifier swapInventoryPacketId = id("swap_inventory_packet");
    public static final Identifier utilPacketId = id("util_packet");

    @Override
    public void onInitialize() {
        LOGGER.info("Moony pls don't wet yourself for Minecraft 1.20.1 Fabric Edition.");
        MamyCompat.init();
        //C2S Packet
        ServerPlayNetworking.registerGlobalReceiver(SonidoClearPacket.ID, new SonidoClearPacket.Receiver());
        ServerPlayNetworking.registerGlobalReceiver(SonidoPacket.ID, new SonidoPacket.Receiver());
        ServerPlayNetworking.registerGlobalReceiver(MaskPacket.ID, new MaskPacket.Receiver());
        ServerPlayNetworking.registerGlobalReceiver(AirhikePacket.ID,new AirhikePacket.Receiver());
        ServerPlayNetworking.registerGlobalReceiver(SwirlPacket.ID,new SwirlPacket.Receiver());

        ModEnchantments.init();
        ModSoundEvents.init();
        ModParticles.init();
        ModStatusEffects.init();
        ModEntityAttributes.init();
        ModIframe.registerHandlers();
        ModItems.init();
        ModEntityTypes.init();
        ModItemGroup.init();
        ModLootableModifiers.LootTable();
        ModConfig.init(MODID, ModConfig.class);
        packetinit();

        EntityElytraEvents.ALLOW.register((entity -> false));

        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if (!(entity instanceof PlayerEntity player))
                return;
            final HogyokuState state = HogyokuState.get(world.getServer());
            final String s = player.getEntityName();
            if (!state.map.containsKey(s))
                setupNewPlayer(s, state, player);
        });

        ServerPlayerEvents.COPY_FROM.register(((oldPlayer, newPlayer, alive) -> {
            final HogyokuState state = HogyokuState.get(newPlayer.server);
            final String s = newPlayer.getEntityName();
            final int v;
            if (!state.map.containsKey(s))
                setupNewPlayer(s, state, newPlayer);
            v = Math.max(0, state.map.get(s));
            state.map.put(s, v);

            Objects.requireNonNull(newPlayer.getAttributeInstance(ModEntityAttributes.GENERIC_HOGYOKU)).setBaseValue(v);
        }));

    }

    void setupNewPlayer(String name, HogyokuState state, PlayerEntity player) {
        state.map.put(name, 0);
        state.markDirty();
        Objects.requireNonNull(player.getAttributeInstance(ModEntityAttributes.GENERIC_HOGYOKU)).setBaseValue(0);
    }
    public static void packetinit() {
        ServerPlayNetworking.registerGlobalReceiver(holdWeaponPacketId, (server, player, handler, buf, responseSender) -> {
            boolean hold = buf.readBoolean();
            BackWeaponComponent.setHoldingBackWeapon(player, hold);
        });
        ServerPlayNetworking.registerGlobalReceiver(swapWeaponPacketId, (server, player, handler, buf, responseSender) -> {
            if (!player.isSpectator()) {
                boolean toggled = BackWeaponComponent.isHoldingBackWeapon(player);
                BackWeaponComponent.setHoldingBackWeapon(player, false);
                ItemStack itemStack = BackWeaponComponent.getBackWeapon(player);
                boolean success = BackWeaponComponent.setBackWeapon(player, player.getStackInHand(Hand.MAIN_HAND));
                if (success) {
                    player.setStackInHand(Hand.MAIN_HAND, itemStack);
                }
                player.clearActiveItem();
                BackWeaponComponent.setHoldingBackWeapon(player, toggled);
            }
        });
        ServerPlayNetworking.registerGlobalReceiver(swapInventoryPacketId, (server, player, handler, buf, responseSender) -> {
            int slotId = buf.readInt();
            if (!player.isSpectator()) {
                if (!player.currentScreenHandler.isValid(slotId)) {
                    return;
                }

                Slot slot = player.currentScreenHandler.getSlot(slotId);
                ItemStack itemStack = BackWeaponComponent.getBackWeapon(player);
                boolean success = BackWeaponComponent.setBackWeapon(player, slot.getStack());
                if (success) {
                    slot.setStack(itemStack);
                }
            }
        });
        ServerPlayNetworking.registerGlobalReceiver(utilPacketId, (server, player, handler, buf, responseSender) -> {
            EnderChestInventory enderChest =  player.getEnderChestInventory();
            if (enderChest != null) {
                if (!player.getWorld().isClient()) {
                    player.openHandledScreen(new SimpleNamedScreenHandlerFactory((i, playerInventory, playerEntity) -> {
                        return GenericContainerScreenHandler.createGeneric9x3(i, playerInventory, enderChest);
                    }, Text.translatable("mamy.util").formatted(Formatting.GRAY)));
                }
            }
        });

    }
}
