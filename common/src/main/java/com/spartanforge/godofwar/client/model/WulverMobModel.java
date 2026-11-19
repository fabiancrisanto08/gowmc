package com.spartanforge.godofwar.client.model;

import com.spartanforge.godofwar.GodOfWarMod;
import com.spartanforge.godofwar.entity.WulverMobEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class WulverMobModel extends GeoModel<WulverMobEntity> {
    @Override
    public ResourceLocation getModelResource(WulverMobEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "geo/wulver_mob.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(WulverMobEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "textures/entity/wulver_mob.png");
    }

    @Override
    public ResourceLocation getAnimationResource(WulverMobEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "animations/wulver_mob.animation.json");
    }
}
