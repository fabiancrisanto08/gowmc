package com.spartanforge.godofwar.client.model;

import com.spartanforge.godofwar.GodOfWarMod;
import com.spartanforge.godofwar.entity.PersesTitanEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class PersesTitanModel extends GeoModel<PersesTitanEntity> {
    @Override
    public ResourceLocation getModelResource(PersesTitanEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "geo/perses_titan.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(PersesTitanEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "textures/entity/perses_titan.png");
    }

    @Override
    public ResourceLocation getAnimationResource(PersesTitanEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "animations/perses_titan.animation.json");
    }
}
