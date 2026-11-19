package com.spartanforge.godofwar.client.renderer;

import com.spartanforge.godofwar.client.model.EinherjarMobModel;
import com.spartanforge.godofwar.entity.EinherjarMobEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class EinherjarMobRenderer extends GeoEntityRenderer<EinherjarMobEntity> {
    public EinherjarMobRenderer(EntityRendererProvider.Context context) {
        super(context, new EinherjarMobModel());
        this.scaleWidth = 1.15f;
        this.scaleHeight = 1.15f;
    }
}
