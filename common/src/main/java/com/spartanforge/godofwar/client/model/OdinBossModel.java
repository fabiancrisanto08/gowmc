package com.spartanforge.godofwar.client.model;

import com.spartanforge.godofwar.GodOfWarMod;
import com.spartanforge.godofwar.entity.OdinBossEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class OdinBossModel extends GeoModel<OdinBossEntity> {
    @Override
    public ResourceLocation getModelResource(OdinBossEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "geo/odin_boss.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(OdinBossEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "textures/entity/odin_boss.png");
    }

    @Override
    public ResourceLocation getAnimationResource(OdinBossEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "animations/odin_boss.animation.json");
    }
}
