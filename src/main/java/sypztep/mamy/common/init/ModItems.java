package sypztep.mamy.common.init;

import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ProjectileDispenserBehavior;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;
import sypztep.mamy.common.Item.*;
import sypztep.mamy.common.MamyMod;

import java.util.Set;

public class ModItems {
    public static final Set<MamySwordItem> ALL_SCYTHE = new ReferenceOpenHashSet<>();
    public static MamySwordItem DEATH_SCYTHE;
    public static Item HOLLOW_MASK;

    public static void init(){
        DEATH_SCYTHE = registerSworditem("death_scythe", new DeathScytheItem());
        HOLLOW_MASK = registerItem("hollow_mask", new HollowmaskItem());
    }
    public static <T extends MamySwordItem> T registerSworditem(String name, T item) {
        Registry.register(Registries.ITEM, MamyMod.id(name), item);
        ALL_SCYTHE.add(item);
        return item;
    }
    public static <T extends Item> T registerItem(String name, T item) {
        Registry.register(Registries.ITEM, MamyMod.id(name), item);
        return item;
    }
}
