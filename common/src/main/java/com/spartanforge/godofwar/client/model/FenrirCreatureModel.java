package com.spartanforge.godofwar.client.model;

import com.spartanforge.godofwar.GodOfWarMod;
import com.spartanforge.godofwar.entity.FenrirCreatureEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class FenrirCreatureModel extends GeoModel<FenrirCreatureEntity> {
    @Override
    public ResourceLocation getModelResource(FenrirCreatureEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "geo/fenrir_creature.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(FenrirCreatureEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "textures/entity/fenrir_creature.png");
    }

    @Override
    public ResourceLocation getAnimationResource(FenrirCreatureEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "animations/fenrir_creature.animation.json");
    }
}
