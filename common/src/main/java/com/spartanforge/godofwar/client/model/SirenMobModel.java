package com.spartanforge.godofwar.client.model;

import com.spartanforge.godofwar.GodOfWarMod;
import com.spartanforge.godofwar.entity.SirenMobEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SirenMobModel extends GeoModel<SirenMobEntity> {
    @Override
    public ResourceLocation getModelResource(SirenMobEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "geo/siren_mob.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SirenMobEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "textures/entity/siren_mob.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SirenMobEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "animations/siren_mob.animation.json");
    }
}
