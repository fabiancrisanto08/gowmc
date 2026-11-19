package com.spartanforge.godofwar.client.renderer;

import com.spartanforge.godofwar.client.model.OdinBossModel;
import com.spartanforge.godofwar.entity.OdinBossEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class OdinBossRenderer extends GeoEntityRenderer<OdinBossEntity> {
    public OdinBossRenderer(EntityRendererProvider.Context context) {
        super(context, new OdinBossModel());
        this.scaleWidth = 1.15f;
        this.scaleHeight = 1.15f;
    }
}
