package com.spartanforge.godofwar.client.renderer;

import com.spartanforge.godofwar.client.model.DraugrMobModel;
import com.spartanforge.godofwar.entity.DraugrMobEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class DraugrMobRenderer extends GeoEntityRenderer<DraugrMobEntity> {
    public DraugrMobRenderer(EntityRendererProvider.Context context) {
        super(context, new DraugrMobModel());
        this.scaleWidth = 1.00f;
        this.scaleHeight = 1.00f;
    }
}
