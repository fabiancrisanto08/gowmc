package com.spartanforge.godofwar.client.renderer;

import com.spartanforge.godofwar.client.model.SigrunBossModel;
import com.spartanforge.godofwar.entity.SigrunBossEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SigrunBossRenderer extends GeoEntityRenderer<SigrunBossEntity> {
    public SigrunBossRenderer(EntityRendererProvider.Context context) {
        super(context, new SigrunBossModel());
        this.scaleWidth = 1.30f;
        this.scaleHeight = 1.30f;
    }
}
