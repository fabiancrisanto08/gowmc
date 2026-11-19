package com.spartanforge.godofwar.client.renderer;

import com.spartanforge.godofwar.client.model.HadesBossModel;
import com.spartanforge.godofwar.entity.HadesBossEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class HadesBossRenderer extends GeoEntityRenderer<HadesBossEntity> {
    public HadesBossRenderer(EntityRendererProvider.Context context) {
        super(context, new HadesBossModel());
        this.scaleWidth = 1.55f;
        this.scaleHeight = 1.55f;
    }
}
