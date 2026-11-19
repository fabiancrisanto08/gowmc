package com.spartanforge.godofwar.client.model;

import com.spartanforge.godofwar.GodOfWarMod;
import com.spartanforge.godofwar.entity.ThanatosBossEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ThanatosBossModel extends GeoModel<ThanatosBossEntity> {
    @Override
    public ResourceLocation getModelResource(ThanatosBossEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "geo/thanatos_boss.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ThanatosBossEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "textures/entity/thanatos_boss.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ThanatosBossEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "animations/thanatos_boss.animation.json");
    }
}
