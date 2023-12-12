package sypztep.mamy.common.Item;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterials;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import sypztep.mamy.client.packetS2C.AddSwirlingParticlePacket;
import sypztep.mamy.common.init.*;
import sypztep.mamy.common.util.SkillUtil;

import java.util.List;
import java.util.UUID;

import static sypztep.mamy.common.util.SkillUtil.getCounts;

public class DeathScytheItem extends EmptySwordItem implements CustomHitSoundItem, CustomHitParticleItem{
    public DeathScytheItem() {
        super(ToolMaterials.NETHERITE,6, -3f, new Settings().fireproof());
    }
    private static final EntityAttributeModifier REACH_MODIFIER;
    private int count = 0;
    @Override
        public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        Multimap<EntityAttribute, EntityAttributeModifier> map = LinkedHashMultimap.create(super.getAttributeModifiers(slot));
        if (slot == EquipmentSlot.MAINHAND) {
            map.put(ReachEntityAttributes.ATTACK_RANGE, REACH_MODIFIER);
        }
        return map;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (world.isClient()) {
            if (!user.getItemCooldownManager().isCoolingDown(itemStack.getItem())) {
                user.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1, (float) (1.5f + user.getRandom().nextGaussian() / 10));
                return TypedActionResult.consume(itemStack);
            }
        }
        if (!world.isClient()) {
            if (!user.getItemCooldownManager().isCoolingDown(itemStack.getItem())) {
                SkillUtil.ShockWaveDamage(user,3.0d, 0.5f, true,true);
                user.heal(getCounts() * 0.25f); //heal amount target hit with efficiency 25%
                addSwirlingParticles(user);
                count++;
                if (count >= 5 ) {
                    user.getItemCooldownManager().set(itemStack.getItem(), 60);
                    user.damage(user.getWorld().getDamageSources().create(ModDamageTypes.BLEEDOUT), + user.getHealth() * 0.25f); // consume 25% of player current health
                    user.addStatusEffect(new StatusEffectInstance(ModStatusEffects.GRIEVOUSWOUNDS,180,0,false,false,false));
                    count = 0;
                }
            }
        }
        return TypedActionResult.pass(itemStack);
    }
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        itemdesc(tooltip);
        super.appendTooltip(stack, world, tooltip, context);
    }
    private void itemdesc(List<Text> list) {
        Item item = this; // Assuming this is the current item
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        String registryName = item.getTranslationKey();

        MutableText info = (Text.translatable(registryName + ".desc" ,String.valueOf(String.format("%.2f",player.getHealth() * 0.25f)))).formatted(Formatting.GRAY);
        MutableText passive = (Text.translatable(registryName + ".desc.passive")).formatted(Formatting.GRAY);
        list.add(Text.literal(" - ").append((Text.literal("Passive : ").append(passive).formatted(Formatting.GOLD))).formatted(Formatting.GRAY));
        list.add(Text.literal(" - ").append((Text.literal("Ability : RightClick").formatted(Formatting.GOLD))).formatted(Formatting.GRAY));
        list.add((info).append((Text.literal(String.valueOf(String.format("%.2f",player.getHealth() * 0.25f)))).formatted(Formatting.RED).append(Text.literal(" ♥"))));
    }
    private void addSwirlingParticles(PlayerEntity player) { //Client Packet
        ServerPlayerEntity dys = ((ServerPlayerEntity) player);
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeDouble(dys.getX());
        buf.writeDouble(dys.getBodyY(0.2f));
        buf.writeDouble(dys.getZ());
        for (ServerPlayerEntity pl : PlayerLookup.tracking(dys.getServerWorld(), dys.getChunkPos())) {
            ServerPlayNetworking.send(pl, AddSwirlingParticlePacket.ID, buf);
        }
    }
    @Override
    public void playHitSound(PlayerEntity user) {
        user.playSound(ModSoundEvents.ENTITY_PLAYER_ATTACK_SCYTHE, 1.0F, (float)(1.0 + user.getRandom().nextGaussian() / 10.0));
    }
    @Override
    public void spawnHitParticles(PlayerEntity user) {
        double d0 = -MathHelper.sin(user.getYaw() * 0.017453292F);
        double d1 = MathHelper.cos(user.getYaw() * 0.017453292F);
        World var7 = user.getWorld();
        if (var7 instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(ModParticles.RED_SWEEP_ATTACK_PARTICLE, user.getX() + d0, user.getBodyY(0.5), user.getZ() + d1, 0, d0, 0.0, d1, 0.0);
        }
    }
    static {
        REACH_MODIFIER = new EntityAttributeModifier(UUID.fromString("911af262-067d-4da2-854c-20f03cc2dd8b"), "Weapon modifier", 0.5, EntityAttributeModifier.Operation.ADDITION);
    }
}