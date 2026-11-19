package com.spartanforge.godofwar.client.renderer;

import com.spartanforge.godofwar.client.model.AresBossModel;
import com.spartanforge.godofwar.entity.AresBossEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class AresBossRenderer extends GeoEntityRenderer<AresBossEntity> {
    public AresBossRenderer(EntityRendererProvider.Context context) {
        super(context, new AresBossModel());
        this.scaleWidth = 1.80f;
        this.scaleHeight = 1.80f;
    }
}
