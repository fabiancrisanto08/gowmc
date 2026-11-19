package com.spartanforge.godofwar.client.renderer;

import com.spartanforge.godofwar.client.model.CyclopsMobModel;
import com.spartanforge.godofwar.entity.CyclopsMobEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class CyclopsMobRenderer extends GeoEntityRenderer<CyclopsMobEntity> {
    public CyclopsMobRenderer(EntityRendererProvider.Context context) {
        super(context, new CyclopsMobModel());
        this.scaleWidth = 2.10f;
        this.scaleHeight = 2.10f;
    }
}
