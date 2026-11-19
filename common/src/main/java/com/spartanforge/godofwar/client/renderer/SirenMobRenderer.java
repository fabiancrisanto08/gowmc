package com.spartanforge.godofwar.client.renderer;

import com.spartanforge.godofwar.client.model.SirenMobModel;
import com.spartanforge.godofwar.entity.SirenMobEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SirenMobRenderer extends GeoEntityRenderer<SirenMobEntity> {
    public SirenMobRenderer(EntityRendererProvider.Context context) {
        super(context, new SirenMobModel());
        this.scaleWidth = 1.00f;
        this.scaleHeight = 1.00f;
    }
}
