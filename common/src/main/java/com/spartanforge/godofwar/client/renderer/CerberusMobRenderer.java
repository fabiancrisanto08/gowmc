package com.spartanforge.godofwar.client.renderer;

import com.spartanforge.godofwar.client.model.CerberusMobModel;
import com.spartanforge.godofwar.entity.CerberusMobEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class CerberusMobRenderer extends GeoEntityRenderer<CerberusMobEntity> {
    public CerberusMobRenderer(EntityRendererProvider.Context context) {
        super(context, new CerberusMobModel());
        this.scaleWidth = 1.65f;
        this.scaleHeight = 1.65f;
    }
}
