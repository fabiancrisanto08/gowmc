package com.spartanforge.godofwar.client.renderer;

import com.spartanforge.godofwar.client.model.PhoenixCreatureModel;
import com.spartanforge.godofwar.entity.PhoenixCreatureEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class PhoenixCreatureRenderer extends GeoEntityRenderer<PhoenixCreatureEntity> {
    public PhoenixCreatureRenderer(EntityRendererProvider.Context context) {
        super(context, new PhoenixCreatureModel());
        this.scaleWidth = 1.50f;
        this.scaleHeight = 1.50f;
    }
}
