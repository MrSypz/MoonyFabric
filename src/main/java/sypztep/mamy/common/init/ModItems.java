package sypztep.mamy.common.init;

import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
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
    public static PolearmItem WOODEN_POLEARM = new PolearmItem(ToolMaterials.WOOD,4,-2.6F,new Item.Settings());
    public static PolearmItem STONE_POLEARM = new PolearmItem(ToolMaterials.STONE,4,-3F,new Item.Settings());
    public static PolearmItem IRON_POLEARM = new PolearmItem(ToolMaterials.IRON,4,-3.1F,new Item.Settings());
    public static PolearmItem GOLDEN_POLEARM = new PolearmItem(ToolMaterials.GOLD,4,-3.2F,new Item.Settings());
    public static PolearmItem DIAMOND_POLEARM = new PolearmItem(ToolMaterials.DIAMOND,4,-3F,new Item.Settings());
    public static PolearmItem NETHERITE_POLEARM = new PolearmItem(ToolMaterials.NETHERITE,4,-3F,new Item.Settings().fireproof());


    public static HollowmaskItem HALF_HOLLOW_MASK;
    public static HollowmaskItem HOLLOW_MASK_TIER1;
    public static HollowmaskItem HOLLOW_MASK_TIER2;
    public static HollowmaskItem HOLLOW_MASK_TIER3;
    public static HollowmaskItem HOLLOW_MASK_TIER4;
    public static VastomaskItem VASTO_MASK;
    public static MamyMaskCosmItem FURINA_HAT;
    public static Item ARCHAIC_EYE;

    public static void init(){
//        Registry.register(Registries.ITEM, MamyMod.id("wooden_polearm"),WOODEN_POLEARM);
        Registry.register(Registries.ITEM, MamyMod.id("stone_polearm"),STONE_POLEARM);
        Registry.register(Registries.ITEM, MamyMod.id("iron_polearm"),IRON_POLEARM);
        Registry.register(Registries.ITEM, MamyMod.id("golden_polearm"),GOLDEN_POLEARM);
        Registry.register(Registries.ITEM, MamyMod.id("diamond_polearm"),DIAMOND_POLEARM);
        Registry.register(Registries.ITEM, MamyMod.id("netherite_polearm"),NETHERITE_POLEARM);
        registeritem("wooden_polearm",WOODEN_POLEARM);

        DEATH_SCYTHE = registerSworditem("death_scythe", new DeathScytheItem());
        //COSMETIC
        FURINA_HAT = registeritem("furina_hat",new MamyMaskCosmItem(new Item.Settings()));

        ARCHAIC_EYE = registeritem("archaic_eye",new Item(new Item.Settings().fireproof().maxCount(1).rarity(Rarity.UNCOMMON)));

        //HOLLOW MASK
        HALF_HOLLOW_MASK = registerMaskItem("half_hollow_mask", new HollowmaskItem(new FabricItemSettings().maxDamage(10))); // 10 Second
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
