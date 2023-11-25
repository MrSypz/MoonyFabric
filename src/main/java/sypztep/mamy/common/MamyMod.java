package sypztep.mamy.common;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sypztep.mamy.common.init.*;

public class MamyMod implements ModInitializer {
    public static final String MODID = "mamy";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
    public static Identifier id(String id) {
        return new Identifier(MODID, id);
    }

    @Override
    public void onInitialize() {
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
