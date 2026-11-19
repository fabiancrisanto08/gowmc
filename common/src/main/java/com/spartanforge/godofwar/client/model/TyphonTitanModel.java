package com.spartanforge.godofwar.client.model;

import com.spartanforge.godofwar.GodOfWarMod;
import com.spartanforge.godofwar.entity.TyphonTitanEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class TyphonTitanModel extends GeoModel<TyphonTitanEntity> {
    @Override
    public ResourceLocation getModelResource(TyphonTitanEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "geo/typhon_titan.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(TyphonTitanEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "textures/entity/typhon_titan.png");
    }

    @Override
    public ResourceLocation getAnimationResource(TyphonTitanEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "animations/typhon_titan.animation.json");
    }
}
