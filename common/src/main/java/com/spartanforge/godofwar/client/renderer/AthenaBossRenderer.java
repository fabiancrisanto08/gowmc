package com.spartanforge.godofwar.client.renderer;

import com.spartanforge.godofwar.client.model.AthenaBossModel;
import com.spartanforge.godofwar.entity.AthenaBossEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class AthenaBossRenderer extends GeoEntityRenderer<AthenaBossEntity> {
    public AthenaBossRenderer(EntityRendererProvider.Context context) {
        super(context, new AthenaBossModel());
        this.scaleWidth = 1.20f;
        this.scaleHeight = 1.20f;
    }
}
