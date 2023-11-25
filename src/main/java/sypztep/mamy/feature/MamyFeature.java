package sypztep.mamy.feature;

import com.google.common.collect.ImmutableMap;
import com.google.common.reflect.TypeToken;
import com.google.gson.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import sypztep.mamy.feature.data.OverheadData;
import sypztep.mamy.feature.data.PlayerCosmeticData;
import sypztep.mamy.feature.particle.pet.PlayerLanternParticle;
import sypztep.mamy.feature.particle.pet.PlayerSquareParticle;
import sypztep.mamy.feature.render.entity.feature.OverheadFeatureRenderer;
import sypztep.mamy.feature.render.entity.model.hat.*;
import sypztep.mamy.feature.render.entity.model.pet.LanternModel;
import sypztep.mamy.feature.render.entity.model.pet.SquareModel;

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
    public static ImmutableMap<String, DefaultParticleType> PETS_DATA;
    private static Map<UUID, PlayerCosmeticData> PLAYER_COSMETICS = Collections.emptyMap();

    public static DefaultParticleType LANTERN_PET;
    public static DefaultParticleType GOLDEN_COIN;
    public static DefaultParticleType YELAN_DICE;

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
        EntityModelLayerRegistry.registerModelLayer(TiaraModel.MODEL_LAYER, TiaraModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(VoidheartTiaraModel.MODEL_LAYER, VoidheartTiaraModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(WreathModel.MODEL_LAYER, WreathModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(LanternModel.MODEL_LAYER, LanternModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(SquareModel.MODEL_LAYER, SquareModel::getTexturedModelData);
        //Register PET
        LANTERN_PET = Registry.register(Registries.PARTICLE_TYPE, new Identifier(MamyFeature.MODID, "lantern_pet"), FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(MamyFeature.LANTERN_PET, fabricSpriteProvider -> new PlayerLanternParticle.DefaultFactory(fabricSpriteProvider, new Identifier(MamyFeature.MODID,
                "textures/entity/lantern.png"), 1.0f, 1.0f, 1.0f));
        GOLDEN_COIN = Registry.register(Registries.PARTICLE_TYPE, new Identifier(MamyFeature.MODID, "golden_coin_pet"), FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(MamyFeature.GOLDEN_COIN, fabricSpriteProvider -> new PlayerSquareParticle.DefaultFactory(fabricSpriteProvider, new Identifier(MamyFeature.MODID,
                "textures/entity/golden_coin.png"), 1.0f, 1.0f, 1.0f));
        YELAN_DICE = Registry.register(Registries.PARTICLE_TYPE, new Identifier(MamyFeature.MODID, "yelan_dice_pet"), FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(MamyFeature.YELAN_DICE, fabricSpriteProvider -> new PlayerSquareParticle.DefaultFactory(fabricSpriteProvider, new Identifier(MamyFeature.MODID,
                "textures/entity/yelan_dice.png"), 1.0f, 1.0f, 1.0f));
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
                .build();
        PETS_DATA = ImmutableMap.<String, DefaultParticleType>builder()
                .put("lantern", LANTERN_PET)
                .put("golden", GOLDEN_COIN)
                .put("yelan_dice", YELAN_DICE)
                .build();
    }
    private static class PlayerCosmeticDataParser implements JsonDeserializer<PlayerCosmeticData> {
        @Override
        public PlayerCosmeticData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            return new PlayerCosmeticData (
                      jsonObject.get("overhead")
                    , jsonObject.get("back")
                    , jsonObject.get("pet"));
        }
    }
}
