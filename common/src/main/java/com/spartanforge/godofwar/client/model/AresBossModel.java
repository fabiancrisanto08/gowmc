package com.spartanforge.godofwar.client.model;

import com.spartanforge.godofwar.GodOfWarMod;
import com.spartanforge.godofwar.entity.AresBossEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class AresBossModel extends GeoModel<AresBossEntity> {
    @Override
    public ResourceLocation getModelResource(AresBossEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "geo/ares_boss.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(AresBossEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "textures/entity/ares_boss.png");
    }

    @Override
    public ResourceLocation getAnimationResource(AresBossEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "animations/ares_boss.animation.json");
    }
}
