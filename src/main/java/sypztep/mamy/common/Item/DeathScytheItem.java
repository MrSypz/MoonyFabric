package sypztep.mamy.common.Item;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterials;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import sypztep.mamy.client.packetS2C.AddSwirlingParticlePacket;
import sypztep.mamy.common.init.*;
import sypztep.mamy.common.packetC2S.SwirlPacket;
import sypztep.mamy.common.util.SkillUtil;

import java.util.List;
import java.util.UUID;


public class DeathScytheItem extends EmptySwordItem implements CustomHitSoundItem, CustomHitParticleItem {
    public DeathScytheItem() {
        super(ToolMaterials.NETHERITE,8, -3f, new Settings().fireproof().rarity(Rarity.EPIC));
    }
    private static final EntityAttributeModifier REACH_MODIFIER;
    private static final EntityAttributeModifier CRIT_DAMAGE_MODIFIER;
    private int count = 0;
    @Override
        public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        Multimap<EntityAttribute, EntityAttributeModifier> map = LinkedHashMultimap.create(super.getAttributeModifiers(slot));
        if (slot == EquipmentSlot.MAINHAND) {
            map.put(ReachEntityAttributes.ATTACK_RANGE, REACH_MODIFIER);
            map.put(ModEntityAttributes.GENERIC_CRIT_DAMAGE, CRIT_DAMAGE_MODIFIER);
        }
        return map;
    }

    @Override
    public TypedActionResult<ItemStack> use (World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        SkillUtil scytheskill = new SkillUtil();
        if (EnchantmentHelper.getEquipmentLevel(ModEnchantments.VENGEANCE, user) <= 0)
            return super.use(world, user, hand);
        else {
            if (!world.isClient()) {
                if ((user.getStatusEffect(ModStatusEffects.SCYTHE_COOLDOWN) == null)) {
                    scytheskill.ShockWaveDamage(user, 5.0d, 22.0f, true, true);
                    user.heal(scytheskill.getCounts()); //heal 50 % amount damage +
                    AddSwirlingParticlePacket.send((ServerPlayerEntity) user,user.getId());
                    SwirlPacket.send();
                    count++;
                    if (count >= 5) {
                        user.damage(user.getWorld().getDamageSources().create(ModDamageTypes.BLEEDOUT), user.getHealth() * 0.25f); // consume 25% of player current health
                        user.addStatusEffect(new StatusEffectInstance(ModStatusEffects.GRIEVOUSWOUNDS, 180, 0, false, false, false));
                        user.addStatusEffect(new StatusEffectInstance(ModStatusEffects.SCYTHE_COOLDOWN, 120, 0, false, false, true));
                        count = 0;
                    }
                }
            } else
                if ((user.getStatusEffect(ModStatusEffects.SCYTHE_COOLDOWN) == null))
                    SwirlPacket.send();
            return TypedActionResult.pass(itemStack);
        }
    }
    public static void addDeathScytheParticles(Entity entity) {
        if (MinecraftClient.getInstance().gameRenderer.getCamera().isThirdPerson() || entity != MinecraftClient.getInstance().cameraEntity) {
            for (int i = 0; i < 1; i++) {
                entity.getWorld().addParticle(ModParticles.BLOODWAVE, entity.getX(), entity.getBodyY(0.2f), entity.getZ(), 0, 0, 0);
            }
        }
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
        list.add(Text.literal(" - ").append((Text.literal("Ability : Right Click").formatted(Formatting.GOLD))).formatted(Formatting.GRAY));
        list.add((info).append((Text.literal(String.valueOf(String.format("%.2f",player.getHealth() * 0.25f)))).formatted(Formatting.RED).append(Text.literal(" ♥"))));
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
        REACH_MODIFIER = new EntityAttributeModifier(UUID.fromString("911af262-067d-4da2-854c-20f03cc2dd8b"), "Weapon modifier", 0.5D, EntityAttributeModifier.Operation.ADDITION);
        CRIT_DAMAGE_MODIFIER = new EntityAttributeModifier(UUID.fromString("4cdc0e38-c037-42bf-864f-5c745ddf0b61"), "Weapon modifier", 50.0D, EntityAttributeModifier.Operation.ADDITION);
    }
}