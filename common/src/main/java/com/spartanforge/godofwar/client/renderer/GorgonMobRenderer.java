package com.spartanforge.godofwar.client.renderer;

import com.spartanforge.godofwar.client.model.GorgonMobModel;
import com.spartanforge.godofwar.entity.GorgonMobEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class GorgonMobRenderer extends GeoEntityRenderer<GorgonMobEntity> {
    public GorgonMobRenderer(EntityRendererProvider.Context context) {
        super(context, new GorgonMobModel());
        this.scaleWidth = 1.15f;
        this.scaleHeight = 1.15f;
    }
}
