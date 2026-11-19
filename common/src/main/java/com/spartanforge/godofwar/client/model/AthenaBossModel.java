package com.spartanforge.godofwar.client.model;

import com.spartanforge.godofwar.GodOfWarMod;
import com.spartanforge.godofwar.entity.AthenaBossEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class AthenaBossModel extends GeoModel<AthenaBossEntity> {
    @Override
    public ResourceLocation getModelResource(AthenaBossEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "geo/athena_boss.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(AthenaBossEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "textures/entity/athena_boss.png");
    }

    @Override
    public ResourceLocation getAnimationResource(AthenaBossEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "animations/athena_boss.animation.json");
    }
}
