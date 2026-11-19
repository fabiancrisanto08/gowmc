package com.spartanforge.godofwar.client.model;

import com.spartanforge.godofwar.GodOfWarMod;
import com.spartanforge.godofwar.entity.ThorBossEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ThorBossModel extends GeoModel<ThorBossEntity> {
    @Override
    public ResourceLocation getModelResource(ThorBossEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "geo/thor_boss.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ThorBossEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "textures/entity/thor_boss.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ThorBossEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "animations/thor_boss.animation.json");
    }
}
