package com.spartanforge.godofwar.client.model;

import com.spartanforge.godofwar.GodOfWarMod;
import com.spartanforge.godofwar.entity.CerberusMobEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class CerberusMobModel extends GeoModel<CerberusMobEntity> {
    @Override
    public ResourceLocation getModelResource(CerberusMobEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "geo/cerberus_mob.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CerberusMobEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "textures/entity/cerberus_mob.png");
    }

    @Override
    public ResourceLocation getAnimationResource(CerberusMobEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "animations/cerberus_mob.animation.json");
    }
}
