package com.spartanforge.godofwar.client.model;

import com.spartanforge.godofwar.GodOfWarMod;
import com.spartanforge.godofwar.entity.RomanArcherEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class RomanArcherModel extends GeoModel<RomanArcherEntity> {
    @Override
    public ResourceLocation getModelResource(RomanArcherEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "geo/roman_archer.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(RomanArcherEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "textures/entity/roman_archer.png");
    }

    @Override
    public ResourceLocation getAnimationResource(RomanArcherEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "animations/roman_archer.animation.json");
    }
}
