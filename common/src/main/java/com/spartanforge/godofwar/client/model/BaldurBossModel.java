package com.spartanforge.godofwar.client.model;

import com.spartanforge.godofwar.GodOfWarMod;
import com.spartanforge.godofwar.entity.BaldurBossEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BaldurBossModel extends GeoModel<BaldurBossEntity> {
    @Override
    public ResourceLocation getModelResource(BaldurBossEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "geo/baldur_boss.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BaldurBossEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "textures/entity/baldur_boss.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BaldurBossEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "animations/baldur_boss.animation.json");
    }
}
