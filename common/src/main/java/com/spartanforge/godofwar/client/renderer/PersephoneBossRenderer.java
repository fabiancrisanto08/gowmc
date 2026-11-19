package com.spartanforge.godofwar.client.renderer;

import com.spartanforge.godofwar.client.model.PersephoneBossModel;
import com.spartanforge.godofwar.entity.PersephoneBossEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class PersephoneBossRenderer extends GeoEntityRenderer<PersephoneBossEntity> {
    public PersephoneBossRenderer(EntityRendererProvider.Context context) {
        super(context, new PersephoneBossModel());
        this.scaleWidth = 1.20f;
        this.scaleHeight = 1.20f;
    }
}
