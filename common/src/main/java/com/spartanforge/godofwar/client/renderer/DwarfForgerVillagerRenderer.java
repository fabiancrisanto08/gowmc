package com.spartanforge.godofwar.client.renderer;

import com.spartanforge.godofwar.client.model.DwarfForgerVillagerModel;
import com.spartanforge.godofwar.entity.DwarfForgerVillagerEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class DwarfForgerVillagerRenderer extends GeoEntityRenderer<DwarfForgerVillagerEntity> {
    public DwarfForgerVillagerRenderer(EntityRendererProvider.Context context) {
        super(context, new DwarfForgerVillagerModel());
        this.scaleWidth = 0.85f;
        this.scaleHeight = 0.85f;
    }
}
