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
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.joml.Vector3f;
import sypztep.mamy.client.MamyModClient;
import sypztep.mamy.client.particle.util.Easing;
import sypztep.mamy.common.Item.HollowmaskItem;
import sypztep.mamy.common.init.ModEntityComponents;
import sypztep.mamy.common.init.ModParticles;
import sypztep.mamy.common.init.ModSoundEvents;
import sypztep.mamy.common.packetC2S.AirhikePacket;
import sypztep.mamy.common.packetC2S.SonidoClearPacket;
import sypztep.mamy.common.packetC2S.SonidoPacket;
import sypztep.mamy.common.util.AbilityUtil;
import sypztep.mamy.common.util.EnchantmentUtil;
import sypztep.mamy.mixin.util.accesor.LivingEntityAccessor;

import static sypztep.mamy.common.util.RaytraceUtil.raytraceForDash;

public class VizardComponent implements AutoSyncedComponent, CommonTickingComponent{
    private static final short DEFAULT_SONIDO_COOLDOWN = 8;
    private final PlayerEntity obj;
    public static boolean hasMask = false ,dodash = false, wearingmask = false;
    private int sonidoCooldown = DEFAULT_SONIDO_COOLDOWN, ticksLefthasPress = 0,
            jumpCooldown = 10, jumpsLeft = 0, ticksInAir = 0 ,ticksInAir2 = 0;
    public static int invisDuration = 0;
    private int pressedTicks = 0;

    private boolean wasPressing = false ;

    public VizardComponent(PlayerEntity obj) {
        this.obj = obj;
    }
    @Override
    public void readFromNbt(NbtCompound tag) {
        sonidoCooldown = tag.getInt("SonidoCooldown");
        ticksInAir = tag.getInt("TicksInAir");
        ticksInAir2 = tag.getInt("TicksInAir2");
        jumpCooldown = tag.getInt("JumpCooldown");
        jumpsLeft = tag.getInt("JumpsLeft");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putInt("SonidoCooldown",sonidoCooldown);
        tag.putInt("TicksInAir", ticksInAir);
        tag.putInt("TicksInAir2", ticksInAir2);
        tag.putInt("JumpCooldown", jumpCooldown);
        tag.putInt("JumpsLeft", jumpsLeft);
    }

    @Override
    public void tick() {
        ItemStack stack = obj.getEquippedStack(EquipmentSlot.HEAD);
        hasMask = AbilityUtil.hasAnyMask(obj);
        if (hasMask && !HollowmaskItem.HalfMask(stack))
            obj.getWorld().addParticle(ParticleTypes.CLOUD, obj.getParticleX(2), obj.getEyeY(), obj.getParticleZ(2), 0, 0.1, 0);
        if (hasMask) {
            wearingmask = true;
            if (obj.age % 20 == 0)
                stack.damage(1, obj, (consumer) -> consumer.sendEquipmentBreakStatus(EquipmentSlot.HEAD));
            if (sonidoCooldown > 0)
                sonidoCooldown--;
            if (jumpCooldown > 0)
                jumpCooldown--;
            if (obj.isOnGround()) {
                ticksInAir = 0;
                ticksInAir2 = 0;
                jumpsLeft = 4;
            } else ticksInAir2++;
            if (EnchantmentUtil.isGroundedOrAirborne(obj) && obj.getWorld().raycast(new RaycastContext(obj.getPos(), obj.getPos().add(0, -1, 0), RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.ANY, obj)).getType() == HitResult.Type.MISS)
                ticksInAir++;

        } else {
            wearingmask = false;
            resetInv(obj);
            sonidoCooldown = DEFAULT_SONIDO_COOLDOWN;
            jumpCooldown = 0;
            jumpsLeft = 0;
            ticksInAir = 0;
            ticksInAir2 = 0;
        }
    }

    @Override
    public void clientTick() {
        tick();
        if (hasMask && sonidoCooldown == 0 && !obj.isSpectator() && obj == MinecraftClient.getInstance().player) {
            GameOptions options = MinecraftClient.getInstance().options;
            boolean pressingActivationKey = MamyModClient.SONIDO_KEYBINDING.isUnbound() ? options.sprintKey.isPressed() : MamyModClient.SONIDO_KEYBINDING.isPressed();
            pressedTicks = pressingActivationKey ? pressedTicks + 1 : 0;

            if (ticksLefthasPress > 0)
                ticksLefthasPress--;
            if (invisDuration > 0) {
                invisDuration--;
                if (invisDuration <= 0) {
                    dodash = false;
                }
            }
            if (pressingActivationKey && !wasPressing) {
                if (ticksLefthasPress > 0) {
                    ticksLefthasPress = 0;
                    handle(obj, this);
                    addSonidoParticles(obj);
                    SonidoPacket.send();
                    dodash = true;
                } else
                    ticksLefthasPress = 7;
            } else if (pressingActivationKey && pressedTicks > 0) {
                World world = obj.getWorld();
                Vec3d target = raytraceForDash(obj);
                if (target != null) {
                    for (int i = 0; i <= 9; i++) {
                        world.addParticle(
                                new DustParticleEffect(new Vector3f(0, 0, 0), 1),
                                target.getX() - 0.5 + world.random.nextDouble(),
                                target.getY() + world.random.nextDouble() * 2,
                                target.getZ() - 0.5 + world.random.nextDouble(),
                                0.0,
                                0.5,
                                0.0
                        );
                    }
                }
            }
            wasPressing = pressingActivationKey;
            if (dodash) {
                float t = (float) ((invisDuration) * 0.1) * -1;
                float interpolatedValue = MathHelper.lerp(
                        Easing.CUBIC_IN.ease(t, 0, -1.2f - 0, 1),
                        0, -1.2f);
                MamyModClient.setDistortAmount(interpolatedValue);
            }
             else {
                MamyModClient.setDistortAmount(0f);
                SonidoClearPacket.send();
                resetInv(obj);
            }
        }
        if (!obj.isOnGround() && hasMask && jumpCooldown == 0 && jumpsLeft > 0 && ticksInAir2 >= 8 && EnchantmentUtil.isGroundedOrAirborne(obj) && ((LivingEntityAccessor) obj).mamy$jumping()) {
            handlejump(obj, this);
            addAirhikeParticles(obj);
            AirhikePacket.send();
        }
    }
    public int getTicksInAir() {
        return ticksInAir;
    }

    public static void handlejump(PlayerEntity player, VizardComponent vizardComponent) {
        ModEntityComponents.VIZARD.maybeGet(player).ifPresent(dashComponent -> {
            player.jump();
            player.setVelocity(player.getVelocity().getX(), player.getVelocity().getY() * 2.0f, player.getVelocity().getZ());
            player.addExhaustion(0.1f);

            player.playSound(ModSoundEvents.ENTITY_GENERIC_AIRHIKE, 1.0F, 1.3F);
            vizardComponent.jumpCooldown = 8;
            vizardComponent.jumpsLeft--;
        });
    }
    public static void handle(PlayerEntity user, VizardComponent vizardComponent) {//Server Packet
        Vec3d target = raytraceForDash(user);
        if (target != null) {
            user.playSound(ModSoundEvents.ENTITY_GENERIC_SONIDO, 1.0f, 1.0f);
            user.teleport(target.x, target.y, target.z);
            user.addExhaustion(0.1f);
            vizardComponent.sonidoCooldown = DEFAULT_SONIDO_COOLDOWN;
        }
    }
    public static void addSonidoParticles(LivingEntity entity) { //Client Packet
        entity.setInvisible(true);
        invisDuration = DEFAULT_SONIDO_COOLDOWN;
        if (MinecraftClient.getInstance().gameRenderer.getCamera().isThirdPerson() || entity != MinecraftClient.getInstance().cameraEntity)
            entity.getWorld().addParticle(ParticleTypes.FLASH, entity.getParticleX(2), entity.getRandomBodyY(), entity.getParticleZ(2), 0, 0, 0);
    }
    public static void addAirhikeParticles(Entity entity) {
        if (MinecraftClient.getInstance().gameRenderer.getCamera().isThirdPerson() || entity != MinecraftClient.getInstance().cameraEntity) {
            for (int i = 0; i < 1; i++) {
                entity.getWorld().addParticle(ModParticles.BLOODWAVE, entity.getParticleX(0.2F), entity.getY(), entity.getParticleZ(0.2F), 0, 0, 0);
            }
        }
    }
    public static void resetInv(LivingEntity entity) {
        entity.setInvisible(false);
    }
}
