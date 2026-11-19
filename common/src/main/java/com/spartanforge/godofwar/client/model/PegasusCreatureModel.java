package com.spartanforge.godofwar.client.model;

import com.spartanforge.godofwar.GodOfWarMod;
import com.spartanforge.godofwar.entity.PegasusCreatureEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class PegasusCreatureModel extends GeoModel<PegasusCreatureEntity> {
    @Override
    public ResourceLocation getModelResource(PegasusCreatureEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "geo/pegasus_creature.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(PegasusCreatureEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "textures/entity/pegasus_creature.png");
    }

    @Override
    public ResourceLocation getAnimationResource(PegasusCreatureEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "animations/pegasus_creature.animation.json");
    }
}
