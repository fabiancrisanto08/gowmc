package com.spartanforge.godofwar.client.model;

import com.spartanforge.godofwar.GodOfWarMod;
import com.spartanforge.godofwar.entity.PoseidonBossEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class PoseidonBossModel extends GeoModel<PoseidonBossEntity> {
    @Override
    public ResourceLocation getModelResource(PoseidonBossEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "geo/poseidon_boss.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(PoseidonBossEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "textures/entity/poseidon_boss.png");
    }

    @Override
    public ResourceLocation getAnimationResource(PoseidonBossEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "animations/poseidon_boss.animation.json");
    }
}
