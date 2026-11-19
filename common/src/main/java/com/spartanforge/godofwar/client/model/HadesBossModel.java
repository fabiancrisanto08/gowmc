package com.spartanforge.godofwar.client.model;

import com.spartanforge.godofwar.GodOfWarMod;
import com.spartanforge.godofwar.entity.HadesBossEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class HadesBossModel extends GeoModel<HadesBossEntity> {
    @Override
    public ResourceLocation getModelResource(HadesBossEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "geo/hades_boss.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(HadesBossEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "textures/entity/hades_boss.png");
    }

    @Override
    public ResourceLocation getAnimationResource(HadesBossEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "animations/hades_boss.animation.json");
    }
}
