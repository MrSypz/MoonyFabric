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
    private static final Identifier DESERT_WELL_ARCHAEOLOGY_LOOT = new Identifier("minecraft", "archaeology/desert_well");
    private static final Identifier BASTION_TREASURE_CHEST_LOOT_TABLE_ID = new Identifier("minecraft", "chests/bastion_treasure");
    private static final Identifier OCEAN_RUIN_ARCHAEOLOGY_LOOT = new Identifier("minecraft","archaeology/ocean_ruin_warm");
    public static void LootTable() {
        //ARCHAEOLOGY
        LootTableEvents.REPLACE.register(((resourceManager, lootManager, id, original, source) -> {
            if (DESERT_PYRAMID_ARCHAEOLOGY_LOOT.equals(id)) {
                List<LootPoolEntry> poolEntries = new ArrayList<>(Arrays.asList(original.pools[0].entries));
                poolEntries.add(ItemEntry.builder(Items.BOOK).weight(2)
                    .apply(new EnchantRandomlyLootFunction.Builder()
                    .add(ModEnchantments.VITALITY)).build());
                poolEntries.add(ItemEntry.builder(ModItems.HOGYOKU).weight(1)
                        .build());
                LootPool.Builder pool = LootPool.builder().with(poolEntries);
                return LootTable.builder().pool(pool).build();
            }
            if (OCEAN_RUIN_ARCHAEOLOGY_LOOT.equals(id)) {
                List<LootPoolEntry> poolEntries = new ArrayList<>(Arrays.asList(original.pools[0].entries));
                poolEntries.add(ItemEntry.builder(ModItems.FURINA_HAT).weight(1)
                        .build());
                poolEntries.add(ItemEntry.builder(ModItems.WANDERER_HAT).weight(1)
                        .build());
                LootPool.Builder pool = LootPool.builder().with(poolEntries);
                return LootTable.builder().pool(pool).build();
            }
            if (DESERT_WELL_ARCHAEOLOGY_LOOT.equals(id)) {
                List<LootPoolEntry> poolEntries = new ArrayList<>(Arrays.asList(original.pools[0].entries));
                poolEntries.add(ItemEntry.builder(ModItems.BROKEN_LUST_HANDLE).weight(1)
                        .build());
                LootPool.Builder pool = LootPool.builder().with(poolEntries);
                return LootTable.builder().pool(pool).build();
            }
            return null;
        }));
        //CHEST
        //BASTION LOOT
        UniformLootNumberProvider lootTableRange = UniformLootNumberProvider.create(0, 1);
        LootCondition archaic_eye_lootchance = RandomChanceLootCondition.builder(0.5f).build();
        LootPool archaic_eye = LootPool.builder()
                .rolls(lootTableRange)
                .conditionally(archaic_eye_lootchance)
                .with(ItemEntry.builder(ModItems.ARCHAIC_EYE).build())
                .build();
        LootCondition pale_cinnabar_lootchance = RandomChanceLootCondition.builder(0.5f).build();
        LootPool pale_cinnabar = LootPool.builder()
                .rolls(lootTableRange)
                .conditionally(pale_cinnabar_lootchance)
                .with(ItemEntry.builder(ModItems.PALE_CINNABAR).build())
                .build();
        LootCondition ancient_trident__lootchance = RandomChanceLootCondition.builder(60f).build();
        LootPool ancient_trident = LootPool.builder()
                .rolls(UniformLootNumberProvider.create(1, 1))
                .conditionally(ancient_trident__lootchance)
                .with(ItemEntry.builder(ModItems.ANCIENT_TRIDENT).build())
                .build();

        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, supplier, setter) -> {
            if (BASTION_TREASURE_CHEST_LOOT_TABLE_ID.equals(id)) {
                supplier.pool(archaic_eye); // Add the first item's loot pool
                supplier.pool(pale_cinnabar); // Add the second item's loot pool
                supplier.pool(ancient_trident); // Add the second item's loot pool
            }
        });
    }
}
