package com.spartanforge.godofwar.client.model;

import com.spartanforge.godofwar.GodOfWarMod;
import com.spartanforge.godofwar.entity.CronosTitanEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class CronosTitanModel extends GeoModel<CronosTitanEntity> {
    @Override
    public ResourceLocation getModelResource(CronosTitanEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "geo/cronos_titan.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CronosTitanEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "textures/entity/cronos_titan.png");
    }

    @Override
    public ResourceLocation getAnimationResource(CronosTitanEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "animations/cronos_titan.animation.json");
    }
}
