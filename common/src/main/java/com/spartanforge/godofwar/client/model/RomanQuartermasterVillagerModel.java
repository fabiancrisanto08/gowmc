package com.spartanforge.godofwar.client.model;

import com.spartanforge.godofwar.GodOfWarMod;
import com.spartanforge.godofwar.entity.RomanQuartermasterVillagerEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class RomanQuartermasterVillagerModel extends GeoModel<RomanQuartermasterVillagerEntity> {
    @Override
    public ResourceLocation getModelResource(RomanQuartermasterVillagerEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "geo/roman_quartermaster_villager.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(RomanQuartermasterVillagerEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "textures/entity/roman_quartermaster_villager.png");
    }

    @Override
    public ResourceLocation getAnimationResource(RomanQuartermasterVillagerEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "animations/roman_quartermaster_villager.animation.json");
    }
}
