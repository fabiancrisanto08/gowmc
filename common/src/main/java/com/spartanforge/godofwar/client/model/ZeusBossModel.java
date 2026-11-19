package com.spartanforge.godofwar.client.model;

import com.spartanforge.godofwar.GodOfWarMod;
import com.spartanforge.godofwar.entity.ZeusBossEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ZeusBossModel extends GeoModel<ZeusBossEntity> {
    @Override
    public ResourceLocation getModelResource(ZeusBossEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "geo/zeus_boss.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ZeusBossEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "textures/entity/zeus_boss.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ZeusBossEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "animations/zeus_boss.animation.json");
    }
}
