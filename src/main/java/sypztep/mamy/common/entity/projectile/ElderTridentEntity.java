package sypztep.mamy.common.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ElderTridentEntity extends MamyTridentEntity {
    private final List<ItemStack> fetchedStacks = new ArrayList<>();
    protected Entity tridentTarget;
    protected boolean hasSearchedTarget;
    public ElderTridentEntity(EntityType<? extends ElderTridentEntity> entityType, World world) {
        super(entityType, world);
    }
    public Consumer<ItemStack> getStackFetcher() {
        return fetchedStacks::add;
    }

    @Override
    public void tick() {
        if (this.inGround) {
            this.setDealtDamage();
        }
        if (!this.hasSearchedTarget) {
            if (this.getOwner() != null) {
                Vec3d rotationVec = this.getOwner().getRotationVector();
                Box box = new Box(this.getX() - 1, this.getY() - 1, this.getZ() - 1, this.getX() + 1, this.getY() + 1, this.getZ() + 1).expand(96 * rotationVec.getX(), 96 * rotationVec.getY(), 96 * rotationVec.getZ());
                List<Entity> possibleTargets = getWorld().getEntitiesByClass(Entity.class, box, (entity) -> entity.groundCollision && entity != this.getOwner() && entity.getType() != EntityType.EXPERIENCE_ORB && entity.getType() != EntityType.ENDERMAN && !(entity instanceof TameableEntity && ((TameableEntity) entity).isTamed()) && !(entity instanceof TridentEntity));

                double max = 0.3;
                for (Entity possibleTarget : possibleTargets) {
                    Vec3d vecDist = possibleTarget.getPos().subtract(this.getOwner().getPos());
                    double dotProduct = vecDist.normalize().dotProduct(rotationVec);
                    if (dotProduct > max) {
                        this.tridentTarget = possibleTarget;
                        max = dotProduct;
                    }
                }
                this.hasSearchedTarget = true;
            }
        } else {
        if (!this.hasDealtDamage()) {
            if (this.tridentTarget != null && tridentTarget.isAlive()) {
                Vec3d vec3d = new Vec3d(tridentTarget.getX() - this.getX(), tridentTarget.getEyeY() - this.getY(), tridentTarget.getZ() - this.getZ());
                this.setVelocity(this.getVelocity().multiply(0.9D).add(vec3d.normalize().multiply(0.25D)));
            }
            this.setNoGravity(this.tridentTarget != null && tridentTarget.isAlive());
        } else {
            this.setNoGravity(false);
        }
    }
        super.tick();
        Box box = this.getBoundingBox();
        List<Entity> list = this.getWorld().getOtherEntities(this, box);
        for (Entity entity : list) {
            if (entity instanceof ItemEntity itemEntity) {
                fetchedStacks.add(itemEntity.getStack());
                itemEntity.discard();
            }
        }
    }

    @Override
    protected void setDealtDamage() {
        this.setNoGravity(false);
        this.tridentTarget = null;
        super.setDealtDamage();
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        if (this.getWorld() instanceof ServerWorld && this.hasChanneling() && entityHitResult.getEntity() instanceof LivingEntity livingEntity) {
            if (livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 200, 2))) {
                if (livingEntity instanceof ServerPlayerEntity serverPlayerEntity) {
                    serverPlayerEntity.networkHandler.sendPacket(new GameStateChangeS2CPacket(GameStateChangeS2CPacket.ELDER_GUARDIAN_EFFECT, this.isSilent() ? 0.0F : 1.0F));
                }
            }
        }
    }

    @Override
    public void onPlayerCollision(PlayerEntity player) {
        super.onPlayerCollision(player);
        Entity entity = this.getOwner();
        if (entity == null || entity.getUuid() == player.getUuid()) {
            for (ItemStack stack : fetchedStacks) {
                if (!player.getInventory().insertStack(stack)) {
                    this.dropStack(stack);
                }
            }
            fetchedStacks.clear();
        }
    }

    @Override
    protected float getDragInWater() {
        return 1.0f;
    }

    @Override
    public void remove(RemovalReason reason) {
        if (reason.shouldDestroy()) {
            for (ItemStack fetchedStack : this.fetchedStacks) {
                this.dropStack(fetchedStack);
            }
        }
        super.remove(reason);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound tag) {
        super.readCustomDataFromNbt(tag);
        if (tag.contains("fetched_items", NbtElement.LIST_TYPE)) {
            NbtList fetchedItems = tag.getList("fetched_items", NbtElement.COMPOUND_TYPE);
            for (int i = 0; i < fetchedItems.size(); i++) {
                NbtCompound fetchedItem = fetchedItems.getCompound(i);
                this.fetchedStacks.add(ItemStack.fromNbt(fetchedItem));
            }
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound tag) {
        super.writeCustomDataToNbt(tag);
        NbtList NbtList = new NbtList();
        for (ItemStack fetchedStack : this.fetchedStacks) {
            NbtList.add(fetchedStack.writeNbt(new NbtCompound()));
        }
        tag.put("fetched_stacks", NbtList);
    }
}
