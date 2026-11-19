package com.spartanforge.godofwar.client.renderer;

import com.spartanforge.godofwar.client.model.ZeusBossModel;
import com.spartanforge.godofwar.entity.ZeusBossEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ZeusBossRenderer extends GeoEntityRenderer<ZeusBossEntity> {
    public ZeusBossRenderer(EntityRendererProvider.Context context) {
        super(context, new ZeusBossModel());
        this.scaleWidth = 1.45f;
        this.scaleHeight = 1.45f;
    }
}
