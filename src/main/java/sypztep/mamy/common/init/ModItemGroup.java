package sypztep.mamy.common.init;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import sypztep.mamy.common.MamyMod;


public class ModItemGroup {
    public static void init(){
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(content -> {
            content.add(ModItems.DEATH_SCYTHE);
        });
        Registry.register(Registries.ITEM_GROUP, new Identifier(MamyMod.MODID, "mask"), MASK_GROUP);
        Registry.register(Registries.ITEM_GROUP, new Identifier(MamyMod.MODID, "cosmetic"), COSMETIC_GROUP);
    }
    private static final ItemGroup MASK_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(ModItems.HOLLOW_MASK_TIER3))
            .displayName(Text.translatable("itemGroup." + MamyMod.MODID + "mask"))
            .entries((context, entries) -> {
                entries.add(ModItems.HALF_HOLLOW_MASK);
                entries.add(ModItems.HOLLOW_MASK_TIER1);
                entries.add(ModItems.HOLLOW_MASK_TIER2);
                entries.add(ModItems.HOLLOW_MASK_TIER3);
                entries.add(ModItems.HOLLOW_MASK_TIER4);
                entries.add(ModItems.VASTO_MASK);
            })
            .build();
    private static final ItemGroup COSMETIC_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(ModItems.FURINA_HAT))
            .displayName(Text.translatable("itemGroup." + MamyMod.MODID + "cosmetic"))
            .entries((context, entries) -> {
                entries.add(ModItems.FURINA_HAT);
                entries.add(ModItems.KITSUNE);
            })
            .build();
}
