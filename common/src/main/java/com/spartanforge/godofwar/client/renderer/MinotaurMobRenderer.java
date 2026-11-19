package com.spartanforge.godofwar.client.renderer;

import com.spartanforge.godofwar.client.model.MinotaurMobModel;
import com.spartanforge.godofwar.entity.MinotaurMobEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class MinotaurMobRenderer extends GeoEntityRenderer<MinotaurMobEntity> {
    public MinotaurMobRenderer(EntityRendererProvider.Context context) {
        super(context, new MinotaurMobModel());
        this.scaleWidth = 1.35f;
        this.scaleHeight = 1.35f;
    }
}
