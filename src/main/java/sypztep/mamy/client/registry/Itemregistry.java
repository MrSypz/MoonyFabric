package sypztep.mamy.client.registry;

import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.registry.Registries;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import sypztep.mamy.client.render.entity.MamyItemRenderer;
import sypztep.mamy.common.Item.EmptySwordItem;
import sypztep.mamy.common.init.ModItems;

public class Itemregistry {
    public static void init(){


        for (EmptySwordItem item : ModItems.ALL_SCYTHE) {
            Identifier scytheId = Registries.ITEM.getId(item);

            MamyItemRenderer mamyItemRenderer = new MamyItemRenderer(scytheId);
            ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(mamyItemRenderer);
            BuiltinItemRendererRegistry.INSTANCE.register(item, mamyItemRenderer);
//            ModelLoadingPlugin.register(pluginContext -> {
//
//            });
            ModelLoadingRegistry.INSTANCE.registerModelProvider(((manager, out) -> {
                out.accept(new ModelIdentifier(scytheId.getNamespace(),scytheId.getPath() + "_gui","inventory"));
                out.accept(new ModelIdentifier(scytheId.getNamespace(),scytheId.getPath() + "_handheld","inventory"));
            }));
        }
    }
}
