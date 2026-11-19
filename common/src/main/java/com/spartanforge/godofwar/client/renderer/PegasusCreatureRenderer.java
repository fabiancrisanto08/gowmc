package com.spartanforge.godofwar.client.renderer;

import com.spartanforge.godofwar.client.model.PegasusCreatureModel;
import com.spartanforge.godofwar.entity.PegasusCreatureEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class PegasusCreatureRenderer extends GeoEntityRenderer<PegasusCreatureEntity> {
    public PegasusCreatureRenderer(EntityRendererProvider.Context context) {
        super(context, new PegasusCreatureModel());
        this.scaleWidth = 1.35f;
        this.scaleHeight = 1.35f;
    }
}
