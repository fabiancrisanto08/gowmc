package com.spartanforge.godofwar.client.model;

import com.spartanforge.godofwar.GodOfWarMod;
import com.spartanforge.godofwar.entity.PersephoneBossEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class PersephoneBossModel extends GeoModel<PersephoneBossEntity> {
    @Override
    public ResourceLocation getModelResource(PersephoneBossEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "geo/persephone_boss.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(PersephoneBossEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "textures/entity/persephone_boss.png");
    }

    @Override
    public ResourceLocation getAnimationResource(PersephoneBossEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "animations/persephone_boss.animation.json");
    }
}
