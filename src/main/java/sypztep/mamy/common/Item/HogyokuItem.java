package sypztep.mamy.common.Item;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import sypztep.mamy.client.packetS2C.AddHogyokuParticlePacket;
import sypztep.mamy.common.init.ModDamageTypes;
import sypztep.mamy.common.init.ModEntityAttributes;
import sypztep.mamy.common.util.HogyokuState;

import java.util.List;
import java.util.Objects;

public class HogyokuItem extends Item {
    public HogyokuItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (world.isClient) {
            return TypedActionResult.fail(stack);
        }
        HogyokuState state = HogyokuState.get(Objects.requireNonNull(user.getServer()));
        String name = user.getEntityName();
        int v;
        if (state.map.containsKey(name))
            v = state.map.get(user.getEntityName()) + 1;
        else v = 0;
        if (v < 6) {
            state.map.put(name, v);
            state.markDirty();
            Objects.requireNonNull(user.getAttributeInstance(ModEntityAttributes.GENERIC_HOGYOKU)).setBaseValue(Math.max(1, v));
            stack.decrement(1);
            user.playSound(SoundEvents.BLOCK_END_PORTAL_SPAWN, SoundCategory.PLAYERS, 0.8f, 1.45f);
            user.damage(user.getWorld().getDamageSources().create(ModDamageTypes.HOGYOKU),  user.getHealth()); // consume 99% of player current health
            user.sendMessage(Text.translatable("hogyoku.success").formatted(Formatting.GOLD), true);
            ClientPlayNetworking.send(AddHogyokuParticlePacket.ID, new PacketByteBuf(Unpooled.buffer()));
            return TypedActionResult.success(stack);
        }
        user.sendMessage(Text.translatable("hogyoku.limit_reached").formatted(Formatting.DARK_RED), true);
        return TypedActionResult.fail(stack);
    }
    public static void addparticle(LivingEntity living) {
        if (MinecraftClient.getInstance().gameRenderer.getCamera().isThirdPerson() || living != MinecraftClient.getInstance().cameraEntity) {
            for (int i = 0; i < 10; ++i)
                living.getWorld().addParticle(ParticleTypes.FLASH, living.getParticleX(2), living.getRandomBodyY(), living.getParticleZ(2), 0.2 + living.getRandom().nextGaussian() / 10, 0,  0.2 +living.getRandom().nextGaussian() / 10);
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

        MutableText info = (Text.translatable(registryName + ".desc")).formatted(Formatting.GRAY);
        MutableText passive = (Text.translatable(registryName + ".desc.passive")).formatted(Formatting.GRAY);
        list.add(Text.literal(" - ").append((Text.literal("Passive : ").append(passive).formatted(Formatting.GOLD))).formatted(Formatting.GRAY));
        list.add(Text.literal(" - ").append((Text.literal("Ability : RightClick").formatted(Formatting.GOLD))).formatted(Formatting.GRAY));
        list.add((info).append((Text.literal(String.valueOf(String.format("%.2f",player.getHealth())))).formatted(Formatting.RED).append(Text.literal(" ♥"))));
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }
}
