package com.spartanforge.godofwar.client.model;

import com.spartanforge.godofwar.GodOfWarMod;
import com.spartanforge.godofwar.entity.RomanCenturionEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class RomanCenturionModel extends GeoModel<RomanCenturionEntity> {
    @Override
    public ResourceLocation getModelResource(RomanCenturionEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "geo/roman_centurion.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(RomanCenturionEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "textures/entity/roman_centurion.png");
    }

    @Override
    public ResourceLocation getAnimationResource(RomanCenturionEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "animations/roman_centurion.animation.json");
    }
}
