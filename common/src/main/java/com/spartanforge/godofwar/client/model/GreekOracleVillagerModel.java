package com.spartanforge.godofwar.client.model;

import com.spartanforge.godofwar.GodOfWarMod;
import com.spartanforge.godofwar.entity.GreekOracleVillagerEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class GreekOracleVillagerModel extends GeoModel<GreekOracleVillagerEntity> {
    @Override
    public ResourceLocation getModelResource(GreekOracleVillagerEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "geo/greek_oracle_villager.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GreekOracleVillagerEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "textures/entity/greek_oracle_villager.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GreekOracleVillagerEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "animations/greek_oracle_villager.animation.json");
    }
}
