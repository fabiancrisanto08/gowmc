package com.spartanforge.godofwar.client.renderer;

import com.spartanforge.godofwar.client.model.ThorBossModel;
import com.spartanforge.godofwar.entity.ThorBossEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ThorBossRenderer extends GeoEntityRenderer<ThorBossEntity> {
    public ThorBossRenderer(EntityRendererProvider.Context context) {
        super(context, new ThorBossModel());
        this.scaleWidth = 1.35f;
        this.scaleHeight = 1.35f;
    }
}
