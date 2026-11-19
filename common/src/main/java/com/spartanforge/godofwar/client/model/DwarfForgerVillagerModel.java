package com.spartanforge.godofwar.client.model;

import com.spartanforge.godofwar.GodOfWarMod;
import com.spartanforge.godofwar.entity.DwarfForgerVillagerEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class DwarfForgerVillagerModel extends GeoModel<DwarfForgerVillagerEntity> {
    @Override
    public ResourceLocation getModelResource(DwarfForgerVillagerEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "geo/dwarf_forger_villager.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(DwarfForgerVillagerEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "textures/entity/dwarf_forger_villager.png");
    }

    @Override
    public ResourceLocation getAnimationResource(DwarfForgerVillagerEntity entity) {
        return new ResourceLocation(GodOfWarMod.MOD_ID, "animations/dwarf_forger_villager.animation.json");
    }
}
