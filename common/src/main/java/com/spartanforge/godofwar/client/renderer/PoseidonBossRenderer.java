package com.spartanforge.godofwar.client.renderer;

import com.spartanforge.godofwar.client.model.PoseidonBossModel;
import com.spartanforge.godofwar.entity.PoseidonBossEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class PoseidonBossRenderer extends GeoEntityRenderer<PoseidonBossEntity> {
    public PoseidonBossRenderer(EntityRendererProvider.Context context) {
        super(context, new PoseidonBossModel());
        this.scaleWidth = 1.65f;
        this.scaleHeight = 1.65f;
    }
}
