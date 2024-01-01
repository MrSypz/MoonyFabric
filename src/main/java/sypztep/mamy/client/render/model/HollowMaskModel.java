package sypztep.mamy.client.render.model;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;
import sypztep.mamy.common.Item.MamyMaskFuncItem;
import sypztep.mamy.common.init.ModItems;

import static sypztep.mamy.common.MamyMod.MODID;

public class HollowMaskModel extends GeoModel<MamyMaskFuncItem>{
    @Override
    public Identifier getModelResource(MamyMaskFuncItem animatable) {
        return new Identifier(MODID,"geo/hollowmask.geo.json");
    }
    @Override
    public Identifier getTextureResource(MamyMaskFuncItem animatable) {
        ItemStack stack = new ItemStack(animatable);
        if (stack.isOf(ModItems.HOLLOW_MASK_TIER1))
            return new Identifier(MODID, "textures/armor/hollow_mask1_armor.png");
         else if (stack.isOf(ModItems.HOLLOW_MASK_TIER2))
            return new Identifier(MODID, "textures/armor/hollow_mask2_armor.png");
        else if (stack.isOf(ModItems.HOLLOW_MASK_TIER3))
            return new Identifier(MODID, "textures/armor/hollow_mask3_armor.png");
        else if (stack.isOf(ModItems.HOLLOW_MASK_TIER4))
            return new Identifier(MODID, "textures/armor/hollow_mask4_armor.png");
         else
            return new Identifier(MODID, "textures/armor/half_hollow_mask_armor.png");
    }
    @Override
    public Identifier getAnimationResource(MamyMaskFuncItem animatable) {
        return new Identifier(MODID,"animations/hollowmask.animation.json");
    }
}
