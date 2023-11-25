package sypztep.mamy.common.init;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import sypztep.mamy.common.ModConfig;
import sypztep.mamy.common.interfaces.EntityHurtCallback;
import sypztep.mamy.common.interfaces.EntityKnockbackCallback;
import sypztep.mamy.common.interfaces.PlayerAttackCallback;
import sypztep.mamy.common.interfaces.PlayerEntityAccessor;

public class ModIframe {

    public static void registerHandlers() {
        EntityHurtCallback.EVENT.register((entity, source, amount) -> {
            if (entity.getEntityWorld().isClient) {
                return ActionResult.PASS;
            }
            if (ModConfig.excludePlayers && entity instanceof PlayerEntity) {
                return ActionResult.PASS;
            }
            if (ModConfig.excludeAllMobs && !(entity instanceof PlayerEntity)) {
                return ActionResult.PASS;
            }
            Identifier loc = EntityType.getId(entity.getType());
            for (String id : ModConfig.dmgReceiveExcludedEntities) {
                if (loc == null)
                    break;
                int starIndex = id.indexOf('*');
                if (starIndex != -1) {
                    if (loc.toString().contains(id.substring(0, starIndex))) {
                        return ActionResult.PASS;
                    }
                } else if (loc.toString().equals(id)) {
                    return ActionResult.PASS;
                }
            }
            for (String dmgType : ModConfig.damageSrcWhitelist) {
                if (source.getName().equals(dmgType)) {
                    return ActionResult.PASS;
                }
            }
            for (String id : ModConfig.attackExcludedEntities) {
                Entity attacker = source.getAttacker();
                if (attacker == null)
                    break;
                if (loc == null)
                    break;
                int starIndex = id.indexOf('*');
                if (starIndex != -1) {
                    if (loc.toString().contains(id.substring(0, starIndex))) {
                        return ActionResult.PASS;
                    }
                } else if (loc.toString().equals(id)) {
                    return ActionResult.PASS;
                }

            }

            entity.timeUntilRegen = ModConfig.iFrameInterval;
            return ActionResult.PASS;
        });
        PlayerAttackCallback.EVENT.register((player, target) -> {
            if (player.getEntityWorld().isClient) {
                return ActionResult.PASS;
            }

            float str = player.getAttackCooldownProgress(0);
            if (str <= ModConfig.attackCancelThreshold) {
                return ActionResult.FAIL;
            }
            if (str <= ModConfig.knockbackCancelThreshold) {
                // Don't worry, it's only magic
                PlayerEntityAccessor playerAccessor = (PlayerEntityAccessor) player;
                playerAccessor.setSwinging(true);
            }

            return ActionResult.PASS;

        });
        EntityKnockbackCallback.EVENT.register((entity, source, amp, dx, dz) -> {
            if (entity.getEntityWorld().isClient) {
                return ActionResult.PASS;
            }
            if (source != null) {
                if (source instanceof PlayerEntity player) {
                    PlayerEntityAccessor playerAccessor = (PlayerEntityAccessor) player;
                    if (playerAccessor.isSwinging()) {
                        playerAccessor.setSwinging(false);
                        return ActionResult.FAIL;
                    }
                }
            }
            return ActionResult.PASS;
        });
    }
}
