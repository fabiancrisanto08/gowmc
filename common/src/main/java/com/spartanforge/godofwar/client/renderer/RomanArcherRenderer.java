package com.spartanforge.godofwar.client.renderer;

import com.spartanforge.godofwar.client.model.RomanArcherModel;
import com.spartanforge.godofwar.entity.RomanArcherEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RomanArcherRenderer extends GeoEntityRenderer<RomanArcherEntity> {
    public RomanArcherRenderer(EntityRendererProvider.Context context) {
        super(context, new RomanArcherModel());
        this.scaleWidth = 1.00f;
        this.scaleHeight = 1.00f;
    }
}
