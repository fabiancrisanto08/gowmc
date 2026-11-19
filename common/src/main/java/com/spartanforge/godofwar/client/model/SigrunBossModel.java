package com.spartanforge.godofwar.client.model;

import com.spartanforge.godofwar.GodOfWarMod;
import com.spartanforge.godofwar.entity.SigrunBossEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SigrunBossModel extends GeoModel<SigrunBossEntity> {
    @Override
    public ResourceLocation getModelResource(SigrunBossEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "geo/sigrun_boss.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SigrunBossEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "textures/entity/sigrun_boss.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SigrunBossEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "animations/sigrun_boss.animation.json");
    }
}
