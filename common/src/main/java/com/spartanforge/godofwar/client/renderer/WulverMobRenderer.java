package com.spartanforge.godofwar.client.renderer;

import com.spartanforge.godofwar.client.model.WulverMobModel;
import com.spartanforge.godofwar.entity.WulverMobEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class WulverMobRenderer extends GeoEntityRenderer<WulverMobEntity> {
    public WulverMobRenderer(EntityRendererProvider.Context context) {
        super(context, new WulverMobModel());
        this.scaleWidth = 1.25f;
        this.scaleHeight = 1.25f;
    }
}
