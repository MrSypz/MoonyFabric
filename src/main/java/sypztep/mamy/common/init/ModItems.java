package sypztep.mamy.common.init;

import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Rarity;
import sypztep.mamy.common.Item.*;
import sypztep.mamy.common.MamyMod;

import java.util.Set;

public class ModItems {
    public static final Set<EmptySwordItem> ALL_SCYTHE = new ReferenceOpenHashSet<>();
    public static final Set<HollowmaskItem> ALL_MASK = new ReferenceOpenHashSet<>();

    public static EmptySwordItem DEATH_SCYTHE;

    public static PolearmItem WOODEN_POLEARM;
    public static PolearmItem STONE_POLEARM;
    public static PolearmItem IRON_POLEARM;
    public static PolearmItem GOLDEN_POLEARM;
    public static PolearmItem DIAMOND_POLEARM;
    public static PolearmItem NETHERITE_POLEARM;


    public static HollowmaskItem HALF_HOLLOW_MASK;
    public static HollowmaskItem HOLLOW_MASK_TIER1;
    public static HollowmaskItem HOLLOW_MASK_TIER2;
    public static HollowmaskItem HOLLOW_MASK_TIER3;
    public static HollowmaskItem HOLLOW_MASK_TIER4;
    public static VastomaskItem VASTO_MASK;

    public static MamyMaskCosmItem FURINA_HAT;
    public static MamyMaskCosmItem HANEGA;
    public static MamyMaskCosmItem WANDERER_HAT;


    public static Item ARCHAIC_EYE;
    public static Item PACKSTICK;
    public static Item HOGYOKU;

    public static SwordItem BLOODLUST;


    public static void init(){
        WOODEN_POLEARM = registerSworditem("wooden_polearm", new PolearmItem(ToolMaterials.WOOD,4,-3F,new FabricItemSettings()));
        STONE_POLEARM = registerSworditem("stone_polearm", new PolearmItem(ToolMaterials.STONE,4,-3F,new FabricItemSettings()));
        IRON_POLEARM = registerSworditem("iron_polearm", new PolearmItem(ToolMaterials.IRON,4,-3F,new FabricItemSettings()));
        GOLDEN_POLEARM = registerSworditem("golden_polearm", new PolearmItem(ToolMaterials.GOLD,4,-3F,new FabricItemSettings()));
        DIAMOND_POLEARM = registerSworditem("diamond_polearm", new PolearmItem(ToolMaterials.DIAMOND,4,-3F,new FabricItemSettings()));
        NETHERITE_POLEARM= registerSworditem("netherite_polearm", new PolearmItem(ToolMaterials.NETHERITE,4,-3F,new FabricItemSettings().fireproof()));



        DEATH_SCYTHE = registerSworditem("death_scythe", new DeathScytheItem());
        BLOODLUST = registerSworditem("bloodlust", new BloodlustItem(ToolMaterials.IRON,5,-3f,new Item.Settings().rarity(Rarity.COMMON)));
        //CONSUME
        HOGYOKU = registeritem("hogyoku", new HogyokuItem(new Item.Settings().maxCount(1).rarity(Rarity.EPIC)));

        //COSMETIC
        FURINA_HAT = registeritem("furina_hat", new MamyMaskCosmItem(new FabricItemSettings().maxCount(1).rarity(Rarity.RARE),EquipmentSlot.HEAD));
        HANEGA = registeritem("hanega", new MamyMaskCosmItem(new FabricItemSettings().maxCount(1).rarity(Rarity.RARE),EquipmentSlot.HEAD));
        WANDERER_HAT = registeritem("wanderer_hat", new MamyMaskCosmItem(new FabricItemSettings().maxCount(1).rarity(Rarity.RARE),EquipmentSlot.HEAD));
        //MATERIAL
        ARCHAIC_EYE = registeritem("archaic_eye", new Item(new FabricItemSettings().fireproof().maxCount(1).rarity(Rarity.UNCOMMON)));
        PACKSTICK = registeritem("packstick", new Item(new FabricItemSettings().maxCount(64).rarity(Rarity.COMMON)));

        //HOLLOW MASK
        HALF_HOLLOW_MASK = registerMaskItem("half_hollow_mask", new HollowmaskItem(new FabricItemSettings().maxDamage(15))); // 10 Second
        HOLLOW_MASK_TIER1 = registerMaskItem("hollow_mask_1", new HollowmaskItem(new FabricItemSettings().maxDamage(30))); // 30 Second
        HOLLOW_MASK_TIER2 = registerMaskItem("hollow_mask_2", new HollowmaskItem(new FabricItemSettings().maxDamage(60))); //1 Minute
        HOLLOW_MASK_TIER3 = registerMaskItem("hollow_mask_3", new HollowmaskItem(new FabricItemSettings().maxDamage(180))); // 3 Minute
        HOLLOW_MASK_TIER4 = registerMaskItem("hollow_mask_4", new HollowmaskItem(new FabricItemSettings().maxDamage(300))); // 5 Minute
        VASTO_MASK = registerMaskItem("vasto_mask", new VastomaskItem(new FabricItemSettings().maxDamage(900))); // 15 Minute
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
    public static <T extends Item> T registeritem(String name, T item) {
        Registry.register(Registries.ITEM, MamyMod.id(name), item);
        return item;
    }
}
