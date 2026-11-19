package com.spartanforge.godofwar.client.renderer;

import com.spartanforge.godofwar.client.model.TyphonTitanModel;
import com.spartanforge.godofwar.entity.TyphonTitanEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class TyphonTitanRenderer extends GeoEntityRenderer<TyphonTitanEntity> {
    public TyphonTitanRenderer(EntityRendererProvider.Context context) {
        super(context, new TyphonTitanModel());
        this.scaleWidth = 6.00f;
        this.scaleHeight = 6.00f;
    }
}
