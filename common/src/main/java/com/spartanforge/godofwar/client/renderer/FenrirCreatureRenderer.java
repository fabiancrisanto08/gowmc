package com.spartanforge.godofwar.client.renderer;

import com.spartanforge.godofwar.client.model.FenrirCreatureModel;
import com.spartanforge.godofwar.entity.FenrirCreatureEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class FenrirCreatureRenderer extends GeoEntityRenderer<FenrirCreatureEntity> {
    public FenrirCreatureRenderer(EntityRendererProvider.Context context) {
        super(context, new FenrirCreatureModel());
        this.scaleWidth = 2.20f;
        this.scaleHeight = 2.20f;
    }
}
