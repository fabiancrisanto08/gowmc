package com.spartanforge.godofwar.client.model;

import com.spartanforge.godofwar.GodOfWarMod;
import com.spartanforge.godofwar.entity.HeliosBossEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class HeliosBossModel extends GeoModel<HeliosBossEntity> {
    @Override
    public ResourceLocation getModelResource(HeliosBossEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "geo/helios_boss.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(HeliosBossEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "textures/entity/helios_boss.png");
    }

    @Override
    public ResourceLocation getAnimationResource(HeliosBossEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "animations/helios_boss.animation.json");
    }
}
