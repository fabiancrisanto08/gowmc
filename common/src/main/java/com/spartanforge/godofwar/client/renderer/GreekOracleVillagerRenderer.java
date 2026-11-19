package com.spartanforge.godofwar.client.renderer;

import com.spartanforge.godofwar.client.model.GreekOracleVillagerModel;
import com.spartanforge.godofwar.entity.GreekOracleVillagerEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class GreekOracleVillagerRenderer extends GeoEntityRenderer<GreekOracleVillagerEntity> {
    public GreekOracleVillagerRenderer(EntityRendererProvider.Context context) {
        super(context, new GreekOracleVillagerModel());
        this.scaleWidth = 1.00f;
        this.scaleHeight = 1.00f;
    }
}
