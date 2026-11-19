package com.spartanforge.godofwar.client.renderer;

import com.spartanforge.godofwar.client.model.RomanPraetorianModel;
import com.spartanforge.godofwar.entity.RomanPraetorianEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RomanPraetorianRenderer extends GeoEntityRenderer<RomanPraetorianEntity> {
    public RomanPraetorianRenderer(EntityRendererProvider.Context context) {
        super(context, new RomanPraetorianModel());
        this.scaleWidth = 1.20f;
        this.scaleHeight = 1.20f;
    }
}
