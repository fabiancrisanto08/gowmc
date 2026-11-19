package com.spartanforge.godofwar.client.model;

import com.spartanforge.godofwar.GodOfWarMod;
import com.spartanforge.godofwar.entity.CentaurMobEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class CentaurMobModel extends GeoModel<CentaurMobEntity> {
    @Override
    public ResourceLocation getModelResource(CentaurMobEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "geo/centaur_mob.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CentaurMobEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "textures/entity/centaur_mob.png");
    }

    @Override
    public ResourceLocation getAnimationResource(CentaurMobEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "animations/centaur_mob.animation.json");
    }
}
