package com.spartanforge.godofwar.client.model;

import com.spartanforge.godofwar.GodOfWarMod;
import com.spartanforge.godofwar.entity.RomanBerserkerEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class RomanBerserkerModel extends GeoModel<RomanBerserkerEntity> {
    @Override
    public ResourceLocation getModelResource(RomanBerserkerEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "geo/roman_berserker.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(RomanBerserkerEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "textures/entity/roman_berserker.png");
    }

    @Override
    public ResourceLocation getAnimationResource(RomanBerserkerEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "animations/roman_berserker.animation.json");
    }
}
