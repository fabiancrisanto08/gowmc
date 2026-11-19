package com.spartanforge.godofwar.client.renderer;

import com.spartanforge.godofwar.client.model.HippocampusCreatureModel;
import com.spartanforge.godofwar.entity.HippocampusCreatureEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class HippocampusCreatureRenderer extends GeoEntityRenderer<HippocampusCreatureEntity> {
    public HippocampusCreatureRenderer(EntityRendererProvider.Context context) {
        super(context, new HippocampusCreatureModel());
        this.scaleWidth = 1.30f;
        this.scaleHeight = 1.30f;
    }
}
