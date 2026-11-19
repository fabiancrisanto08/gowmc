package com.spartanforge.godofwar.client.model;

import com.spartanforge.godofwar.GodOfWarMod;
import com.spartanforge.godofwar.entity.EinherjarMobEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class EinherjarMobModel extends GeoModel<EinherjarMobEntity> {
    @Override
    public ResourceLocation getModelResource(EinherjarMobEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "geo/einherjar_mob.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(EinherjarMobEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "textures/entity/einherjar_mob.png");
    }

    @Override
    public ResourceLocation getAnimationResource(EinherjarMobEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "animations/einherjar_mob.animation.json");
    }
}
