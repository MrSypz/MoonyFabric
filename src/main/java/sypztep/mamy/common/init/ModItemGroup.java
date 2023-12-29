package sypztep.mamy.common.init;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import sypztep.mamy.common.MamyMod;


public class ModItemGroup {
    public static void init(){
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(content -> {
            content.add(ModItems.DEATH_SCYTHE);
            content.addAfter(Items.TRIDENT, ModItems.PITCHFORK);
            content.addAfter(ModItems.PITCHFORK, ModItems.HOMA);
            content.addAfter(Items.NETHERITE_AXE, ModItems.WOODEN_POLEARM);
            content.addAfter(ModItems.WOODEN_POLEARM, ModItems.STONE_POLEARM);
            content.addAfter(ModItems.STONE_POLEARM, ModItems.IRON_POLEARM);
            content.addAfter(ModItems.IRON_POLEARM, ModItems.GOLDEN_POLEARM);
            content.addAfter(ModItems.GOLDEN_POLEARM, ModItems.DIAMOND_POLEARM);
            content.addAfter(ModItems.DIAMOND_POLEARM, ModItems.NETHERITE_POLEARM);
            content.addAfter(ModItems.DEATH_SCYTHE, ModItems.BLOODLUST);

        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(content -> {
            content.addBefore(Items.GOLDEN_CARROT,ModItems.ARCHAIC_EYE);
            content.addAfter(Items.STICK,ModItems.PACKSTICK);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(content -> {
            content.addBefore(Items.APPLE,ModItems.HOGYOKU);
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
                entries.add(ModItems.WANDERER_HAT);
                entries.add(ModItems.HANEGA);
            })
            .build();
}
