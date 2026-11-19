package com.spartanforge.godofwar.client.renderer;

import com.spartanforge.godofwar.client.model.HarpyMobModel;
import com.spartanforge.godofwar.entity.HarpyMobEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class HarpyMobRenderer extends GeoEntityRenderer<HarpyMobEntity> {
    public HarpyMobRenderer(EntityRendererProvider.Context context) {
        super(context, new HarpyMobModel());
        this.scaleWidth = 0.95f;
        this.scaleHeight = 0.95f;
    }
}
