package sypztep.mamy.client.registry;

import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.item.SwordItem;
import net.minecraft.registry.Registries;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import sypztep.mamy.client.render.entity.MamyItemRenderer;
import sypztep.mamy.client.render.entity.MamyTridentItemRenderer;
import sypztep.mamy.common.Item.EmptySwordItem;
import sypztep.mamy.common.init.ModItems;

public class Itemregistry {
    public static void init(){
        for (EmptySwordItem item : ModItems.ALL_SCYTHE) {
            Identifier id = Registries.ITEM.getId(item);

            MamyItemRenderer mamyItemRenderer = new MamyItemRenderer(id);
            ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(mamyItemRenderer);
            BuiltinItemRendererRegistry.INSTANCE.register(item, mamyItemRenderer);
            ModelLoadingRegistry.INSTANCE.registerModelProvider(((manager, out) -> {
                out.accept(new ModelIdentifier(id.getNamespace(),id.getPath() + "_gui","inventory"));
                out.accept(new ModelIdentifier(id.getNamespace(),id.getPath() + "_handheld","inventory"));
            }));
        }
        Registries.ITEM.forEach((item) -> {
            if(item instanceof SwordItem) {
                FabricModelPredicateProviderRegistry.register(item, new Identifier("parrying"), (stack, world, entity, i) -> entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0F : 0.0F);
            }
        });
        for (MamyTridentItem item : ModItems.ALL_TRIDENTS) {
            Identifier id = Registries.ITEM.getId(item);
            Identifier texture = new Identifier(id.getNamespace(), "textures/entity/" + id.getPath() + ".png");

            EntityModelLayer modelLayer = EntityModelLayers.TRIDENT;
            MamyTridentItemRenderer mamyTridentItemRenderer = new MamyTridentItemRenderer(id, texture, modelLayer);
            ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(mamyTridentItemRenderer);
            BuiltinItemRendererRegistry.INSTANCE.register(item, mamyTridentItemRenderer);
            ModelLoadingRegistry.INSTANCE.registerModelProvider(((manager, out) -> out.accept(new ModelIdentifier(id.getNamespace(), id.getPath() + "_in_inventory", "inventory"))));
        }
    }
}
