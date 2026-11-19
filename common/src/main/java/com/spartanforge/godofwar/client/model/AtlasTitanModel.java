package com.spartanforge.godofwar.client.model;

import com.spartanforge.godofwar.GodOfWarMod;
import com.spartanforge.godofwar.entity.AtlasTitanEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class AtlasTitanModel extends GeoModel<AtlasTitanEntity> {
    @Override
    public ResourceLocation getModelResource(AtlasTitanEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "geo/atlas_titan.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(AtlasTitanEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "textures/entity/atlas_titan.png");
    }

    @Override
    public ResourceLocation getAnimationResource(AtlasTitanEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "animations/atlas_titan.animation.json");
    }
}
