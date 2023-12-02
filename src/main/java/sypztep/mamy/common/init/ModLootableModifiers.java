package sypztep.mamy.common.init;

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.loot.function.EnchantRandomlyLootFunction;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModLootableModifiers {
    private static final Identifier DESERT_PYRAMID_ARCHAEOLOGY_LOOT = new Identifier("minecraft", "archaeology/desert_pyramid");
    private static final Identifier BASTION_TREASURE_CHEST_LOOT_TABLE_ID = new Identifier("minecraft", "chests/bastion_treasure");
    public static void LootTable() {
        LootTableEvents.REPLACE.register(((resourceManager, lootManager, id, original, source) -> {
            if (DESERT_PYRAMID_ARCHAEOLOGY_LOOT.equals(id)) {
                List<LootPoolEntry> poolEntries = new ArrayList<>(Arrays.asList(original.pools[0].entries));
                poolEntries.add(ItemEntry.builder(Items.BOOK).weight(2)
                    .apply(new EnchantRandomlyLootFunction.Builder()
                    .add(ModEnchantments.VITALITY)).build());

                LootPool.Builder pool = LootPool.builder().with(poolEntries);
                return LootTable.builder().pool(pool).build();
            }
            return null;
        }));
        UniformLootNumberProvider lootTableRange = UniformLootNumberProvider.create(1, 1);
        LootCondition chanceLootCondition = RandomChanceLootCondition.builder(10).build();
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, supplier, setter) -> {
            if (BASTION_TREASURE_CHEST_LOOT_TABLE_ID.equals(id)) {
                LootPool lootPool = LootPool.builder()
                        .rolls(lootTableRange)
                        .conditionally(chanceLootCondition)
                        .with(ItemEntry.builder(ModItems.ARCHAIC_EYE).build()).build();

                supplier.pool(lootPool);
            }
        });
    }
}
