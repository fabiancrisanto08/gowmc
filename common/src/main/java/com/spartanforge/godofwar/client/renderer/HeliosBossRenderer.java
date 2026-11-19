package com.spartanforge.godofwar.client.renderer;

import com.spartanforge.godofwar.client.model.HeliosBossModel;
import com.spartanforge.godofwar.entity.HeliosBossEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class HeliosBossRenderer extends GeoEntityRenderer<HeliosBossEntity> {
    public HeliosBossRenderer(EntityRendererProvider.Context context) {
        super(context, new HeliosBossModel());
        this.scaleWidth = 1.25f;
        this.scaleHeight = 1.25f;
    }
}
