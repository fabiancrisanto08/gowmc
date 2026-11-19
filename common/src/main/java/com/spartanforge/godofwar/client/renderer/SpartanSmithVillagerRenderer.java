package com.spartanforge.godofwar.client.renderer;

import com.spartanforge.godofwar.client.model.SpartanSmithVillagerModel;
import com.spartanforge.godofwar.entity.SpartanSmithVillagerEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SpartanSmithVillagerRenderer extends GeoEntityRenderer<SpartanSmithVillagerEntity> {
    public SpartanSmithVillagerRenderer(EntityRendererProvider.Context context) {
        super(context, new SpartanSmithVillagerModel());
        this.scaleWidth = 1.00f;
        this.scaleHeight = 1.00f;
    }
}
