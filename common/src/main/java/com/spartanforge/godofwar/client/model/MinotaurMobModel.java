package com.spartanforge.godofwar.client.model;

import com.spartanforge.godofwar.GodOfWarMod;
import com.spartanforge.godofwar.entity.MinotaurMobEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class MinotaurMobModel extends GeoModel<MinotaurMobEntity> {
    @Override
    public ResourceLocation getModelResource(MinotaurMobEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "geo/minotaur_mob.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MinotaurMobEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "textures/entity/minotaur_mob.png");
    }

    @Override
    public ResourceLocation getAnimationResource(MinotaurMobEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "animations/minotaur_mob.animation.json");
    }
}
