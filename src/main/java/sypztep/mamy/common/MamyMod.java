package sypztep.mamy.common;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sypztep.mamy.common.init.*;
import sypztep.mamy.common.packet.SonidoClearPacket;
import sypztep.mamy.common.packet.SonidoPacket;

public class MamyMod implements ModInitializer {
    public static final String MODID = "mamy";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
    public static Identifier id(String id) {
        return new Identifier(MODID, id);
    }

    @Override
    public void onInitialize() {
        ServerPlayNetworking.registerGlobalReceiver(SonidoClearPacket.ID, new SonidoClearPacket.Receiver());
        ServerPlayNetworking.registerGlobalReceiver(SonidoPacket.ID, new SonidoPacket.Receiver());

        ModEnchantments.init();
        ModSoundEvents.init();
        ModParticles.init();
        ModStatusEffects.init();
        ModIframe.registerHandlers();
        ModItems.init();
        ModEntityTypes.init();
        ModItemGroup.init();
        ModLootableModifiers.LootTable();
        ModConfig.init(MODID, ModConfig.class);
    }
}
