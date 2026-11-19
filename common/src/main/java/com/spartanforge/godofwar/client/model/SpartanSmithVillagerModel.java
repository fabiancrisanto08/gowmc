package com.spartanforge.godofwar.client.model;

import com.spartanforge.godofwar.GodOfWarMod;
import com.spartanforge.godofwar.entity.SpartanSmithVillagerEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SpartanSmithVillagerModel extends GeoModel<SpartanSmithVillagerEntity> {
    @Override
    public ResourceLocation getModelResource(SpartanSmithVillagerEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "geo/spartan_smith_villager.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SpartanSmithVillagerEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "textures/entity/spartan_smith_villager.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SpartanSmithVillagerEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "animations/spartan_smith_villager.animation.json");
    }
}
