package com.spartanforge.godofwar.client.model;

import com.spartanforge.godofwar.GodOfWarMod;
import com.spartanforge.godofwar.entity.CyclopsMobEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class CyclopsMobModel extends GeoModel<CyclopsMobEntity> {
    @Override
    public ResourceLocation getModelResource(CyclopsMobEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "geo/cyclops_mob.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CyclopsMobEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "textures/entity/cyclops_mob.png");
    }

    @Override
    public ResourceLocation getAnimationResource(CyclopsMobEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "animations/cyclops_mob.animation.json");
    }
}
