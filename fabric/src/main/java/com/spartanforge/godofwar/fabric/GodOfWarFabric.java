package com.spartanforge.godofwar.fabric;

import com.spartanforge.godofwar.GodOfWarMod;
import net.fabricmc.api.ModInitializer;

public final class GodOfWarFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        GodOfWarMod.init();
    }
}
