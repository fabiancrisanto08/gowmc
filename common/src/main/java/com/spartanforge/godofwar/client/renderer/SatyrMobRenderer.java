package com.spartanforge.godofwar.client.renderer;

import com.spartanforge.godofwar.client.model.SatyrMobModel;
import com.spartanforge.godofwar.entity.SatyrMobEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SatyrMobRenderer extends GeoEntityRenderer<SatyrMobEntity> {
    public SatyrMobRenderer(EntityRendererProvider.Context context) {
        super(context, new SatyrMobModel());
        this.scaleWidth = 1.15f;
        this.scaleHeight = 1.15f;
    }
}
