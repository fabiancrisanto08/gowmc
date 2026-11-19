package com.spartanforge.godofwar.client.model;

import com.spartanforge.godofwar.GodOfWarMod;
import com.spartanforge.godofwar.entity.HermesBossEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class HermesBossModel extends GeoModel<HermesBossEntity> {
    @Override
    public ResourceLocation getModelResource(HermesBossEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "geo/hermes_boss.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(HermesBossEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "textures/entity/hermes_boss.png");
    }

    @Override
    public ResourceLocation getAnimationResource(HermesBossEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "animations/hermes_boss.animation.json");
    }
}
