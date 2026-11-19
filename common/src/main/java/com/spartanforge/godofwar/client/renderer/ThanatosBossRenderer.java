package com.spartanforge.godofwar.client.renderer;

import com.spartanforge.godofwar.client.model.ThanatosBossModel;
import com.spartanforge.godofwar.entity.ThanatosBossEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ThanatosBossRenderer extends GeoEntityRenderer<ThanatosBossEntity> {
    public ThanatosBossRenderer(EntityRendererProvider.Context context) {
        super(context, new ThanatosBossModel());
        this.scaleWidth = 1.50f;
        this.scaleHeight = 1.50f;
    }
}
