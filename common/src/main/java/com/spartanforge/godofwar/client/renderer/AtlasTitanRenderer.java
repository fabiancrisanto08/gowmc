package com.spartanforge.godofwar.client.renderer;

import com.spartanforge.godofwar.client.model.AtlasTitanModel;
import com.spartanforge.godofwar.entity.AtlasTitanEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class AtlasTitanRenderer extends GeoEntityRenderer<AtlasTitanEntity> {
    public AtlasTitanRenderer(EntityRendererProvider.Context context) {
        super(context, new AtlasTitanModel());
        this.scaleWidth = 7.00f;
        this.scaleHeight = 7.00f;
    }
}
