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
            content.add(ModItems.THROWING_DART);
            content.add(ModItems.PASSOUT_POISON_DART);
            content.add(ModItems.BATRACHOTOXIN_POISON_DART);
            content.add(ModItems.NUMBNESS_POISON_DART);
            content.add(ModItems.VULNERABILITY_POISON_DART);
            content.add(ModItems.TORPOR_POISON_DART);
            content.add(ModItems.STIMULATION_POISON_DART);
            content.add(ModItems.BLINDNESS_POISON_DART);
            content.add(ModItems.DEATH_SCYTHE);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(content -> {
            content.add(ModItems.BLUE_POISON_DART_FROG_BOWL);
            content.add(ModItems.GOLDEN_POISON_DART_FROG_BOWL);
            content.add(ModItems.GREEN_POISON_DART_FROG_BOWL);
            content.add(ModItems.ORANGE_POISON_DART_FROG_BOWL);
            content.add(ModItems.CRIMSON_POISON_DART_FROG_BOWL);
            content.add(ModItems.RED_POISON_DART_FROG_BOWL);
            content.add(ModItems.LUXALAMANDER_BOWL);
            content.add(ModItems.RANA_BOWL);
        });
        Registry.register(Registries.ITEM_GROUP, new Identifier(MamyMod.MODID, "mask"), MASK_GROUP);
    }
    private static final ItemGroup MASK_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(ModItems.HOLLOW_MASK))
            .displayName(Text.translatable(MamyMod.MODID+"mask"))
            .entries((context, entries) -> {
                entries.add(ModItems.HOLLOW_MASK);
            })
            .build();
}
