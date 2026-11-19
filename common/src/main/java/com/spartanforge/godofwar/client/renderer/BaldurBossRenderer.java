package com.spartanforge.godofwar.client.renderer;

import com.spartanforge.godofwar.client.model.BaldurBossModel;
import com.spartanforge.godofwar.entity.BaldurBossEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BaldurBossRenderer extends GeoEntityRenderer<BaldurBossEntity> {
    public BaldurBossRenderer(EntityRendererProvider.Context context) {
        super(context, new BaldurBossModel());
        this.scaleWidth = 1.10f;
        this.scaleHeight = 1.10f;
    }
}
