package com.spartanforge.godofwar.client.renderer;

import com.spartanforge.godofwar.client.model.CentaurMobModel;
import com.spartanforge.godofwar.entity.CentaurMobEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class CentaurMobRenderer extends GeoEntityRenderer<CentaurMobEntity> {
    public CentaurMobRenderer(EntityRendererProvider.Context context) {
        super(context, new CentaurMobModel());
        this.scaleWidth = 1.50f;
        this.scaleHeight = 1.50f;
    }
}
