package com.spartanforge.godofwar.client.renderer;

import com.spartanforge.godofwar.client.model.RomanBerserkerModel;
import com.spartanforge.godofwar.entity.RomanBerserkerEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RomanBerserkerRenderer extends GeoEntityRenderer<RomanBerserkerEntity> {
    public RomanBerserkerRenderer(EntityRendererProvider.Context context) {
        super(context, new RomanBerserkerModel());
        this.scaleWidth = 1.20f;
        this.scaleHeight = 1.20f;
    }
}
