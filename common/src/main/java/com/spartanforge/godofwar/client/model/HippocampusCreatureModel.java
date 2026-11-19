package com.spartanforge.godofwar.client.model;

import com.spartanforge.godofwar.GodOfWarMod;
import com.spartanforge.godofwar.entity.HippocampusCreatureEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class HippocampusCreatureModel extends GeoModel<HippocampusCreatureEntity> {
    @Override
    public ResourceLocation getModelResource(HippocampusCreatureEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "geo/hippocampus_creature.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(HippocampusCreatureEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "textures/entity/hippocampus_creature.png");
    }

    @Override
    public ResourceLocation getAnimationResource(HippocampusCreatureEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "animations/hippocampus_creature.animation.json");
    }
}
