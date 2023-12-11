package sypztep.mamy.common;

import eu.midnightdust.lib.config.MidnightConfig;


public class ModConfig extends MidnightConfig{
    @Comment(centered = true)
    public static Comment iframetext;
    @Entry
    public static int iFrameInterval = 10;

    @Entry
    public static boolean excludePlayers = false;

    @Entry
    public static boolean excludeAllMobs = false;
    @Entry
    public static float attackCancelThreshold = 0.1f;

    @Entry
    public static float knockbackCancelThreshold = 0.75f;

    public static String[] attackExcludedEntities = new String[] {"minecraft:slime", "minecraft:magma_cube"};

    public static String[] dmgReceiveExcludedEntities = new String[] {};

    public static String[] damageSrcWhitelist = new String[] {"inFire", "lava", "sweetBerryBush", "cactus", "lightningBolt", "inWall", "hotFloor"};
    @Comment(centered = true)
    public static Comment MaskDashEffect;
    @Entry
    public static float distorsion = 1.0f;


    public static double default_multiplier = 0.50D;

    public static boolean consume_animation = true;

    public static boolean prioritize_shield = false;

}
