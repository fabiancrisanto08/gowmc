package com.spartanforge.godofwar.client.renderer;

import com.spartanforge.godofwar.client.model.PersesTitanModel;
import com.spartanforge.godofwar.entity.PersesTitanEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class PersesTitanRenderer extends GeoEntityRenderer<PersesTitanEntity> {
    public PersesTitanRenderer(EntityRendererProvider.Context context) {
        super(context, new PersesTitanModel());
        this.scaleWidth = 6.80f;
        this.scaleHeight = 6.80f;
    }
}
