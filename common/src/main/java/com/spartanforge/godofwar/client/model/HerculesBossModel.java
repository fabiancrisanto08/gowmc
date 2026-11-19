package com.spartanforge.godofwar.client.model;

import com.spartanforge.godofwar.GodOfWarMod;
import com.spartanforge.godofwar.entity.HerculesBossEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class HerculesBossModel extends GeoModel<HerculesBossEntity> {
    @Override
    public ResourceLocation getModelResource(HerculesBossEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "geo/hercules_boss.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(HerculesBossEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "textures/entity/hercules_boss.png");
    }

    @Override
    public ResourceLocation getAnimationResource(HerculesBossEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "animations/hercules_boss.animation.json");
    }
}
