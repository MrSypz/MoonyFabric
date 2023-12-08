package sypztep.mamy.common.Item;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
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
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sypztep.mamy.client.packet.AddSwirlingParticlePacket;
import sypztep.mamy.common.component.entity.VizardComponent;
import sypztep.mamy.common.init.*;

import java.util.List;
import java.util.UUID;

public class DeathScytheItem extends EmptySwordItem implements CustomHitSoundItem, CustomHitParticleItem{
    public DeathScytheItem() {
        super(ToolMaterials.NETHERITE,6, -3f, new Settings().fireproof());
    }
    private static final EntityAttributeModifier REACH_MODIFIER;
    private int count = 0;
    private static int counts = 0;
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

        if (!user.getWorld().isClient()) {
            System.out.println(hasvizard(user));
            if (user != null && user.isSneaking() && hasvizard(user)) {
                boolean itemToCheck = VizardComponent.hasAnyMask(user);
                ItemStack getHeadSlot = user.getEquippedStack(EquipmentSlot.HEAD);
                if (getHeadSlot.isEmpty())
                    equipMask(user);
                else {
                    if (!itemToCheck) {
                        int emptySlot = user.getInventory().getEmptySlot();
                        if (emptySlot >= 0)
                            user.getInventory().setStack(emptySlot, getHeadSlot);
                        else
                            user.dropItem(getHeadSlot, false);
                        equipMask(user);
                    }
                }
            } else if (user != null && !user.getItemCooldownManager().isCoolingDown(itemStack.getItem())) {
                ShockWaveDamage(user, 0.5f, true);
                user.heal(counts * 0.25f);
                addSwirlingParticles(user);
                count++;
                if (count >= 5) {
                    user.getItemCooldownManager().set(itemStack.getItem(), 60);
                    user.damage(user.getWorld().getDamageSources().create(ModDamageTypes.BLEEDOUT), + user.getHealth() * 0.25f);
                    user.addStatusEffect(new StatusEffectInstance(ModStatusEffects.GRIEVOUSWOUNDS,120,0,false,false,false));
                    count = 0;
                }
            }
        }
        return TypedActionResult.pass(itemStack);
    }

    public static void ShockWaveDamage(PlayerEntity user,float amount,boolean useCustomDamage) {
        double damageRadiusSquared = 5.0d;

        List<LivingEntity> entities = user.getWorld().getNonSpectatingEntities(LivingEntity.class, user.getBoundingBox().expand(damageRadiusSquared));
        counts = 0;
        for (LivingEntity target : entities) {
            if (target != user) {
                double distanceToEntity = target.squaredDistanceTo(user.getX(), user.getY(), user.getZ());
                double normalizedDistance = Math.sqrt(distanceToEntity) / damageRadiusSquared; // Adjust as needed for your range
                if (normalizedDistance > 0) {
                    counts++;
                } else counts = 0;
                if (!useCustomDamage) {
                    float damagebyArea = (float) (damageRadiusSquared - (float) (normalizedDistance * (damageRadiusSquared - 1.0f)));
                    target.damage(target.getWorld().getDamageSources().create(ModDamageTypes.MASKIMPACT, user), damagebyArea);
                } else {
                    float damagebyCustom = (amount - (float) (normalizedDistance * (amount - 0.1f)));
                    target.damage(target.getWorld().getDamageSources().create(ModDamageTypes.BLEEDOUT, user), damagebyCustom);
                }
            }
        }
    }
    private void equipMask(PlayerEntity user) {
        int baseValue = (int) user.getAttributes().getBaseValue(ModEntityAttributes.GENERIC_HOGYOKU);
        ItemStack Hollowmask = getItemStack(baseValue);

        Hollowmask.addEnchantment(Enchantments.BINDING_CURSE, 1);
        Hollowmask.addEnchantment(Enchantments.VANISHING_CURSE, 1);
        Hollowmask.addHideFlag(ItemStack.TooltipSection.ENCHANTMENTS);
        user.equipStack(EquipmentSlot.HEAD, Hollowmask);
        HollowmaskItem.useMaskParticle(user);
        ShockWaveDamage(user, 0, false);
        user.damage(user.getWorld().getDamageSources().create(ModDamageTypes.MASKIMPACT, user), user.getHealth() * 0.5f);
        user.getItemCooldownManager().set(this, 60); // 3 min
    }

    @NotNull
    private static ItemStack getItemStack(int baseValue) {
        ItemStack Hollowmask;
        if (baseValue == 1)
            Hollowmask = new ItemStack(ModItems.HALF_HOLLOW_MASK);
        else if (baseValue == 2)
            Hollowmask = new ItemStack(ModItems.HOLLOW_MASK_TIER1);
        else if (baseValue == 3)
            Hollowmask = new ItemStack(ModItems.HOLLOW_MASK_TIER2);
        else if (baseValue == 4)
            Hollowmask = new ItemStack(ModItems.HOLLOW_MASK_TIER3);
        else if (baseValue == 5)
            Hollowmask = new ItemStack(ModItems.HOLLOW_MASK_TIER4);
        else Hollowmask = new ItemStack(ModItems.HALF_HOLLOW_MASK);
        return Hollowmask;
    }

    private boolean hasvizard(PlayerEntity user) {
        if (user.getAttributes().getBaseValue(ModEntityAttributes.GENERIC_HOGYOKU) > 0)
            return true;
        return false;
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
        MutableText shiftText = Text.literal("\"Shift\"").formatted(Formatting.GRAY);
        MutableText infoText = Text.literal("Press ").append(shiftText).append(" for more info").formatted(Formatting.DARK_GRAY);
        MutableText DescText = (Text.translatable(registryName + ".desc" ,String.valueOf(String.format("%.2f",player.getHealth() * 0.5f)))).formatted(Formatting.DARK_GRAY);
        MutableText Desc2Text = (Text.translatable(registryName + ".desc2" ,String.valueOf(String.format("%.2f",player.getHealth() * 0.25f)))).formatted(Formatting.DARK_GRAY);
        MutableText lore = (Text.translatable(registryName + ".lore").formatted(Formatting.GRAY));
        if (Screen.hasShiftDown()) {
            list.add(Text.literal(" - ").append((Text.literal("Ability : Shift + RightClick").formatted(Formatting.GOLD))).formatted(Formatting.GRAY));
            list.add((DescText));
            list.add(Text.literal(" - ").append((Text.literal("Ability : RightClick").formatted(Formatting.GOLD))).formatted(Formatting.GRAY));
            list.add((Desc2Text));
        }
         else {
            list.add(lore);
            list.add(infoText);
         }
    }
    public static void addSwirlingParticles(PlayerEntity player) { //Client Packet
        ServerPlayerEntity dys = ((ServerPlayerEntity) player);
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeDouble(dys.getX() + (dys.getRandom().nextFloat() / 2) - 0.25f);
        buf.writeDouble(dys.getY() + dys.getRandom().nextFloat() + 0.1f);
        buf.writeDouble(dys.getZ() + (dys.getRandom().nextFloat() / 2) - 0.25f);
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