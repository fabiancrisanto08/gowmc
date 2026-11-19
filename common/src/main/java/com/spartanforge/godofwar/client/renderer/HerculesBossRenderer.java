package com.spartanforge.godofwar.client.renderer;

import com.spartanforge.godofwar.client.model.HerculesBossModel;
import com.spartanforge.godofwar.entity.HerculesBossEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class HerculesBossRenderer extends GeoEntityRenderer<HerculesBossEntity> {
    public HerculesBossRenderer(EntityRendererProvider.Context context) {
        super(context, new HerculesBossModel());
        this.scaleWidth = 1.40f;
        this.scaleHeight = 1.40f;
    }
}
