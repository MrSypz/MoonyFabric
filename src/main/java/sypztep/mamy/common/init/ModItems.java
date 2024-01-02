package sypztep.mamy.common.init;

import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ProjectileDispenserBehavior;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.Util;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;
import sypztep.mamy.common.Item.*;
import sypztep.mamy.common.Item.EmptySwordItem;
import sypztep.mamy.common.Item.MamyMaskCosmItem;
import sypztep.mamy.common.Item.MamyTridentItem;
import sypztep.mamy.common.Item.HomaItem;
import sypztep.mamy.common.Item.PitchforkItem;
import sypztep.mamy.common.MamyMod;
import sypztep.mamy.common.entity.projectile.MamyTridentEntity;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ModItems {
    public static final Set<EmptySwordItem> ALL_SCYTHE = new ReferenceOpenHashSet<>();
    public static final Set<HollowmaskItem> ALL_MASK = new ReferenceOpenHashSet<>();
    public static final Set<MamyTridentItem> ALL_TRIDENTS = new ReferenceOpenHashSet<>();
    public static final Set<MamyTridentItem> ALL_3DTRIDENTS = new ReferenceOpenHashSet<>();

    public static EmptySwordItem DEATH_SCYTHE;

    public static HollowmaskItem HALF_HOLLOW_MASK;
    public static HollowmaskItem HOLLOW_MASK_TIER1;
    public static HollowmaskItem HOLLOW_MASK_TIER2;
    public static HollowmaskItem HOLLOW_MASK_TIER3;
    public static HollowmaskItem HOLLOW_MASK_TIER4;
    public static VastomaskItem VASTO_MASK;

    public static MamyMaskCosmItem FURINA_HAT;
    public static MamyMaskCosmItem WANDERER_HAT;

    public static MamyTridentItem PITCHFORK;
    public static MamyTridentItem HOMA;
    public static MamyTridentItem HOMASOUL;
    public static MamyTridentItem HELLFORK;
    public static MamyTridentItem SOULFORK;
    public static MamyTridentItem ENGULFING;
    public static MamyTridentItem ELDER_TRIDENT;


    public static Item ARCHAIC_EYE;
    public static Item PALE_CINNABAR;
    public static Item BROKEN_LUST_HANDLE;
    public static Item HOGYOKU;
    public static Item ANCIENT_TRIDENT;
    public static Item ELDER_GUARDIAN_EYE;
    public static Item DEATHGOD_UPGRADE_SMITHING_TEMPLATE;

    public static SwordItem BLOODLUST;


    public static void init(){

        //TRIDENT
        ELDER_TRIDENT = registerTridentItem("elder_trident", new ElderTridentItem(new FabricItemSettings().maxDamage(250)),true);
        PITCHFORK = registerTridentItem("pitchfork", new PitchforkItem(new FabricItemSettings().maxDamage(150)),true);
        HOMA = register3DTrident("homa", new HomaItem(new FabricItemSettings().maxDamage(325).fireproof()),true);
        HOMASOUL = register3DTrident("homasoul", new HomaItem(new FabricItemSettings().maxDamage(325).fireproof()),true);
        HELLFORK = registerTridentItem("hellfork",new HellforkItem((new Item.Settings()).maxDamage(325).fireproof().fireproof()),  true);
        SOULFORK = registerTridentItem("soulfork", new HellforkItem((new Item.Settings()).maxDamage(325).fireproof().fireproof()), true);

        ENGULFING = register3DTrident("engulfing", new HomaItem(new FabricItemSettings().maxDamage(325).fireproof()),true);
        //UPGRADE TEMPLATE
        DEATHGOD_UPGRADE_SMITHING_TEMPLATE = registeritem("deathgod_upgrade_smithing_template", createDeathGodUpgradeSmithingTemplate());

        DEATH_SCYTHE = registerSworditem("death_scythe", new DeathScytheItem());
        BLOODLUST = registerSworditem("bloodlust", new BloodlustItem());
        //CONSUME
        HOGYOKU = registeritem("hogyoku", new HogyokuItem(new Item.Settings().maxCount(1).rarity(Rarity.EPIC)));

        //COSMETIC
        FURINA_HAT = registeritem("furina_hat", new MamyMaskCosmItem(new FabricItemSettings().maxCount(1).rarity(Rarity.RARE),EquipmentSlot.HEAD));
        WANDERER_HAT = registeritem("wanderer_hat", new MamyMaskCosmItem(new FabricItemSettings().maxCount(1).rarity(Rarity.RARE),EquipmentSlot.HEAD));
        //MATERIAL
        ARCHAIC_EYE = registeritem("archaic_eye", new Item(new FabricItemSettings().fireproof().maxCount(1).rarity(Rarity.UNCOMMON)));
        PALE_CINNABAR = registeritem("pale_cinnabar", new Item(new FabricItemSettings().fireproof().maxCount(1).rarity(Rarity.UNCOMMON)));
        BROKEN_LUST_HANDLE = registeritem("broken_lust_handle", new Item(new FabricItemSettings().fireproof().maxCount(1).rarity(Rarity.UNCOMMON)));
        ELDER_GUARDIAN_EYE = registeritem("elder_guardian_eye",new Item(new FabricItemSettings().maxCount(1).rarity(Rarity.UNCOMMON)));
        ANCIENT_TRIDENT = registeritem("ancient_trident",new Item(new FabricItemSettings().maxCount(1).rarity(Rarity.UNCOMMON).fireproof()));


        //HOLLOW MASK
        HALF_HOLLOW_MASK = registerMaskItem("half_hollow_mask", new HollowmaskItem(new FabricItemSettings().maxDamage(15))); // 10 Second
        HOLLOW_MASK_TIER1 = registerMaskItem("hollow_mask_1", new HollowmaskItem(new FabricItemSettings().maxDamage(30))); // 30 Second
        HOLLOW_MASK_TIER2 = registerMaskItem("hollow_mask_2", new HollowmaskItem(new FabricItemSettings().maxDamage(60))); //1 Minute
        HOLLOW_MASK_TIER3 = registerMaskItem("hollow_mask_3", new HollowmaskItem(new FabricItemSettings().maxDamage(180))); // 3 Minute
        HOLLOW_MASK_TIER4 = registerMaskItem("hollow_mask_4", new HollowmaskItem(new FabricItemSettings().maxDamage(300))); // 5 Minute
        VASTO_MASK = registerMaskItem("vasto_mask", new VastomaskItem(new FabricItemSettings().maxDamage(900))); // 15 Minute
    }

    private static <T extends MamyTridentItem> T register3DTrident(String name,T item, boolean registerDispenserBehavior) {
        Registry.register(Registries.ITEM, MamyMod.id(name), item);
        ALL_3DTRIDENTS.add(item);
        if (registerDispenserBehavior) {
            DispenserBlock.registerBehavior(item, new ProjectileDispenserBehavior() {
                @Override
                protected ProjectileEntity createProjectile(World world, Position position, ItemStack itemStack) {
                    MamyTridentEntity tridentEntity = Objects.requireNonNull(item.getEntityType(itemStack).create(world));
                    tridentEntity.setPos(position.getX(), position.getY(), position.getZ());
                    tridentEntity.setTridentAttributes(itemStack);
                    tridentEntity.setTridentStack(itemStack);
                    itemStack.decrement(1);
                    tridentEntity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
                    return tridentEntity;
                }
            });
        }
        return item;
    }

    public static <T extends EmptySwordItem> T registerSworditem(String name, T item) {
        Registry.register(Registries.ITEM, MamyMod.id(name), item);
        ALL_SCYTHE.add(item);
        return item;
    }
    public static <T extends Item> T registerMaskItem(String name, T item) {
        Registry.register(Registries.ITEM, MamyMod.id(name), item);
        ALL_MASK.add((HollowmaskItem) item);
        return item;
    }
    public static <T extends MamyTridentItem> T registerTridentItem(String name, T item, boolean registerDispenserBehavior) {
        Registry.register(Registries.ITEM, MamyMod.id(name), item);
        ALL_TRIDENTS.add(item);
        if (registerDispenserBehavior) {
            DispenserBlock.registerBehavior(item, new ProjectileDispenserBehavior() {
                @Override
                protected ProjectileEntity createProjectile(World world, Position position, ItemStack itemStack) {
                    MamyTridentEntity tridentEntity = Objects.requireNonNull(item.getEntityType(itemStack).create(world));
                    tridentEntity.setPos(position.getX(), position.getY(), position.getZ());
                    tridentEntity.setTridentAttributes(itemStack);
                    tridentEntity.setTridentStack(itemStack);
                    itemStack.decrement(1);
                    tridentEntity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
                    return tridentEntity;
                }
            });
        }
        return item;
    }
    public static <T extends Item> T registeritem(String name, T item) {
        Registry.register(Registries.ITEM, MamyMod.id(name), item);
        return item;
    }

    private static Item createDeathGodUpgradeSmithingTemplate() {
        return new SmithingTemplateItem(
                Text.translatable(Util.createTranslationKey("item", new Identifier(MamyMod.MODID, "smithing_template.warden_upgrade.applies_to"))).formatted(Formatting.BLUE),
                Text.translatable(Util.createTranslationKey("item", new Identifier(MamyMod.MODID, "smithing_template.warden_upgrade.ingredients"))).formatted(Formatting.BLUE),
                Text.translatable(Util.createTranslationKey("upgrade", new Identifier(MamyMod.MODID, "warden_upgrade"))).formatted(Formatting.GRAY),
                Text.translatable(Util.createTranslationKey("item", new Identifier(MamyMod.MODID, "smithing_template.warden_upgrade.base_slot_description"))),
                Text.translatable(Util.createTranslationKey("item", new Identifier(MamyMod.MODID, "smithing_template.warden_upgrade.additions_slot_description"))),
                getDeathGodEmptyBaseSlotTextures(),
                getWardenEmptyAdditionsSlotTextures());
    }
    private static List<Identifier> getDeathGodEmptyBaseSlotTextures() {
        return List.of(new Identifier("item/empty_armor_slot_helmet"),
                new Identifier("item/empty_armor_slot_chestplate"),
                new Identifier("item/empty_armor_slot_leggings"),
                new Identifier("item/empty_armor_slot_boots"),
                new Identifier("item/empty_slot_sword"),
                new Identifier("item/empty_slot_pickaxe"),
                new Identifier("item/empty_slot_axe"),
                new Identifier("item/empty_slot_shovel"),
                new Identifier("item/empty_slot_hoe"));
    }

    private static List<Identifier> getWardenEmptyAdditionsSlotTextures() {
        return List.of(new Identifier(MamyMod.MODID, "item/empty_slot_reinforced_echo_shard"));
    }
}
