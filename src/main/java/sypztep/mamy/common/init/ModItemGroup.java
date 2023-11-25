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
            })
            .build();
}
