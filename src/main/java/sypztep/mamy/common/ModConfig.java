package sypztep.mamy.common;

import eu.midnightdust.lib.config.MidnightConfig;


public class ModConfig extends MidnightConfig {
    @Comment(centered = true)
    public static Comment iframetext;
    @Entry
    public static int iFrameInterval = 0;
    @Entry
    public static boolean excludePlayers = false;
    @Entry
    public static boolean excludeAllMobs = false;
    @Entry
    public static float attackCancelThreshold = 0.1f;
    @Entry
    public static float knockbackCancelThreshold = 0.75f;
    @Entry
    public static boolean debugMode = false;

    public static String[] attackExcludedEntities = new String[] {"minecraft:slime", "minecraft:magma_cube", "enchancement:brimstone"};
    public static String[] dmgReceiveExcludedEntities = new String[] {};
    public static String[] damageSrcWhitelist = new String[] {"inFire", "lava", "sweetBerryBush", "cactus", "lightningBolt", "inWall", "hotFloor","brimstone","bloodlust"};
    @Comment(centered = true)
    public static Comment MaskDashEffect;
    @Entry
    public static float distorsion = 1.0f;
    @Comment(centered = true)
    public static Comment SwordParry;
    public static double default_multiplier = 0.50D;
    @Entry
    public static boolean consume_animation = false;
    @Entry
    public static boolean prioritize_shield = true;
    @Comment(centered = true)
    public static Comment FeatureCosmetic;
    @Entry (min = -1024.0f)
    public static float offsetx = 0.0f;
    @Entry (min = -1024.0f)

    public static float offsety = 0.0f;
    @Entry (min = -1024.0f)

    public static float offsetz = 0.0f;
    @Entry
    public static CosmeticsOptions cosmetics = CosmeticsOptions.ENABLE;
    public static boolean shouldDisplayCosmetics() {
        return cosmetics == CosmeticsOptions.ENABLE || cosmetics == CosmeticsOptions.FIRST_PERSON;
    }
    public enum CosmeticsOptions {
        ENABLE, FIRST_PERSON, DISABLE
    }
}
