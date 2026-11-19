package com.spartanforge.godofwar.client.renderer;

import com.spartanforge.godofwar.client.model.CronosTitanModel;
import com.spartanforge.godofwar.entity.CronosTitanEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class CronosTitanRenderer extends GeoEntityRenderer<CronosTitanEntity> {
    public CronosTitanRenderer(EntityRendererProvider.Context context) {
        super(context, new CronosTitanModel());
        this.scaleWidth = 8.50f;
        this.scaleHeight = 8.50f;
    }
}
