package com.spartanforge.godofwar.client.model;

import com.spartanforge.godofwar.GodOfWarMod;
import com.spartanforge.godofwar.entity.RomanWarriorEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class RomanWarriorModel extends GeoModel<RomanWarriorEntity> {
    @Override
    public ResourceLocation getModelResource(RomanWarriorEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "geo/roman_warrior.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(RomanWarriorEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "textures/entity/roman_warrior.png");
    }

    @Override
    public ResourceLocation getAnimationResource(RomanWarriorEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "animations/roman_warrior.animation.json");
    }
}
