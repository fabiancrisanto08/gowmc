package com.spartanforge.godofwar.client.model;

import com.spartanforge.godofwar.GodOfWarMod;
import com.spartanforge.godofwar.entity.SatyrMobEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SatyrMobModel extends GeoModel<SatyrMobEntity> {
    @Override
    public ResourceLocation getModelResource(SatyrMobEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "geo/satyr_mob.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SatyrMobEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "textures/entity/satyr_mob.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SatyrMobEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "animations/satyr_mob.animation.json");
    }
}
