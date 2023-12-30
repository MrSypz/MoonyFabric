package sypztep.mamy.feature.data;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import sypztep.mamy.feature.MamyFeature;
import sypztep.mamy.feature.render.entity.model.hat.OverheadModel;

import java.util.function.Function;

public class OverheadData {
	private final Function<EntityRendererFactory.Context, OverheadModel> model;
	private final Identifier texture;

	public OverheadData(Function<EntityRendererFactory.Context, OverheadModel> model, String textureName) {
		this.model = model;
		this.texture = new Identifier(MamyFeature.MODID, "textures/entity/" + textureName + ".png");
	}

	public OverheadModel createModel(EntityRendererFactory.Context ctx) {
		return model.apply(ctx);
	}

	public Identifier getTexture() {
		return texture;
	}
}
