package sypztep.mamy.common.init;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;


public class ModItemGroup {
    public static void init(){
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(content -> {
            content.add(ModItems.DEATH_SCYTHE);
            content.addAfter(Items.TRIDENT, ModItems.PITCHFORK);
            content.addAfter(ModItems.PITCHFORK, ModItems.HELLFORK);
            content.addAfter(ModItems.HELLFORK, ModItems.SOULFORK);
            content.addAfter(ModItems.SOULFORK, ModItems.ELDER_TRIDENT);
            content.addAfter(ModItems.ELDER_TRIDENT, ModItems.HOMA);
            content.addAfter(ModItems.HOMA, ModItems.HOMASOUL);
            content.addAfter(ModItems.DEATH_SCYTHE, ModItems.BLOODLUST);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(content -> {
            content.addAfter(Items.DISC_FRAGMENT_5,ModItems.ARCHAIC_EYE);
            content.addAfter(ModItems.ARCHAIC_EYE,ModItems.PALE_CINNABAR);
            content.addAfter(ModItems.PALE_CINNABAR,ModItems.BROKEN_LUST_HANDLE);
            content.addAfter(ModItems.BROKEN_LUST_HANDLE,ModItems.ELDER_GUARDIAN_EYE);
            content.addAfter(ModItems.ELDER_GUARDIAN_EYE,ModItems.ANCIENT_TRIDENT);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(content -> {
            content.addBefore(Items.APPLE,ModItems.HOGYOKU);
        });
    }
}
