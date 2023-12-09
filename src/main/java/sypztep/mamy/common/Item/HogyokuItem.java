package sypztep.mamy.common.Item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import sypztep.mamy.common.init.ModDamageTypes;
import sypztep.mamy.common.init.ModEntityAttributes;
import sypztep.mamy.common.util.HogyokuState;

import java.util.Objects;

public class HogyokuItem extends Item {
    public HogyokuItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (world.isClient)
            return TypedActionResult.fail(stack);
        HogyokuState state = HogyokuState.get(Objects.requireNonNull(user.getServer()));
        String name = user.getEntityName();
        int v;
        if (state.map.containsKey(name))
            v = state.map.get(user.getEntityName()) + 1;
        else v = 1;
        if (v < 6) {
            state.map.put(name, v);
            state.markDirty();
            Objects.requireNonNull(user.getAttributeInstance(ModEntityAttributes.GENERIC_HOGYOKU)).setBaseValue(Math.max(1, v));
            stack.decrement(1);
            user.playSound(SoundEvents.BLOCK_END_PORTAL_SPAWN, SoundCategory.PLAYERS, 0.8f, 1.45f);
            user.damage(user.getWorld().getDamageSources().create(ModDamageTypes.HOGYOKU), + user.getHealth() * 0.99f); // consume 99% of player current health
            user.sendMessage(Text.translatable("hogyoku.success").formatted(Formatting.GOLD), true);
            return TypedActionResult.success(stack);
        }
        user.sendMessage(Text.translatable("hogyoku.limit_reached").formatted(Formatting.DARK_RED), true);
        return TypedActionResult.fail(stack);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }
}
