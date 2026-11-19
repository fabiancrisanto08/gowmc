package com.spartanforge.godofwar;

import com.spartanforge.godofwar.registry.ModBlocks;
import com.spartanforge.godofwar.registry.ModCreativeTabs;
import com.spartanforge.godofwar.registry.ModEntities;
import com.spartanforge.godofwar.registry.ModItems;
import com.spartanforge.godofwar.registry.ModVillagers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.bernie.geckolib.GeckoLib;

public final class GodOfWarMod {
    public static final String MOD_ID = "godofwar";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void init() {
        GeckoLib.initialize();
        ModBlocks.register();
        ModItems.register();
        ModEntities.register();
        ModVillagers.register();
        ModCreativeTabs.register();
        LOGGER.info("God of War Multi-Loader inicializado ({} entidades, {} bloques, {} poderes/items)",
            43, 6, 11);
    }
}
