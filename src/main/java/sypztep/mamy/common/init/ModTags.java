package sypztep.mamy.common.init;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import sypztep.mamy.common.MamyMod;

public class ModTags {
    public static class Items {
        public static final TagKey<Item> DOUBLE_HANDED_ITEMS = TagKey.of(Registries.ITEM.getKey(), MamyMod.id("double_handed_items"));
    }
}
