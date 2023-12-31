package sypztep.mamy.mixin.vanilla.backslot;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sypztep.mamy.common.component.entity.BackWeaponComponent;
import sypztep.mamy.common.util.WeaponSlot;

@Mixin(PlayerScreenHandler.class)
public abstract class PlayerScreenHandlerMixin extends AbstractRecipeScreenHandler<CraftingInventory> {

    public PlayerScreenHandlerMixin(ScreenHandlerType<PlayerScreenHandler> screenHandlerType, int i) {
        super(screenHandlerType, i);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void onConstructed(PlayerInventory inventory, boolean onServer, PlayerEntity owner, CallbackInfo info) {
        this.addSlot(new WeaponSlot(BackWeaponComponent.getBackWeaponInventory(inventory.player), 0, 77, 44));
    }
}