package com.spartanforge.godofwar.client.model;

import com.spartanforge.godofwar.GodOfWarMod;
import com.spartanforge.godofwar.entity.DraugrMobEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class DraugrMobModel extends GeoModel<DraugrMobEntity> {
    @Override
    public ResourceLocation getModelResource(DraugrMobEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "geo/draugr_mob.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(DraugrMobEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "textures/entity/draugr_mob.png");
    }

    @Override
    public ResourceLocation getAnimationResource(DraugrMobEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "animations/draugr_mob.animation.json");
    }
}
