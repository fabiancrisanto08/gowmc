package com.spartanforge.godofwar.client.model;

import com.spartanforge.godofwar.GodOfWarMod;
import com.spartanforge.godofwar.entity.GorgonMobEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class GorgonMobModel extends GeoModel<GorgonMobEntity> {
    @Override
    public ResourceLocation getModelResource(GorgonMobEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "geo/gorgon_mob.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GorgonMobEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "textures/entity/gorgon_mob.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GorgonMobEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "animations/gorgon_mob.animation.json");
    }
}
