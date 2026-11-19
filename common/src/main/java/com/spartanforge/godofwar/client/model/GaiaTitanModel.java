package com.spartanforge.godofwar.client.model;

import com.spartanforge.godofwar.GodOfWarMod;
import com.spartanforge.godofwar.entity.GaiaTitanEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class GaiaTitanModel extends GeoModel<GaiaTitanEntity> {
    @Override
    public ResourceLocation getModelResource(GaiaTitanEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "geo/gaia_titan.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GaiaTitanEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "textures/entity/gaia_titan.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GaiaTitanEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "animations/gaia_titan.animation.json");
    }
}
