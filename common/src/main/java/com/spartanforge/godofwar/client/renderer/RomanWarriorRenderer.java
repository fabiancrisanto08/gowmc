package com.spartanforge.godofwar.client.renderer;

import com.spartanforge.godofwar.client.model.RomanWarriorModel;
import com.spartanforge.godofwar.entity.RomanWarriorEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RomanWarriorRenderer extends GeoEntityRenderer<RomanWarriorEntity> {
    public RomanWarriorRenderer(EntityRendererProvider.Context context) {
        super(context, new RomanWarriorModel());
        this.scaleWidth = 1.00f;
        this.scaleHeight = 1.00f;
    }
}
