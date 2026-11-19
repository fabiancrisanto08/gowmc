package com.spartanforge.godofwar.client.renderer;

import com.spartanforge.godofwar.client.model.GaiaTitanModel;
import com.spartanforge.godofwar.entity.GaiaTitanEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class GaiaTitanRenderer extends GeoEntityRenderer<GaiaTitanEntity> {
    public GaiaTitanRenderer(EntityRendererProvider.Context context) {
        super(context, new GaiaTitanModel());
        this.scaleWidth = 7.50f;
        this.scaleHeight = 7.50f;
    }
}
