package com.spartanforge.godofwar.client.model;

import com.spartanforge.godofwar.GodOfWarMod;
import com.spartanforge.godofwar.entity.PhoenixCreatureEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class PhoenixCreatureModel extends GeoModel<PhoenixCreatureEntity> {
    @Override
    public ResourceLocation getModelResource(PhoenixCreatureEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "geo/phoenix_creature.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(PhoenixCreatureEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "textures/entity/phoenix_creature.png");
    }

    @Override
    public ResourceLocation getAnimationResource(PhoenixCreatureEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "animations/phoenix_creature.animation.json");
    }
}
