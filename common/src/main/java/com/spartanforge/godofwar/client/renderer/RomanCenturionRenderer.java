package com.spartanforge.godofwar.client.renderer;

import com.spartanforge.godofwar.client.model.RomanCenturionModel;
import com.spartanforge.godofwar.entity.RomanCenturionEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RomanCenturionRenderer extends GeoEntityRenderer<RomanCenturionEntity> {
    public RomanCenturionRenderer(EntityRendererProvider.Context context) {
        super(context, new RomanCenturionModel());
        this.scaleWidth = 1.15f;
        this.scaleHeight = 1.15f;
    }
}
