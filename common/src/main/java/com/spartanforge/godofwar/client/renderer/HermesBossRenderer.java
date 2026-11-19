package com.spartanforge.godofwar.client.renderer;

import com.spartanforge.godofwar.client.model.HermesBossModel;
import com.spartanforge.godofwar.entity.HermesBossEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class HermesBossRenderer extends GeoEntityRenderer<HermesBossEntity> {
    public HermesBossRenderer(EntityRendererProvider.Context context) {
        super(context, new HermesBossModel());
        this.scaleWidth = 1.05f;
        this.scaleHeight = 1.05f;
    }
}
