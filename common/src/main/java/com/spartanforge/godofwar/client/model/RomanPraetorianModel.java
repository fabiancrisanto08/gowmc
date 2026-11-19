package com.spartanforge.godofwar.client.model;

import com.spartanforge.godofwar.GodOfWarMod;
import com.spartanforge.godofwar.entity.RomanPraetorianEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class RomanPraetorianModel extends GeoModel<RomanPraetorianEntity> {
    @Override
    public ResourceLocation getModelResource(RomanPraetorianEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "geo/roman_praetorian.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(RomanPraetorianEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "textures/entity/roman_praetorian.png");
    }

    @Override
    public ResourceLocation getAnimationResource(RomanPraetorianEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "animations/roman_praetorian.animation.json");
    }
}
