package sypztep.mamy.common.init;

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.loot.function.EnchantRandomlyLootFunction;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModLootableModifiers {
    private static final Identifier MOD_DESERT_PYRAMID_ARCHAEOLOGY = new Identifier("minecraft", "archaeology/desert_pyramid");
    public static void LootTable() {
        LootTableEvents.REPLACE.register(((resourceManager, lootManager, id, original, source) -> {
            if (MOD_DESERT_PYRAMID_ARCHAEOLOGY.equals(id)) {
                List<LootPoolEntry> poolEntries = new ArrayList<>(Arrays.asList(original.pools[0].entries));
                poolEntries.add(ItemEntry.builder(Items.BOOK).weight(2)
                    .apply(new EnchantRandomlyLootFunction.Builder()
                    .add(ModEnchantments.VITALITY)).build());

                LootPool.Builder pool = LootPool.builder().with(poolEntries);
                return LootTable.builder().pool(pool).build();
            }
            return null;
        }));
    }
}
