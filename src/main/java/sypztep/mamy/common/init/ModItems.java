package sypztep.mamy.common.init;

import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ProjectileDispenserBehavior;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;
import sypztep.mamy.common.Item.*;
import sypztep.mamy.common.MamyMod;
import sypztep.mamy.common.entity.projectile.PoisonDartEntity;

import java.util.Set;

public class ModItems {
    public static final Set<MamySwordItem> ALL_SCYTHE = new ReferenceOpenHashSet<>();
    public static Item THROWING_DART;
    public static Item PASSOUT_POISON_DART;
    public static Item BATRACHOTOXIN_POISON_DART;
    public static Item NUMBNESS_POISON_DART;
    public static Item VULNERABILITY_POISON_DART;
    public static Item TORPOR_POISON_DART;
    public static Item STIMULATION_POISON_DART;
    public static Item BLINDNESS_POISON_DART;
    public static PoisonDartFrogBowlItem BLUE_POISON_DART_FROG_BOWL;
    public static PoisonDartFrogBowlItem GOLDEN_POISON_DART_FROG_BOWL;
    public static PoisonDartFrogBowlItem GREEN_POISON_DART_FROG_BOWL;
    public static PoisonDartFrogBowlItem ORANGE_POISON_DART_FROG_BOWL;
    public static PoisonDartFrogBowlItem CRIMSON_POISON_DART_FROG_BOWL;
    public static PoisonDartFrogBowlItem RED_POISON_DART_FROG_BOWL;
    public static PoisonDartFrogBowlItem LUXALAMANDER_BOWL;
    public static PoisonDartFrogBowlItem RANA_BOWL;
    public static MamySwordItem DEATH_SCYTHE;
    public static Item HOLLOW_MASK;

    public static void init(){
        THROWING_DART = registerDartItem("throwing_dart", new ThrowingDartItem((new Item.Settings()).maxCount(64), null));
        PASSOUT_POISON_DART = registerDartItem("passout_poison_dart", new ThrowingDartItem((new Item.Settings()).maxCount(1), new StatusEffectInstance(ModStatusEffects.PASSOUT, 100))); // 5s
        BATRACHOTOXIN_POISON_DART = registerDartItem("batrachotoxin_poison_dart", new ThrowingDartItem((new Item.Settings()).maxCount(1), new StatusEffectInstance(ModStatusEffects.BATRACHOTOXIN, 80))); // 4s
        NUMBNESS_POISON_DART = registerDartItem("numbness_poison_dart", new ThrowingDartItem((new Item.Settings()).maxCount(1), new StatusEffectInstance(ModStatusEffects.NUMBNESS, 200))); // 10s
        VULNERABILITY_POISON_DART = registerDartItem("vulnerability_poison_dart", new ThrowingDartItem((new Item.Settings()).maxCount(1), new StatusEffectInstance(ModStatusEffects.VULNERABILITY, 200))); // 10s
        TORPOR_POISON_DART = registerDartItem("torpor_poison_dart", new ThrowingDartItem((new Item.Settings()).maxCount(1), new StatusEffectInstance(ModStatusEffects.TORPOR, 100))); // 5s
        STIMULATION_POISON_DART = registerDartItem("stimulation_poison_dart", new ThrowingDartItem((new Item.Settings()).maxCount(1), new StatusEffectInstance(ModStatusEffects.STIMULATION, 600))); // 30s
        BLINDNESS_POISON_DART = registerDartItem("blindness_poison_dart", new ThrowingDartItem((new Item.Settings()).maxCount(1), new StatusEffectInstance(StatusEffects.BLINDNESS, 200))); // 10s
        BLUE_POISON_DART_FROG_BOWL = registerItem("blue_poison_dart_frog_bowl", new PoisonDartFrogBowlItem((new Item.Settings()).maxCount(1), MamyMod.id("textures/entity/blue.png")));
        GOLDEN_POISON_DART_FROG_BOWL = registerItem("golden_poison_dart_frog_bowl", new PoisonDartFrogBowlItem((new Item.Settings()).maxCount(1), MamyMod.id("textures/entity/golden.png")));
        GREEN_POISON_DART_FROG_BOWL = registerItem("green_poison_dart_frog_bowl", new PoisonDartFrogBowlItem((new Item.Settings()).maxCount(1), MamyMod.id("textures/entity/green.png")));
        ORANGE_POISON_DART_FROG_BOWL = registerItem("orange_poison_dart_frog_bowl", new PoisonDartFrogBowlItem((new Item.Settings()).maxCount(1), MamyMod.id("textures/entity/orange.png")));
        CRIMSON_POISON_DART_FROG_BOWL = registerItem("crimson_poison_dart_frog_bowl", new PoisonDartFrogBowlItem((new Item.Settings()).maxCount(1), MamyMod.id("textures/entity/crimson.png")));
        RED_POISON_DART_FROG_BOWL = registerItem("red_poison_dart_frog_bowl", new PoisonDartFrogBowlItem((new Item.Settings()).maxCount(1), MamyMod.id("textures/entity/red.png")));
        LUXALAMANDER_BOWL = registerItem("luxalamander_bowl", new PoisonDartFrogBowlItem((new Item.Settings()).maxCount(1).rarity(Rarity.RARE), MamyMod.id("textures/entity/luxintrus.png")));
        RANA_BOWL = registerItem("rana_bowl", new PoisonDartFrogBowlItem((new Item.Settings()).maxCount(1).rarity(Rarity.RARE), MamyMod.id( "textures/entity/rana.png")));

        DEATH_SCYTHE = registerSworditem("death_scythe", new DeathScytheItem());
        HOLLOW_MASK = registerItem("hollow_mask", new HollowmaskItem());
    }
    public static Item registerDartItem(String name, Item item) {
        Registry.register(Registries.ITEM, MamyMod.id(name), item);
        DispenserBlock.registerBehavior(item, new ProjectileDispenserBehavior() {
            @Override
            protected ProjectileEntity createProjectile(World world, Position position, ItemStack itemStack) {
                PoisonDartEntity throwingDart = new PoisonDartEntity(world, position.getX(), position.getY(), position.getZ());
                throwingDart.setDamage(throwingDart.getDamage());
                throwingDart.setItem(itemStack);
                StatusEffectInstance statusEffectInstance = ThrowingDartItem.class.cast(itemStack.getItem()).getStatusEffectInstance();
                if (statusEffectInstance != null) {
                    StatusEffectInstance potion = new StatusEffectInstance(statusEffectInstance);
                    throwingDart.addEffect(potion);
                    throwingDart.setColor(potion.getEffectType().getColor());
                }
                itemStack.decrement(1);
                return throwingDart;
            }
        });
        return item;
    }
    public static PoisonDartFrogBowlItem[] getAllFrogBowls() {
        return new PoisonDartFrogBowlItem[]{
                BLUE_POISON_DART_FROG_BOWL,
                RED_POISON_DART_FROG_BOWL,
                CRIMSON_POISON_DART_FROG_BOWL,
                GREEN_POISON_DART_FROG_BOWL,
                GOLDEN_POISON_DART_FROG_BOWL,
                ORANGE_POISON_DART_FROG_BOWL,
                LUXALAMANDER_BOWL,
                RANA_BOWL
        };
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
