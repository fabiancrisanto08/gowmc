package com.spartanforge.godofwar.client.renderer;

import com.spartanforge.godofwar.client.model.RomanQuartermasterVillagerModel;
import com.spartanforge.godofwar.entity.RomanQuartermasterVillagerEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RomanQuartermasterVillagerRenderer extends GeoEntityRenderer<RomanQuartermasterVillagerEntity> {
    public RomanQuartermasterVillagerRenderer(EntityRendererProvider.Context context) {
        super(context, new RomanQuartermasterVillagerModel());
        this.scaleWidth = 1.00f;
        this.scaleHeight = 1.00f;
    }
}
