package sypztep.mamy.mixin.util.accesor;

import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
@Mixin({CreativeInventoryScreen.CreativeSlot.class})

public interface CreativeSlotAccessor {
    @Accessor("slot")
    Slot getSlot();
}
