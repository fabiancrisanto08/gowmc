package com.spartanforge.godofwar.forge;

import com.spartanforge.godofwar.GodOfWarMod;
import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(GodOfWarMod.MOD_ID)
public final class GodOfWarForge {
    public GodOfWarForge() {
        EventBuses.registerModEventBus(GodOfWarMod.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        GodOfWarMod.init();
    }
}
