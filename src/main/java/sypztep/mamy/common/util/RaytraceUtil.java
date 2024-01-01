package sypztep.mamy.common.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import sypztep.mamy.common.init.ModEntityAttributes;

public class RaytraceUtil {
    public static Vec3d raytraceForDash(LivingEntity player) {
        int baseValue = (int) player.getAttributes().getBaseValue(ModEntityAttributes.GENERIC_HOGYOKU);
        float range = 6 * baseValue;

        World world = player.getWorld();
        Vec3d eyeVec = player.getCameraPosVec(0f);
        Vec3d dir = player.getRotationVec(0f);
        Vec3d rayEnd = eyeVec.add(dir.x * range, dir.y * range, dir.z * range);
        BlockHitResult result = world.raycast(new RaycastContext(eyeVec, rayEnd, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, player));
        BlockPos dashPos;
        boolean posIsFree;
        switch (result.getSide()) {
            case DOWN:
                dashPos = result.getBlockPos().down(2);
                break;
            default:
                dashPos = result.getBlockPos().offset(result.getSide());
                break;
        }
        posIsFree = posIsClear(world, dashPos);
        while (!posIsFree) {
            dashPos = dashPos.down();
            posIsFree = posIsClear(world, dashPos) && world.raycast(new RaycastContext(eyeVec, Vec3d.ofCenter(dashPos.up()), RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, player)).getType() == HitResult.Type.MISS;
            if (dashPos.getY() <= 0)
                break;
        }
        return posIsFree ? Vec3d.ofCenter(dashPos) : null;
    }

    public static boolean posIsClear(World world, BlockPos pos) {
        return (world.isAir(pos) || world.getBlockState(pos).getCollisionShape(world, pos).isEmpty())
                && (world.isAir(pos.up()) || world.getBlockState(pos.up()).getCollisionShape(world, pos.up()).isEmpty());
    }
}