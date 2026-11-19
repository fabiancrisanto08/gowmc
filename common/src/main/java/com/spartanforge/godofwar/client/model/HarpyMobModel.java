package com.spartanforge.godofwar.client.model;

import com.spartanforge.godofwar.GodOfWarMod;
import com.spartanforge.godofwar.entity.HarpyMobEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class HarpyMobModel extends GeoModel<HarpyMobEntity> {
    @Override
    public ResourceLocation getModelResource(HarpyMobEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "geo/harpy_mob.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(HarpyMobEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "textures/entity/harpy_mob.png");
    }

    @Override
    public ResourceLocation getAnimationResource(HarpyMobEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "animations/harpy_mob.animation.json");
    }
}
