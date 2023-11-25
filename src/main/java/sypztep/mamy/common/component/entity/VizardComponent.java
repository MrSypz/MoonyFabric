package sypztep.mamy.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;
import sypztep.mamy.client.MamyModClient;
import sypztep.mamy.common.Item.HollowmaskItem;
import sypztep.mamy.common.init.ModItems;
import sypztep.mamy.common.init.ModParticles;
import sypztep.mamy.common.init.ModSoundEvents;
import sypztep.mamy.common.packet.SonidoClearPacket;
import sypztep.mamy.common.packet.SonidoPacket;

public class VizardComponent implements AutoSyncedComponent, CommonTickingComponent {
    private static final short DEFAULT_SONIDO_COOLDOWN = 8;
    public static boolean hasMask = false;
    private static short sonidoCooldown = DEFAULT_SONIDO_COOLDOWN, ticksLefthasPress = 0;
    public static short invisDuration = 0;
    private final PlayerEntity obj;
    private boolean wasPressing = false;

    public VizardComponent(PlayerEntity obj) {
        this.obj = obj;
    }

    private boolean hasAnyMask(PlayerEntity player) {
        for (ItemStack stack : player.getInventory().armor) {
            for (HollowmaskItem mask : ModItems.ALL_MASK) {
                if (stack.getItem() == mask) {
                    return true;
                }
            }
        }
        return false;
    }
    @Override
    public void tick() {
        ItemStack stack = obj.getEquippedStack(EquipmentSlot.HEAD);
        hasMask = hasAnyMask(obj);
        if (hasMask && !HollowmaskItem.HalfMask(stack))
            obj.getWorld().addParticle(ParticleTypes.CLOUD, obj.getParticleX(2), obj.getEyeY(), obj.getParticleZ(2), 0, 0.1, 0);
        if (hasMask) {
            if (obj.age % 20 == 0) {
                stack.damage(1, obj, (consumer) -> consumer.sendEquipmentBreakStatus(EquipmentSlot.HEAD));
            }
            if (sonidoCooldown > 0) {
                sonidoCooldown--;
            }
        } else {
            resetInv(obj);
            sonidoCooldown = DEFAULT_SONIDO_COOLDOWN;
        }
    }

    @Override
    public void clientTick() {
        tick();
        if (hasMask && sonidoCooldown == 0 && !obj.isSpectator() && obj == MinecraftClient.getInstance().player) {
            GameOptions options = MinecraftClient.getInstance().options;
            boolean pressingActivationKey = MamyModClient.SONIDO_KEYBINDING.isUnbound() ? options.sprintKey.isPressed() : MamyModClient.SONIDO_KEYBINDING.isPressed();
            if (ticksLefthasPress > 0)
                ticksLefthasPress--;
            if (invisDuration > 0) {
                invisDuration--;
                if (invisDuration <= 0) {
                    SonidoClearPacket.send();
                    resetInv(obj);
                }
            }
            if (pressingActivationKey && !wasPressing) {
                if (ticksLefthasPress > 0) {
                    ticksLefthasPress = 0;
                    Vec3d velocity = getVelocityFromInput(options).rotateY((float) Math.toRadians(-(obj.getHeadYaw() + 90)));
                    handle(obj, this, velocity.getX(), velocity.getZ());
                    addSonidoParticles(obj);
                    SonidoPacket.send(velocity);
                } else
                    ticksLefthasPress = 7;
            }
            wasPressing = pressingActivationKey;
        }
    }
    private Vec3d getVelocityFromInput(GameOptions options) {
        if (options.backKey.isPressed())
            return new Vec3d(-2, 0, 0);
        if (options.leftKey.isPressed())
            return new Vec3d(0, 0, -2);
        if (options.rightKey.isPressed())
            return new Vec3d(0, 0, 2);
        return new Vec3d(2, 0, 0);
    }
    public static void handle(Entity user, VizardComponent vizardComponent, double velocityX, double velocityZ) {//Server Packet
        user.addVelocity(velocityX, 0, velocityZ);
        user.playSound(ModSoundEvents.ENTITY_GENERIC_SONIDO, 1.0f, 1.0f);
        sonidoCooldown = DEFAULT_SONIDO_COOLDOWN;
    }
    public static void addSonidoParticles(LivingEntity entity) { //Client Packet
        entity.setInvisible(true);
        invisDuration = 8;
        if (MinecraftClient.getInstance().gameRenderer.getCamera().isThirdPerson() || entity != MinecraftClient.getInstance().cameraEntity)
            entity.getWorld().addParticle(ParticleTypes.FLASH, entity.getParticleX(2), entity.getRandomBodyY(), entity.getParticleZ(2), 0, 0, 0);
    }
    public static void resetInv(LivingEntity entity) {
        entity.setInvisible(false);
    }
    @Override
    public void readFromNbt(NbtCompound tag) {

    }

    @Override
    public void writeToNbt(NbtCompound tag) {

    }
}
