package sypztep.mamy.common.compat;

import net.fabricmc.loader.api.FabricLoader;
import sypztep.mamy.common.MamyMod;

public class MamyCompat {
    public static boolean isEnhancementLoaded = false;
    public static void init() {
        if (FabricLoader.getInstance().isModLoaded("enchancement")) {
            isEnhancementLoaded = true;
            MamyMod.LOGGER.info("Enchancement is loaded!");
        } else MamyMod.LOGGER.info("Nothing is loaded.");
    }
}
