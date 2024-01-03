package sypztep.mamy.feature;

import com.google.common.collect.ImmutableMap;
import com.google.common.reflect.TypeToken;
import com.google.gson.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.Nullable;
import sypztep.mamy.feature.data.OverheadData;
import sypztep.mamy.feature.data.PlayerCosmeticData;
import sypztep.mamy.feature.render.entity.feature.OverheadFeatureRenderer;
import sypztep.mamy.feature.render.entity.model.hat.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class MamyFeature implements ClientModInitializer {
    public static final String MODID = "mamy_feature";
    // register overhead models
    static final Type COSMETIC_SELECT_TYPE = new TypeToken<Map<UUID, PlayerCosmeticData>>() {
    }.getType();
    public static final Gson COSMETICS_GSON = new GsonBuilder().registerTypeAdapter(PlayerCosmeticData.class, new PlayerCosmeticDataParser()).create();

    // feature cosmetics
    private static final String COSMETICS_URL = "https://mrsypz.github.io/sypztep.github.io/uuidfeature.json";
    public static ImmutableMap<String, OverheadData> OVERHEADS_DATA;
    public static Map<UUID, PlayerCosmeticData> PLAYER_COSMETICS = Collections.emptyMap();

    public static @Nullable PlayerCosmeticData getCosmeticData(PlayerEntity player) {
        return PLAYER_COSMETICS.get(player.getUuid());
    }
    public static void loadPlayerCosmetics() {
        // get feature player cosmetics
        CompletableFuture.supplyAsync(() -> {
            try (Reader reader = new InputStreamReader(new URL(COSMETICS_URL).openStream())) {
                return COSMETICS_GSON.<Map<UUID, PlayerCosmeticData>>fromJson(reader, COSMETIC_SELECT_TYPE);
            } catch (IOException exception) {
                exception.printStackTrace();
            }

            return null;
        }).exceptionally(throwable -> {
            throwable.printStackTrace();
            return null;
        }).thenAcceptAsync(playerData -> {
            if (playerData != null) {
                PLAYER_COSMETICS = playerData;
            } else {
                PLAYER_COSMETICS = Collections.emptyMap();
            }
        }, MinecraftClient.getInstance());
    }
    @Override
    public void onInitializeClient() {
        loadPlayerCosmetics();
        //Register MODEL OVERHEAD
        EntityModelLayerRegistry.registerModelLayer(CrownModel.MODEL_LAYER, CrownModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(HornsModel.MODEL_LAYER, HornsModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(HaloModel.MODEL_LAYER, HaloModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(BigHaloModel.MODEL_LAYER, BigHaloModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(TiaraModel.MODEL_LAYER, TiaraModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(VoidheartTiaraModel.MODEL_LAYER, VoidheartTiaraModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(WreathModel.MODEL_LAYER, WreathModel::getTexturedModelData);
        /*
                CROWNS FEATURE RENDERER REGISTRATION
         */
        LivingEntityFeatureRendererRegistrationCallback.EVENT.register((entityType, entityRenderer, registrationHelper, context) -> {
            if (entityType == EntityType.PLAYER) {
                @SuppressWarnings("unchecked") var playerRenderer = (FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>>) entityRenderer;
                registrationHelper.register(new OverheadFeatureRenderer(playerRenderer, context));
            }
        });

        OVERHEADS_DATA = ImmutableMap.<String, OverheadData>builder()
                .put("solar_crown", new OverheadData(CrownModel::new, "solar_crown"))
                .put("frost_crown", new OverheadData(CrownModel::new, "frost_crown"))
                .put("pyro_crown", new OverheadData(CrownModel::new, "pyro_crown"))
                .put("deepsculk_horns", new OverheadData(HornsModel::new, "deepsculk_horns"))
                .put("springfae_horns", new OverheadData(HornsModel::new, "springfae_horns"))
                .put("voidheart_tiara", new OverheadData(VoidheartTiaraModel::new, "voidheart_tiara"))
                .put("worldweaver_halo", new OverheadData(HaloModel::new, "worldweaver_halo"))
                .put("summerbreeze_wreath", new OverheadData(WreathModel::new, "summerbreeze_wreath"))
                .put("timeaspect_cult_crown", new OverheadData(TiaraModel::new, "timeaspect_cult_crown"))
                .put("hanega_halo", new OverheadData(BigHaloModel::new, "hanega_halo"))
                .build();
    }
    private static class PlayerCosmeticDataParser implements JsonDeserializer<PlayerCosmeticData> {
        @Override
        public PlayerCosmeticData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            return new PlayerCosmeticData (jsonObject.get("overhead"));
        }
    }
}
